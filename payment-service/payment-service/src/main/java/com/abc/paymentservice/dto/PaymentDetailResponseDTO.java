package com.abc.paymentservice.dto;

import com.abc.paymentservice.enums.PaymentMethod;
import com.abc.paymentservice.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailResponseDTO {

    private Long id;
    private Long serviceRequestId;
    private Double amount;
    private PaymentMethod method;
    private PaymentStatus status;

}
