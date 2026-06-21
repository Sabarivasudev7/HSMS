package com.feedback_service.dto;

import lombok.Data;

@Data
public class PaymentResponseDTO {

    private Long paymentId;
    private Long serviceRequestId;
    private Double amount;
    private String method;
    private String status;
}