package com.feedback_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.feedback_service.dto.BookingResponseDTO;

@FeignClient(
        name = "BOOKING-SERVICE",
        contextId = "bookingClient"
)
public interface BookingFeignClient {

    @GetMapping("/api/booking/{requestId}")
    BookingResponseDTO getBookingById(
            @PathVariable("requestId") Long requestId);
}