package com.abc.assignmentservice.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestDetailResponseDTO {
    private Long requestId;
    private Long customerId;
    private Long categoryId;
    private String serviceType;
    private String status;
    private String address;
    private LocalDateTime scheduledTime;
    private Long technicianId;
    private String technicianName;
}
