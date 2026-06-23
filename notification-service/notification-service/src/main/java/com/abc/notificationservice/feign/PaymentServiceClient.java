package com.abc.notificationservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "payment-service")
public interface PaymentServiceClient {
    @GetMapping("/api/payments/{paymentId}")
    ResponseEntity<Object> getPaymentById(@PathVariable Long paymentId);
}
