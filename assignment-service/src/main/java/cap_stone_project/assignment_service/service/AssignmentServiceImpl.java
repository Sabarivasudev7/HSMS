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
		
		if (booking == null) {throw new AssignmentNotFoundException("Booking Not Found");}

		
		TechnicianResponseDTO technician = technicianFeignClient.getTechnicianById(dto.getTechnicianId());
		
		if (technician == null) {throw new AssignmentNotFoundException("Technician Not Found");}

		
		if (!technician.getAvailability()) {throw new AssignmentNotFoundException("Technician Not Available");}
		
		assignmentRepo.findByServiceRequestId(dto.getServiceRequestId()).ifPresent(a -> {
			throw new DuplicateAssignmentException("Assignment Already Exists");	});

		
//		Assignment assignment = modelMapper.map(dto, Assignment.class);
		Assignment assignment = new Assignment();

		assignment.setTechnicianId(dto.getTechnicianId());
		assignment.setServiceRequestId(dto.getServiceRequestId());
		assignment.setAssignedDate(LocalDateTime.now());
		

		Assignment saved = assignmentRepo.save(assignment);

		bookingFeignClient.markAsAssigned(dto.getServiceRequestId());

		AssignmentResponseDTO response = modelMapper.map(saved, AssignmentResponseDTO.class);

		response.setStatus(BookingStatus.ASSIGNED);

		return response;
	}
	
	
	public AssignmentResponseDTO assignTechnicianFallback(AssignmentRequestDTO dto, Exception ex) {
		throw new ServiceUnavailableException("Booking Service or Technician Service is currently unavailable.");
//		ex.printStackTrace();
//	    throw new ServiceUnavailableException("REAL ERROR : " + ex.getMessage());
//		
	}
	
	

	@Override
	@CircuitBreaker(name = "assignmentService", fallbackMethod = "findAssignmentByIdFallback")
	public AssignmentResponseDTO findAssignmentById(Long assignmentId) {
		Assignment assignment = assignmentRepo.findById(assignmentId).orElseThrow(() -> new AssignmentNotFoundException("Assignment Not Found"));
		
		AssignmentResponseDTO response = modelMapper.map(assignment, AssignmentResponseDTO.class);
	    BookingResponseDTO booking = bookingFeignClient.getBookingById(assignment.getServiceRequestId());
	    response.setStatus(BookingStatus.valueOf(booking.getStatus()));
	    return response;	
	}
	public AssignmentResponseDTO findAssignmentByIdFallback(Long assignmentId, Exception ex) {
	    throw new ServiceUnavailableException("Booking Service is currently unavailable.");
	}

	
	
	@Override
	@CircuitBreaker(name = "assignmentService", fallbackMethod = "getAssignmentsByTechnicianFallback")
	public List<AssignmentResponseDTO> getAssignmentsByTechnician(Long technicianId) {
		return assignmentRepo.findByTechnicianId(technicianId).stream().map(a -> {
        AssignmentResponseDTO response =  modelMapper.map(a, AssignmentResponseDTO.class);
        BookingResponseDTO booking = bookingFeignClient.getBookingById(a.getServiceRequestId());
        response.setStatus(BookingStatus.valueOf(booking.getStatus()));
        return response; }).toList();	
	}
	public List<AssignmentResponseDTO> getAssignmentsByTechnicianFallback(Long technicianId, Exception ex) {
	    throw new ServiceUnavailableException("Booking Service is currently unavailable.");
	}

	
	
	@Override
	@CircuitBreaker(name = "assignmentService", fallbackMethod = "getShowAllAssignmentFallback")
	public List<AssignmentResponseDTO> getShowAllAssignment() {
		List<Assignment> assignList = assignmentRepo.findAll();
		return assignList.stream().map(assign -> {
        AssignmentResponseDTO response = modelMapper.map(assign, AssignmentResponseDTO.class);
        BookingResponseDTO booking = bookingFeignClient.getBookingById(assign.getServiceRequestId());
        response.setStatus(BookingStatus.valueOf(booking.getStatus()));
        return response; }).collect(Collectors.toList());	
	}
	public List<AssignmentResponseDTO> getShowAllAssignmentFallback(Exception ex) {
	    throw new ServiceUnavailableException("Booking Service is currently unavailable.");
	}
	

	
		
	
	

	

}
