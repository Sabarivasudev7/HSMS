package com.abc.executionservice.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRecordRequestDTO {

    @NotNull(message = "Service Request ID is required")
    @Positive(message = "Service Request ID must be positive")
    private Long serviceRequestId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks;

    @PositiveOrZero(message = "Actual cost cannot be negative")
    private Double actualCost;
}
