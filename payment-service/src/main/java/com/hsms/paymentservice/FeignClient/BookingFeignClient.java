package com.hsms.paymentservice.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hsms.paymentservice.dto.BookingResponseDTO;

@FeignClient(name="SERVIEC-REQUEST-SERVICE")
public interface BookingFeignClient {
	
	
	@GetMapping("/api/bookings/{requestId}")
	BookingResponseDTO getBookingById(@PathVariable Long requestId);
	
}