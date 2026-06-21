package com.hsms.paymentservice.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.hsms.paymentservice.dto.BookingResponseDTO;

@FeignClient(name="BOOKING-SERVICE", contextId = "bookingClient")
public interface BookingFeignClient {
	
	@GetMapping("/api/booking/{requestId}")
	BookingResponseDTO getBookingById(@RequestHeader("Authorization") String token, @PathVariable("requestId") Long requestId);
	
	@PutMapping("/api/booking/status/{requestId}")
	BookingResponseDTO updateBookingStatus(@RequestHeader("Authorization") String token, @PathVariable("requestId") Long requestId, @RequestParam("status") String status);
	
}