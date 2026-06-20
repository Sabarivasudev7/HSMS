package com.hsms.execution_service.DTO;

import com.hsms.execution_service.enums.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class BookingResponseDTO {

    private Long requestId;

    private BookingStatus status;
}