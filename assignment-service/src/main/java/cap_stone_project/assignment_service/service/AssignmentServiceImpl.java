package cap_stone_project.assignment_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cap_stone_project.assignment_service.entity.Assignment;
import cap_stone_project.assignment_service.exception.AssignmentNotFoundException;
import cap_stone_project.assignment_service.feignclient.BookingFeignClient;
import cap_stone_project.assignment_service.feignclient.TechnicianFeignClient;
import cap_stone_project.assignment_service.model.AssignmentRequestDTO;
import cap_stone_project.assignment_service.model.AssignmentResponseDTO;
import cap_stone_project.assignment_service.model.BookingResponseDTO;
import cap_stone_project.assignment_service.model.TechnicianResponseDTO;
import cap_stone_project.assignment_service.repository.AssignmentRepository;

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
	public AssignmentResponseDTO assignTechnician(AssignmentRequestDTO dto) {

		BookingResponseDTO booking =bookingFeignClient.getBookingById(dto.getServiceRequestId());
		
	    if(booking == null) {
	        throw new AssignmentNotFoundException("Booking Not Found");
	    }

	    TechnicianResponseDTO technician = technicianFeignClient.getTechnicianById(dto.getTechnicianId());
	    
	    if(technician == null) {
	        throw new AssignmentNotFoundException("Technician Not Found");
	    }

	    if(!technician.getAvailability()) {
	        throw new AssignmentNotFoundException( "Technician Not Available");
	    }

	    assignmentRepo.findByServiceRequestId(dto.getServiceRequestId()).ifPresent(a -> {
	                throw new RuntimeException("Assignment Already Exists");
	    });

	    Assignment assignment = modelMapper.map(dto, Assignment.class);
	    
	    assignment.setAssignedDate(LocalDateTime.now());

	    Assignment saved = assignmentRepo.save(assignment);

	    bookingFeignClient.markAssigned(dto.getServiceRequestId());
	    AssignmentResponseDTO response = modelMapper.map(saved,	AssignmentResponseDTO.class);
	    response.setStatus("ASSIGNED");

	    return response;
	}

	
	
	
	@Override
	public AssignmentResponseDTO findAssignmentById(Long assignmentId) {
		 Assignment assignment = assignmentRepo.findById(assignmentId).orElseThrow(() ->
		                    new AssignmentNotFoundException("Assignment Not Found"));
		 return modelMapper.map(assignment, AssignmentResponseDTO.class);
	}

	
	
	
	@Override
	public List<AssignmentResponseDTO> getAssignmentsByTechnician(Long technicianId) {
		return assignmentRepo.findByTechnicianId(technicianId).stream().map(a ->
		                 modelMapper.map(a, AssignmentResponseDTO.class)).toList();
	}

}
