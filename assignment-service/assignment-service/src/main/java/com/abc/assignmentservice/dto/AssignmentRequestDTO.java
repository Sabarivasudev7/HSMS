package com.abc.assignmentservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentRequestDTO {

    @NotNull(message = "Technician ID is required")
    @Positive(message = "Technician ID must be positive")
    private Long technicianId;

    @NotNull(message = "Service Request ID is required")
    @Positive(message = "Service Request ID must be positive")
    private Long serviceRequestId;
}
