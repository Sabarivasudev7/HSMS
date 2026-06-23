package com.abc.executionservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestResponseDTO {

    private Long requestId;
    private String status;
    private Long technicianId;
}