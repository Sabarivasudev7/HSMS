package cap_stone_project.booking_service.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cap_stone_project.booking_service.model.BookingResponseDTO;

@FeignClient(name = "BOOKING-SERVICE")
public interface BookingFeignClient {

    @GetMapping("/api/booking/{requestId}")
    BookingResponseDTO getBookingById(@PathVariable Long requestId);
    
}