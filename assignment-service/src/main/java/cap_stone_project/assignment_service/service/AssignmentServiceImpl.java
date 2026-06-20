package cap_stone_project.assignment_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cap_stone_project.assignment_service.entity.Assignment;
import cap_stone_project.assignment_service.enums.BookingStatus;
import cap_stone_project.assignment_service.exception.AssignmentNotFoundException;
import cap_stone_project.assignment_service.exception.DuplicateAssignmentException;
import cap_stone_project.assignment_service.exception.ServiceUnavailableException;
import cap_stone_project.assignment_service.feignclient.BookingFeignClient;
import cap_stone_project.assignment_service.feignclient.TechnicianFeignClient;
import cap_stone_project.assignment_service.model.AssignmentRequestDTO;
import cap_stone_project.assignment_service.model.AssignmentResponseDTO;
import cap_stone_project.assignment_service.model.BookingResponseDTO;
import cap_stone_project.assignment_service.model.TechnicianResponseDTO;
import cap_stone_project.assignment_service.repository.AssignmentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class AssignmentServiceImpl implements AssignmentService {

	@Autowired
	private AssignmentRepository assignmentRepo;

	@Autowired
	private BookingFeignClient bookingFeignClient;

	@Autowired
	private TechnicianFeignClient technicianFeignClient;

	@Autowired
	private ModelMapper modelMapper;
	
	
	@Override
	@CircuitBreaker(name = "assignmentService", fallbackMethod = "assignTechnicianFallback")
	public AssignmentResponseDTO assignTechnician(AssignmentRequestDTO dto) {

		BookingResponseDTO booking = bookingFeignClient.getBookingById(dto.getServiceRequestId());

		if (booking == null) {
			throw new AssignmentNotFoundException("Booking Not Found");
		}

		TechnicianResponseDTO technician = technicianFeignClient.getTechnicianById(dto.getTechnicianId());

		if (technician == null) {
			throw new AssignmentNotFoundException("Technician Not Found");
		}

		if (!technician.getAvailability()) {
			throw new AssignmentNotFoundException("Technician Not Available");
		}

		assignmentRepo.findByServiceRequestId(dto.getServiceRequestId()).ifPresent(a -> {
			throw new DuplicateAssignmentException("Assignment Already Exists");
		});

		Assignment assignment = modelMapper.map(dto, Assignment.class);

		assignment.setAssignedDate(LocalDateTime.now());

		Assignment saved = assignmentRepo.save(assignment);

		bookingFeignClient.markAsAssigned(dto.getServiceRequestId());

		AssignmentResponseDTO response = modelMapper.map(saved, AssignmentResponseDTO.class);

		response.setStatus("ASSIGNED");

		return response;
	}
	
	public AssignmentResponseDTO assignTechnicianFallback(AssignmentRequestDTO dto, Exception ex) {
		throw new ServiceUnavailableException("Booking Service or Technician Service is currently unavailable.");
	}
	
	

	@Override
	public AssignmentResponseDTO findAssignmentById(Long assignmentId) {
		Assignment assignment = assignmentRepo.findById(assignmentId).orElseThrow(() -> new AssignmentNotFoundException("Assignment Not Found"));
		return modelMapper.map(assignment, AssignmentResponseDTO.class);
	}

	@Override
	public List<AssignmentResponseDTO> getAssignmentsByTechnician(Long technicianId) {
		return assignmentRepo.findByTechnicianId(technicianId).stream().map(a -> modelMapper.map(a, AssignmentResponseDTO.class)).toList();
	}
	
	@Override
	public List<AssignmentResponseDTO> getShowAllAssignment() {
		List<Assignment> assignList = assignmentRepo.findAll();
		return assignList.stream().map(assign -> modelMapper.map(assign, AssignmentResponseDTO.class)).collect(Collectors.toList());
	
	}
	

	
	// Technician can accept assigned jobs
	// Status updated to ACCEPTED
	@Override
	public String acceptBooking(Long requestId) {
		assignmentRepo.findByServiceRequestId(requestId).orElseThrow(() -> new AssignmentNotFoundException("Assignment Not Found"));
		bookingFeignClient.updateBookingStatus(requestId, BookingStatus.ACCEPTED);
		return "Booking Accepted Successfully";
	}

	
	
	// Technician can reject assigned jobs
	// Status updated to REJECTED
	@Override
	public String rejectBooking(Long requestId) {
		assignmentRepo.findByServiceRequestId(requestId).orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
		bookingFeignClient.updateBookingStatus(requestId, BookingStatus.REJECTED);
	    return "Booking Rejected Successfully";
	}

	
	// Only ACCEPTED requests can be started
	// Status changes to IN_PROGRESS
	@Override
	public String startService(Long requestId) {
		assignmentRepo.findByServiceRequestId(requestId).orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
		bookingFeignClient.updateBookingStatus(requestId, BookingStatus.IN_PROGRESS);
	    return "Service Started Successfully";
	}

	
	// Only IN_PROGRESS requests can be completed
	// Status changes to COMPLETED
	@Override
	public String completeService(Long requestId) {
		assignmentRepo.findByServiceRequestId(requestId).orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
		bookingFeignClient.updateBookingStatus(requestId, BookingStatus.COMPLETED);
	    return "Service Completed Successfully";
	}
	
	
	
	

	

}
