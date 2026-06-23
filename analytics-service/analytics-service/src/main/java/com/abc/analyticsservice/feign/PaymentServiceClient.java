package com.abc.analyticsservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "payment-service",
        url = "http://localhost:8087"
)
public interface PaymentServiceClient {

    @GetMapping("/api/payments")
    Object getAllPayments();

}