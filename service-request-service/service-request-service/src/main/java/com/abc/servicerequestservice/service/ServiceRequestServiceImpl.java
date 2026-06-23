package com.abc.servicerequestservice.service;

import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.abc.servicerequestservice.dto.ServiceRequestDetailResponse;
import com.abc.servicerequestservice.dto.ServiceRequestRequest;
import com.abc.servicerequestservice.dto.ServiceRequestUpdateDTO;
import com.abc.servicerequestservice.entity.ServiceRequest;
import com.abc.servicerequestservice.enums.RequestStatus;
import com.abc.servicerequestservice.exception.InvalidRequestStatusException;
import com.abc.servicerequestservice.exception.ServiceRequestNotFoundException;
import com.abc.servicerequestservice.feign.AssignmentClient;
import com.abc.servicerequestservice.feign.NotificationClient;
import com.abc.servicerequestservice.feign.UserClient;
import com.abc.servicerequestservice.repository.ServiceRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceRequestServiceImpl
        implements ServiceRequestService {

	private final ServiceRequestRepository repository;
	private final ApplicationEventPublisher eventPublisher;
	private final AssignmentClient assignmentClient;
	private final NotificationClient notificationClient;
	private final UserClient userClient;

	@Override
	public ServiceRequestDetailResponse createRequest(
	        ServiceRequestRequest request) {

	    ServiceRequest serviceRequest =
	            new ServiceRequest();

	    serviceRequest.setCustomerId(
	            request.getCustomerId());

	    serviceRequest.setCategoryId(
	            request.getCategoryId());

	    serviceRequest.setServiceType(
	            request.getServiceType());

	    serviceRequest.setAddress(
	            request.getAddress());

	    serviceRequest.setScheduledTime(
	            request.getScheduledTime());
	    serviceRequest.setStatus(
	            RequestStatus.CREATED);

	    ServiceRequest saved =
	            repository.save(serviceRequest);
        // Notify Customer about Service request creation
        NotificationClient.NotificationRequest notif = new NotificationClient.NotificationRequest();
        notif.setUserId(saved.getCustomerId());
        notif.setType("SERVICE_CREATED");
        notif.setMessage("Your service request for " + saved.getServiceType()
                + " has been successfully created with ID: " + saved.getId());
        notificationClient.sendNotification(notif);

	    // Publish event for assignment processing
	    eventPublisher.publishEvent(saved);

	    return convertToResponse(saved);
	}

    @Override
    public ServiceRequestDetailResponse getRequestById(
            Long requestId) {

        ServiceRequest request =
                repository.findById(requestId)
                .orElseThrow(() ->
                        new ServiceRequestNotFoundException(
                                "Request not found with id : "
                                        + requestId));

        return convertToResponse(request);
    }

    @Override
    public List<ServiceRequestDetailResponse>
    getAllRequests() {

        return repository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public Page<ServiceRequestDetailResponse>
    getRequestsByCustomer(
            Long customerId,
            Pageable pageable) {

        return repository
                .findByCustomerId(
                        customerId,
                        pageable)
                .map(this::convertToResponse);
    }

    @Override
    public ServiceRequestDetailResponse updateRequest(
            Long requestId,
            ServiceRequestUpdateDTO dto) {

        ServiceRequest request =
                repository.findById(requestId)
                .orElseThrow(() ->
                        new ServiceRequestNotFoundException(
                                "Request not found with id : "
                                        + requestId));

        request.setServiceType(dto.getServiceType());
        request.setAddress(dto.getAddress());
        request.setScheduledTime(
                dto.getScheduledTime());

        ServiceRequest updated =
                repository.save(request);

        return convertToResponse(updated);
    }

    @Override
    public ServiceRequestDetailResponse updateRequestStatus(
            Long requestId,
            RequestStatus status) {

        ServiceRequest request =
                repository.findById(requestId)
                .orElseThrow(() ->
                        new ServiceRequestNotFoundException(
                                "Request not found"));

        if(request.getStatus() ==
                RequestStatus.CANCELLED) {

            throw new InvalidRequestStatusException(
                    "Cancelled request cannot be updated");
        }

        request.setStatus(status);

        ServiceRequest saved = repository.save(request);
        // Notify Customer when request is COMPLETED
        if (status == RequestStatus.COMPLETED) {
            NotificationClient.NotificationRequest notif = new NotificationClient.NotificationRequest();
            notif.setUserId(saved.getCustomerId());
            notif.setType("SERVICE_COMPLETED");
            notif.setMessage("Your service request for " + saved.getServiceType()
                    + " (ID: " + saved.getId() + ") has been completed successfully!");
            notificationClient.sendNotification(notif);
        }

        return convertToResponse(saved);
    }

    @Override
    public void cancelRequest(Long requestId) {

        ServiceRequest request =
                repository.findById(requestId)
                .orElseThrow(() ->
                        new ServiceRequestNotFoundException(
                                "Request not found"));

        if(request.getStatus() != RequestStatus.CREATED
                &&
           request.getStatus() != RequestStatus.ASSIGNED) {

            throw new InvalidRequestStatusException(
                    "Only CREATED or ASSIGNED requests can be cancelled");
        }

        // Retrieve assigned technician if status was ASSIGNED
        Long assignedTechnicianId = null;
        if (request.getStatus() == RequestStatus.ASSIGNED) {
            ResponseEntity<List<AssignmentClient.AssignmentDto>> assignmentsRes = 
                assignmentClient.getAllAssignments("ADMIN");
            if (assignmentsRes.getStatusCode().is2xxSuccessful() && assignmentsRes.getBody() != null) {
                assignedTechnicianId = assignmentsRes.getBody().stream()
                    .filter(a -> request.getId().equals(a.getServiceRequestId()))
                    .findFirst()
                    .map(AssignmentClient.AssignmentDto::getTechnicianId)
                    .orElse(null);
            }
        }

        request.setStatus(RequestStatus.CANCELLED);
        repository.save(request);

        // Notify assigned technician if there was one
        if (assignedTechnicianId != null) {
            NotificationClient.NotificationRequest notif = new NotificationClient.NotificationRequest();
            notif.setUserId(assignedTechnicianId);
            notif.setType("JOB_REJECTED");
            notif.setMessage("Service Request ID " + requestId + " has been cancelled.");
            notificationClient.sendNotification(notif);
        }
    }

    private ServiceRequestDetailResponse
    convertToResponse(ServiceRequest request) {

        ServiceRequestDetailResponse response =
                new ServiceRequestDetailResponse();

        response.setRequestId(request.getId());
        response.setCustomerId(
                request.getCustomerId());
        response.setCategoryId(
                request.getCategoryId());
        response.setServiceType(
                request.getServiceType());
        response.setStatus(
                request.getStatus());
        response.setAddress(
                request.getAddress());
        response.setScheduledTime(
                request.getScheduledTime());

        // Fetch technician details if request is assigned or proceeding
        if (request.getStatus() != RequestStatus.CREATED && request.getStatus() != RequestStatus.CANCELLED) {
            ResponseEntity<List<AssignmentClient.AssignmentDto>> assignmentsRes = 
                assignmentClient.getAllAssignments("ADMIN");
            if (assignmentsRes.getStatusCode().is2xxSuccessful() && assignmentsRes.getBody() != null) {
                assignmentsRes.getBody().stream()
                    .filter(a -> request.getId().equals(a.getServiceRequestId()))
                    .findFirst()
                    .ifPresent(assignment -> {
                        response.setTechnicianId(assignment.getTechnicianId());

                        ResponseEntity<UserClient.UserDto> userRes = 
                            userClient.getUserProfile(assignment.getTechnicianId(), "ADMIN");
                        if (userRes.getStatusCode().is2xxSuccessful() && userRes.getBody() != null) {
                            response.setTechnicianName(userRes.getBody().getName());
                        }
                    });
            }
        }

        return response;
    }
}