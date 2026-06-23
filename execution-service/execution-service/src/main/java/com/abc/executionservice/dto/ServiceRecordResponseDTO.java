package com.abc.executionservice.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRecordResponseDTO {

    private Long id;
    private Long serviceRequestId;
    private Long technicianId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String remarks;
    private Double actualCost;
}