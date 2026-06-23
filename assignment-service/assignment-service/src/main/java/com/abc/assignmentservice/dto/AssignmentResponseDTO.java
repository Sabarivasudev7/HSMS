package com.abc.assignmentservice.dto;

import java.time.LocalDateTime;

import com.abc.assignmentservice.enums.AssignmentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentResponseDTO {
    private Long id;
    private Long technicianId;
    private Long serviceRequestId;
    private LocalDateTime assignedDate;
    private AssignmentStatus status;
}