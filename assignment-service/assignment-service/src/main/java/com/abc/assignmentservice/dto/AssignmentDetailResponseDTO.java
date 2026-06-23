package com.abc.assignmentservice.dto;

import java.time.LocalDateTime;
import com.abc.assignmentservice.enums.AssignmentStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDetailResponseDTO {
    private Long assignmentId;
    private Long technicianId;
    private Long serviceRequestId;
    private LocalDateTime assignedDate;
    private AssignmentStatus status;

    // Technician details from User Service
    private TechnicianDetailResponseDTO technicianDetails;
}