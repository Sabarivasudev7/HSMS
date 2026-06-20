package com.hsms.execution_service.entity;

import java.time.LocalDateTime;

import com.hsms.execution_service.enums.BookingStatus;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "execution_tbl")
public class Execution {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long executionId;

    private Long serviceRequestId;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}