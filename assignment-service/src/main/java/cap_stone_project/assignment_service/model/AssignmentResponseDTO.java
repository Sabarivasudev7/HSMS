package cap_stone_project.assignment_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AssignmentResponseDTO {

    private Long assignmentId;

    private Long technicianId;

    private Long serviceRequestId;

    private String status;
}