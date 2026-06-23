package com.abc.assignmentservice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.abc.assignmentservice.dto.AssignmentDetailResponseDTO;
import com.abc.assignmentservice.dto.AssignmentRequestDTO;
import com.abc.assignmentservice.dto.AssignmentResponseDTO;
import com.abc.assignmentservice.dto.TechnicianDetailResponseDTO;
import com.abc.assignmentservice.entity.Assignment;
import com.abc.assignmentservice.enums.AssignmentStatus;
import com.abc.assignmentservice.exception.AssignmentNotFoundException;
import com.abc.assignmentservice.exception.DuplicateAssignmentException;
import com.abc.assignmentservice.exception.TechnicianNotAvailableException;
import com.abc.assignmentservice.exception.SkillMismatchException;
import com.abc.assignmentservice.feign.UserServiceClient;
import com.abc.assignmentservice.feign.ServiceRequestClient;
import com.abc.assignmentservice.feign.CategoryClient;
import com.abc.assignmentservice.feign.NotificationServiceClient;
import com.abc.assignmentservice.dto.ServiceRequestDetailResponseDTO;
import com.abc.assignmentservice.dto.CategoryResponseDTO;
import com.abc.assignmentservice.dto.NotificationRequestDTO;
import com.abc.assignmentservice.enums.NotificationType;
import com.abc.assignmentservice.enums.Roles;
import com.abc.assignmentservice.repository.AssignmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final ModelMapper modelMapper;
    private final UserServiceClient userServiceClient;
    private final ServiceRequestClient serviceRequestClient;
    private final CategoryClient categoryClient;
    private final NotificationServiceClient notificationServiceClient;

    
    @Override
    public AssignmentResponseDTO assignTechnician(AssignmentRequestDTO requestDTO, Long managerId) {
        assignmentRepository.findByServiceRequestId(requestDTO.getServiceRequestId())
            .ifPresent(existing -> {
                throw new DuplicateAssignmentException(
                    "An assignment already exists for service request id: " + requestDTO.getServiceRequestId());
            });

        // 1. Fetch technician and verify availability
        ResponseEntity<TechnicianDetailResponseDTO> response =
            userServiceClient.getTechnicianById(requestDTO.getTechnicianId(), Roles.ADMIN, "1");
        TechnicianDetailResponseDTO technician = response.getBody();
        if (technician == null) {
            throw new TechnicianNotAvailableException(
                "Could not retrieve technician with id " + requestDTO.getTechnicianId() + ". User service may be unavailable.");
        }
        if (!isTechnicianAvailable(technician.getAvailability())) {
            throw new TechnicianNotAvailableException(
                "Technician with id " + requestDTO.getTechnicianId() + " is not available. Current status: " + technician.getAvailability());
        }

        // 2. Fetch service request details
        ResponseEntity<ServiceRequestDetailResponseDTO> serviceRequestResponse =
            serviceRequestClient.getRequestById(requestDTO.getServiceRequestId(), Roles.ADMIN);
        ServiceRequestDetailResponseDTO serviceRequest = serviceRequestResponse.getBody();
        if (serviceRequest == null || serviceRequest.getRequestId() == null) {
            throw new IllegalArgumentException("Service Request not found with id: " + requestDTO.getServiceRequestId());
        }

        // 3. Fetch category and verify skill match
        ResponseEntity<CategoryResponseDTO> categoryResponse =
            categoryClient.getCategoryById(serviceRequest.getCategoryId(), Roles.ADMIN);
        CategoryResponseDTO category = categoryResponse.getBody();
        if (category == null || category.getId() == null) {
            throw new IllegalArgumentException("Category not found with id: " + serviceRequest.getCategoryId());
        }

        validateSkillMatch(technician, category);

        // 4. Save assignment
        Assignment assignment = new Assignment();
        assignment.setTechnicianId(requestDTO.getTechnicianId());
        assignment.setServiceRequestId(requestDTO.getServiceRequestId());
        assignment.setAssignedDate(LocalDateTime.now());
        assignment.setStatus(AssignmentStatus.ASSIGNED);
        assignment.setServiceManagerId(managerId);

        Assignment saved = assignmentRepository.save(assignment);

        // 5. Update service request status to ASSIGNED
        serviceRequestClient.updateStatus(requestDTO.getServiceRequestId(), "ASSIGNED", Roles.ADMIN);

        // 6. Trigger notifications
        // Notify Technician
        notificationServiceClient.sendNotification(new NotificationRequestDTO(
            requestDTO.getTechnicianId(),
            NotificationType.TECHNICIAN_ASSIGNED,
            "You have been assigned to Service Request ID: " + requestDTO.getServiceRequestId()
        ));
        // Notify Customer
        notificationServiceClient.sendNotification(new NotificationRequestDTO(
            serviceRequest.getCustomerId(),
            NotificationType.TECHNICIAN_ASSIGNED,
            "Technician has been assigned to your Service Request ID: " + requestDTO.getServiceRequestId()
        ));

        return modelMapper.map(saved, AssignmentResponseDTO.class);
    }

    @Override
    public AssignmentDetailResponseDTO getAssignmentById(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new AssignmentNotFoundException("Assignment is not found: " + assignmentId));

        AssignmentDetailResponseDTO dto = modelMapper.map(assignment, AssignmentDetailResponseDTO.class);

        ResponseEntity<TechnicianDetailResponseDTO> response =
            userServiceClient.getTechnicianById(assignment.getTechnicianId(), Roles.ADMIN, "1");

        dto.setTechnicianDetails(response.getBody());
        return dto;
    }

    @Override
    public List<AssignmentDetailResponseDTO> getAllAssignments() {
        return assignmentRepository.findAll().stream()
            .map(a -> {
                AssignmentDetailResponseDTO dto = modelMapper.map(a, AssignmentDetailResponseDTO.class);
                ResponseEntity<TechnicianDetailResponseDTO> response =
                    userServiceClient.getTechnicianById(a.getTechnicianId(), Roles.ADMIN, "1");
                dto.setTechnicianDetails(response.getBody());
                return dto;
            })
            .toList();
    }

    @Override
    public List<AssignmentResponseDTO> getAssignmentsByTechnician(Long technicianId) {
        return assignmentRepository.findByTechnicianId(technicianId).stream()
            .map(a -> modelMapper.map(a, AssignmentResponseDTO.class))
            .toList();
    }

    @Override
    public AssignmentResponseDTO updateAssignmentStatus(Long assignmentId, AssignmentStatus status) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found with Id: " + assignmentId));
        assignment.setStatus(status);
        Assignment updated = assignmentRepository.save(assignment);
        return modelMapper.map(updated, AssignmentResponseDTO.class);
    }

    @Override
    public AssignmentResponseDTO reassignTechnician(Long assignmentId, Long technicianId, Long managerId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new AssignmentNotFoundException("Assignment " +assignmentId +" not found: "));

        // 1. Fetch technician and verify availability
        ResponseEntity<TechnicianDetailResponseDTO> response =
            userServiceClient.getTechnicianById(technicianId, Roles.ADMIN, "1");
        TechnicianDetailResponseDTO technician = response.getBody();
        if (technician == null) {
            throw new TechnicianNotAvailableException(
                "Could not retrieve technician with id " + technicianId + ". User service may be unavailable.");
        }
        if (!isTechnicianAvailable(technician.getAvailability())) {
            throw new TechnicianNotAvailableException(
                "Technician with id " + technicianId + " is not available. Current status: " + technician.getAvailability());
        }

        // 2. Fetch service request details
        ResponseEntity<ServiceRequestDetailResponseDTO> serviceRequestResponse =
            serviceRequestClient.getRequestById(assignment.getServiceRequestId(), Roles.ADMIN);
        ServiceRequestDetailResponseDTO serviceRequest = serviceRequestResponse.getBody();
        if (serviceRequest == null || serviceRequest.getRequestId() == null) {
            throw new IllegalArgumentException("Service Request not found with id: " + assignment.getServiceRequestId());
        }

        // 3. Fetch category and verify skill match
        ResponseEntity<CategoryResponseDTO> categoryResponse =
            categoryClient.getCategoryById(serviceRequest.getCategoryId(), Roles.ADMIN);
        CategoryResponseDTO category = categoryResponse.getBody();
        if (category == null || category.getId() == null) {
            throw new IllegalArgumentException("Category not found with id: " + serviceRequest.getCategoryId());
        }

        validateSkillMatch(technician, category);

        // 4. Update assignment
        assignment.setTechnicianId(technicianId);
        assignment.setStatus(AssignmentStatus.REASSIGNED);
        assignment.setAssignedDate(LocalDateTime.now());
        assignment.setServiceManagerId(managerId);

        Assignment updated = assignmentRepository.save(assignment);

        // 5. Update service request status to ASSIGNED
        serviceRequestClient.updateStatus(assignment.getServiceRequestId(), "ASSIGNED", Roles.ADMIN);

        // 6. Trigger notifications
        // Notify Technician
        notificationServiceClient.sendNotification(new NotificationRequestDTO(
            technicianId,
            NotificationType.TECHNICIAN_ASSIGNED,
            "You have been reassigned to Service Request ID: " + assignment.getServiceRequestId()
        ));
        // Notify Customer
        notificationServiceClient.sendNotification(new NotificationRequestDTO(
            serviceRequest.getCustomerId(),
            NotificationType.TECHNICIAN_ASSIGNED,
            "A new technician has been assigned to your Service Request ID: " + assignment.getServiceRequestId()
        ));

        return modelMapper.map(updated, AssignmentResponseDTO.class);
    }

    @Override
    public AssignmentResponseDTO acceptJob(Long assignmentId, Long userId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found: " + assignmentId));

        if (!assignment.getTechnicianId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to accept this job");
        }

        assignment.setStatus(AssignmentStatus.ACCEPTED);
        Assignment updated = assignmentRepository.save(assignment);

        // Update service request status to ACCEPTED
        serviceRequestClient.updateStatus(assignment.getServiceRequestId(), "ACCEPTED", Roles.ADMIN);

        // Notify Service Manager
        notificationServiceClient.sendNotification(new NotificationRequestDTO(
            assignment.getServiceManagerId(),
            NotificationType.JOB_ACCEPTED,
            "Technician " + userId + " has accepted the job for Service Request ID: " + assignment.getServiceRequestId()
        ));

        return modelMapper.map(updated, AssignmentResponseDTO.class);
    }

    @Override
    public AssignmentResponseDTO rejectJob(Long assignmentId, Long userId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found: " + assignmentId));

        if (!assignment.getTechnicianId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to reject this job");
        }

        assignment.setStatus(AssignmentStatus.REJECTED);
        assignmentRepository.save(assignment);

        assignment.setStatus(AssignmentStatus.REASSIGNED);
        Assignment updated = assignmentRepository.save(assignment);

        // Update service request status to REJECTED
        serviceRequestClient.updateStatus(assignment.getServiceRequestId(), "REJECTED", Roles.ADMIN);

        // Notify Service Manager
        notificationServiceClient.sendNotification(new NotificationRequestDTO(
            assignment.getServiceManagerId(),
            NotificationType.JOB_REJECTED,
            "Technician " + userId + " has rejected the job for Service Request ID: " + assignment.getServiceRequestId()
        ));

        return modelMapper.map(updated, AssignmentResponseDTO.class);
    }

    @Override
    public long getActiveTechniciansCount(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        List<Assignment> assignments = assignmentRepository.findByAssignedDateBetweenAndStatusNot(
                startOfDay, endOfDay, AssignmentStatus.REJECTED);
        return assignments.stream()
                .map(Assignment::getTechnicianId)
                .distinct()
                .count();
    }
    private void validateSkillMatch(TechnicianDetailResponseDTO technician, CategoryResponseDTO category) {
        if (technician.getSkill() == null || category.getName() == null
                || !technician.getSkill().trim().equalsIgnoreCase(category.getName().trim())) {
            throw new SkillMismatchException("Technician skill '" + technician.getSkill()
                    + "' does not match service category '" + category.getName() + "'");
        }
    }

    private boolean isTechnicianAvailable(String availability) {
        return "AVAILABLE".equalsIgnoreCase(availability)
                || "TRUE".equalsIgnoreCase(availability)
                || "YES".equalsIgnoreCase(availability);
    }

}