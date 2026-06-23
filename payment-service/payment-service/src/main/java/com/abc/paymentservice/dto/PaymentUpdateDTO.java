package com.abc.paymentservice.dto;

import com.abc.paymentservice.enums.PaymentStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUpdateDTO {

    @NotNull(message = "Payment status is required")
    private PaymentStatus status;
}
