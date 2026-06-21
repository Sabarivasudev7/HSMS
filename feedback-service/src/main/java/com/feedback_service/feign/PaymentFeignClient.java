package com.feedback_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.feedback_service.dto.PaymentResponseDTO;

@FeignClient(
        name = "PAYMENT-SERVICE",
        contextId = "paymentClient"
)
public interface PaymentFeignClient {

    @GetMapping("/api/payments/request/{requestId}")
    PaymentResponseDTO getPaymentByRequestId(
            @PathVariable("requestId") Long requestId);
}