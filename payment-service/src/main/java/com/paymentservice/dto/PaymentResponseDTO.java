package com.paymentservice.dto;

import lombok.Data;

@Data
public class PaymentResponseDTO {

    private Long paymentId;
    private Long serviceRequestId;
    private String status;
    private Double amount;
    private String method;
}