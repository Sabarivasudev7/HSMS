package com.hsms.execution_service.DTO;

import java.time.LocalDateTime;

import com.hsms.execution_service.enums.BookingStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class ExecutionResponseDTO {

    private Long serviceRequestId;

    private BookingStatus status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}