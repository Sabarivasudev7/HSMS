package com.hsms.analytics.model;

import lombok.Data;

@Data
public class PaymentResponseDto {
    private Long paymentId;
    private Long customerId;
    private Double amount;
    private String status;
}