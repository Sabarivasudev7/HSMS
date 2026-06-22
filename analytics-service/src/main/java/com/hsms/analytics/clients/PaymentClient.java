package com.hsms.analytics.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.hsms.analytics.model.PaymentResponseDto;

@FeignClient(name = "PAYMENT-SERVICE", contextId = "paymentClient")
public interface PaymentClient {
	@GetMapping("/api/payments/all")
	List<PaymentResponseDto> getAllPayments();
}