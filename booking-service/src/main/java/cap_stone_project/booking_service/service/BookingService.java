package cap_stone_project.booking_service.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cap_stone_project.booking_service.model.BookingRequestDTO;
import cap_stone_project.booking_service.model.BookingResponseDTO;
import cap_stone_project.booking_service.model.BookingUpdateDTO;

public interface BookingService {

	BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO);

	BookingResponseDTO findBookingById(Long requestId);

	BookingResponseDTO updateBooking(Long requestId, BookingUpdateDTO bookingUpdateDTO);

	String cancelBooking(Long requestId);

	List<BookingResponseDTO> showAllBookings();
	
	Page<BookingResponseDTO> getBookingsByCustomer(Long customerId, Pageable pageable);

	String acceptBooking(Long requestId);
	
	String rejectBooking(Long requestId);
	
	String startService(Long requestId);
	
	String completeService(Long requestId);
	
	String markAsAssigned(Long requestId);

}
