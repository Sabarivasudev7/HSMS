package cap_stone_project.assignment_service.service;

import java.util.List;

import cap_stone_project.assignment_service.model.AssignmentRequestDTO;
import cap_stone_project.assignment_service.model.AssignmentResponseDTO;

public interface AssignmentService {

    AssignmentResponseDTO assignTechnician(AssignmentRequestDTO dto);

    AssignmentResponseDTO findAssignmentById(Long assignmentId);
    
    List<AssignmentResponseDTO> getAssignmentsByTechnician(Long technicianId);
    
    List<AssignmentResponseDTO> getShowAllAssignment();
    


   
    
}