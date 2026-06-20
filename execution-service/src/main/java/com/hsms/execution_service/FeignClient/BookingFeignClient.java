package com.hsms.execution_service.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hsms.execution_service.DTO.BookingResponseDTO;
import com.hsms.execution_service.enums.BookingStatus;

@FeignClient(name = "BOOKING-SERVICE")
public interface BookingFeignClient {
	
	@PutMapping("/api/booking/status/{requestId}")
	BookingResponseDTO updateBookingStatus(@PathVariable Long requestId, @RequestParam BookingStatus status);
	
	
	@GetMapping("/api/booking/{requestId}")
	BookingResponseDTO getBookingById(@PathVariable Long requestId);

}
