package com.hsms.notificationservice.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentNotificationDTO {

    private Long paymentId;

    private Long bookingId;

    private BigDecimal amount;

    private String paymentMethod;

    private String paymentStatus;

}