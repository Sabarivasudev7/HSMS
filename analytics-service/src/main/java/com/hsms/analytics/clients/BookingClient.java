package com.hsms.analytics.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.hsms.analytics.model.BookingResponseDto;

@FeignClient(name = "BOOKING-SERVICE", contextId = "bookingClient")
public interface BookingClient {
    @GetMapping("/api/bookings/all")
    List<BookingResponseDto> getAllBookings();
}
