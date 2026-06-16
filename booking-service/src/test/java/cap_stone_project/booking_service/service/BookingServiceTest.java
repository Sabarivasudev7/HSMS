package cap_stone_project.booking_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import cap_stone_project.booking_service.entity.Booking;
import cap_stone_project.booking_service.exception.BookingNotFoundException;
import cap_stone_project.booking_service.feignclient.CategoryFeignClient;
import cap_stone_project.booking_service.feignclient.CustomerFeignClient;
import cap_stone_project.booking_service.model.BookingRequestDTO;
import cap_stone_project.booking_service.model.BookingResponseDTO;
import cap_stone_project.booking_service.model.CategoryResponseDTO;
import cap_stone_project.booking_service.model.CustomerResponseDTO;
import cap_stone_project.booking_service.repository.BookingRepository;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
	
	@InjectMocks
	private BookingServiceImpl bookingService; 
	
	@Mock
	private BookingRepository bookingRepo;
	
	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private CategoryFeignClient cateFeignClient;

	@Mock
	private CustomerFeignClient customerFeignClient;
	
	
	
	
	// Create Booking
	@Test
	void testCreateBooking() {
		BookingRequestDTO dto = new BookingRequestDTO( 101L, 1L, "Plumbing", "Hyd", LocalDateTime.now().plusDays(1));
		CategoryResponseDTO category = new CategoryResponseDTO(1L,"Plumbing","Pipe Work", 500, true);
		CustomerResponseDTO customer = new CustomerResponseDTO(101L, "Ali", "9999999999", "Hyd", "Hyd");

		Booking booking = new Booking();
		BookingResponseDTO response = new BookingResponseDTO();
		
		when(cateFeignClient.getCategoryById(1L)).thenReturn(category);
		when(customerFeignClient.findCustomerById(101L)).thenReturn(customer);
		
		when(modelMapper.map(dto, Booking.class)).thenReturn(booking);
		when(bookingRepo.save(booking)).thenReturn(booking);
		when(modelMapper.map(booking, BookingResponseDTO.class)).thenReturn(response);
		
		BookingResponseDTO result = bookingService.createBooking(dto);
		assertNotNull(result);
	}
	
	
	
	// Find Booking By Id
	@Test
	void testFindBookingById() {
		Booking booking = new Booking();
		BookingResponseDTO response = new BookingResponseDTO();
		
		when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));
		when(modelMapper.map(booking, BookingResponseDTO.class)).thenReturn(response);
		
		BookingResponseDTO result = bookingService.findBookingById(1L);
		assertNotNull(result);
	}
	
	
	
	
	// Find Booking By Id Exception
	@Test
	void testFindBookingByIdException() {
		when(bookingRepo.findById(100L)).thenReturn(Optional.empty());
		assertThrows(BookingNotFoundException.class, () -> bookingService.findBookingById(100L));
		
	}
	
	
	
	
	// Show All Bookings
	@Test
	void testShowAllBookings() {
		Booking booking = new Booking();
		BookingResponseDTO dto = new BookingResponseDTO();
		
		when(bookingRepo.findAll()).thenReturn(Arrays.asList(booking));
		when(modelMapper.map(booking, BookingResponseDTO.class)).thenReturn(dto);
		
		assertEquals(1, bookingService.showAllBookings().size());
	}

}
