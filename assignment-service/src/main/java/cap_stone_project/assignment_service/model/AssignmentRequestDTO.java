package cap_stone_project.assignment_service.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class AssignmentRequestDTO {

    @NotNull(message = "Technician Id Is Mandatory.")
    private Long technicianId;

    @NotNull(message = "ServiceRequest Id Must Be Mandatory.")
    private Long serviceRequestId;
}