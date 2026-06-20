package cap_stone_project.assignment_service.controller;

import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cap_stone_project.assignment_service.model.AssignmentRequestDTO;
import cap_stone_project.assignment_service.model.AssignmentResponseDTO;
import cap_stone_project.assignment_service.service.AssignmentService;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/assignment")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;
    

    @PostMapping("/assign")
    public ResponseEntity<AssignmentResponseDTO> assignTechnician(@Valid @RequestBody AssignmentRequestDTO dto){
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(assignmentService.assignTechnician(dto));
    }
    

    @GetMapping("/{assignmentId}")
    public ResponseEntity<AssignmentResponseDTO> getAssignmentById(@PathVariable Long assignmentId){
        return ResponseEntity.status(HttpStatus.SC_OK).body(assignmentService.findAssignmentById(assignmentId));
    }
    
    
    @GetMapping("/technician/{technicianId}")
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByTechnician(@PathVariable Long technicianId) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(assignmentService.getAssignmentsByTechnician(technicianId));
    }
    
    
    @GetMapping("/all")
    public ResponseEntity<List<AssignmentResponseDTO>> showAllAssignments() {
    	return ResponseEntity.status(HttpStatus.SC_OK).body(assignmentService.getShowAllAssignment());
    }
    
    
    
    
    

}