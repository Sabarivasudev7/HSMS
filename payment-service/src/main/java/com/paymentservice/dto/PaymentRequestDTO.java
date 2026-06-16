package com.paymentservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequestDTO {

    @NotNull
    private Long serviceRequestId;

    @NotNull
    private Double amount;

    @NotNull
    private String method;
}