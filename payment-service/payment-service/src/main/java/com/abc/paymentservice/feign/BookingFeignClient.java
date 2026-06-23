package com.abc.paymentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.abc.paymentservice.dto.BookingResponseDTO;

@FeignClient(name = "service-request-service", fallback = BookingFeignClient.BookingFeignClientFallback.class)
public interface BookingFeignClient {

	@GetMapping("/api/service-requests/{requestId}")
	BookingResponseDTO getBookingById(
		@PathVariable("requestId") Long requestId,
		@RequestHeader("X-Role") String role
	);

	@Component
	class BookingFeignClientFallback implements BookingFeignClient {
		@Override
		public BookingResponseDTO getBookingById(Long requestId, String role) {
			return null;
		}
	}
}
