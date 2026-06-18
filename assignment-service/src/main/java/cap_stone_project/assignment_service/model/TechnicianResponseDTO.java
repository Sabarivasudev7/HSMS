package cap_stone_project.assignment_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TechnicianResponseDTO {

    private Long technicianId;
    
    private String technicianName;
    
    private Boolean availability;
}