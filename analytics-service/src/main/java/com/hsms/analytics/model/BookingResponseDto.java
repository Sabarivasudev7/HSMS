package com.hsms.analytics.model;

import lombok.Data;

@Data
public class BookingResponseDto {
    private Long requestId;
    private String categoryName;
    private Long customerId;
    private String status;
}