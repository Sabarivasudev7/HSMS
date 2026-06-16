package cap_stone_project.booking_service.controller;

import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cap_stone_project.booking_service.Enum.BookingStatus;
import cap_stone_project.booking_service.entity.Booking;
import cap_stone_project.booking_service.exception.BookingNotFoundException;
import cap_stone_project.booking_service.exception.InvalidBookingStateException;
import cap_stone_project.booking_service.model.BookingRequestDTO;
import cap_stone_project.booking_service.model.BookingResponseDTO;
import cap_stone_project.booking_service.model.BookingUpdateDTO;
import cap_stone_project.booking_service.service.BookingService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/booking")
public class BookingController {
	
	@Autowired
	private BookingService bookingService;
	
	
	// Create Booking
	@PostMapping("/save")
	public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO requestDTO) {
		return ResponseEntity.status(HttpStatus.SC_CREATED).body(bookingService.createBooking(requestDTO));
		
	}
	
	
	
	// Find Booking By Id
	@GetMapping("/{requestId}")
	public ResponseEntity<BookingResponseDTO> findBookingById(@PathVariable Long requestId) {
		return ResponseEntity.status(HttpStatus.SC_OK).body(bookingService.findBookingById(requestId));
	}
	
	
	

    // Update Booking
    @PutMapping("/update/{requestId}")
    public ResponseEntity<BookingResponseDTO> updateBooking(@PathVariable Long requestId, @Valid @RequestBody BookingUpdateDTO bookingUpdateDTO) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(bookingService.updateBooking(requestId, bookingUpdateDTO));
    }

    
    
    
    // Cancel Booking
    @PutMapping("/cancel/{requestId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long requestId) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(bookingService.cancelBooking(requestId));
    }

    
    
    
    // Show All Bookings
    @GetMapping("/all")
    public ResponseEntity<List<BookingResponseDTO>> showAllBookings() {

        return ResponseEntity.status(HttpStatus.SC_OK).body(bookingService.showAllBookings());
    }
    
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<BookingResponseDTO>> getCustomerBookings(@PathVariable Long customerId, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(bookingService.getBookingsByCustomer(customerId, pageable));
    }
    
    
    
    // Technician can accept assigned jobs
 	// Status updated to ACCEPTED
    @PutMapping("/accept/{requestId}")
    public ResponseEntity<String> acceptBooking(@PathVariable Long requestId) {
        return ResponseEntity.ok(bookingService.acceptBooking(requestId));
    }
 	



 	// Technician can reject assigned jobs
 	// Status updated to REJECTED
    @PutMapping("/reject/{requestId}")
    public ResponseEntity<String> rejectBooking(@PathVariable Long requestId) {
        return ResponseEntity.ok(bookingService.rejectBooking(requestId));
    }



 	// Only ACCEPTED requests can be started
 	// Status changes to IN_PROGRESS
    @PutMapping("/start/{requestId}")
    public ResponseEntity<String> startService(@PathVariable Long requestId) {
        return ResponseEntity.ok(bookingService.startService(requestId));
    }



 	// Only IN_PROGRESS requests can be completed
 	// Status changes to COMPLETED
    @PutMapping("/complete/{requestId}")
    public ResponseEntity<String> completeService(@PathVariable Long requestId) {
        return ResponseEntity.ok(bookingService.completeService(requestId));
    }


    
    
}
