package com.paymentservice.dto;

import lombok.Data;

@Data
public class PaymentDetailResponseDTO {

    private Long paymentId;
    private Long serviceRequestId;
    private Double amount;
    private String method;
    private String status;
}