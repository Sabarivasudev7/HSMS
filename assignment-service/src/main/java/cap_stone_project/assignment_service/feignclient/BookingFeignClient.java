package cap_stone_project.assignment_service.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cap_stone_project.assignment_service.enums.BookingStatus;
import cap_stone_project.assignment_service.model.BookingResponseDTO;

@FeignClient(name="BOOKING-SERVICE")
public interface BookingFeignClient {

    @GetMapping("/api/booking/{requestId}")
    BookingResponseDTO getBookingById(@PathVariable Long requestId);

    @PutMapping("/api/booking/assign/{requestId}")
    String markAsAssigned(@PathVariable Long requestId);
    
    @PutMapping("/api/booking/status/{requestId}")
    BookingResponseDTO updateBookingStatus(@PathVariable Long requestId, @RequestParam BookingStatus status);
}