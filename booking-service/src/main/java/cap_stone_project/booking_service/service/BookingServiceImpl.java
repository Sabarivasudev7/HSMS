package cap_stone_project.booking_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cap_stone_project.booking_service.Enum.BookingStatus;
import cap_stone_project.booking_service.entity.Booking;
import cap_stone_project.booking_service.exception.BookingNotFoundException;
import cap_stone_project.booking_service.exception.InvalidBookingStateException;
import cap_stone_project.booking_service.exception.ServiceUnavailableException;
import cap_stone_project.booking_service.feignclient.CategoryFeignClient;
import cap_stone_project.booking_service.feignclient.CustomerFeignClient;
import cap_stone_project.booking_service.model.BookingRequestDTO;
import cap_stone_project.booking_service.model.BookingResponseDTO;
import cap_stone_project.booking_service.model.BookingUpdateDTO;
import cap_stone_project.booking_service.model.CategoryResponseDTO;
import cap_stone_project.booking_service.model.CustomerResponseDTO;
import cap_stone_project.booking_service.repository.BookingRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class BookingServiceImpl implements BookingService{
	
	@Autowired
	private BookingRepository bookingRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CategoryFeignClient cateFeignClient;
	
	@Autowired
	private CustomerFeignClient customerFeignClient;
	
	
	
	// Create Booking 
	@Override
	@CircuitBreaker(name = "bookingService", fallbackMethod = "createBookingFallback")
	public BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO) {
		CategoryResponseDTO cate = cateFeignClient.getCategoryById(bookingRequestDTO.getCategoryId());
		if (cate == null) {
			throw new BookingNotFoundException("Category "+ bookingRequestDTO.getCategoryId() +" not found.");
		}
		if (!cate.isActive()) {
			throw new BookingNotFoundException("This "+bookingRequestDTO.getCategoryId()+" Category is not Activated.");
		}
		
		CustomerResponseDTO customer = customerFeignClient.findCustomerById(bookingRequestDTO.getCustomerId());
		if (customer == null) {
			throw new BookingNotFoundException("Customer not found with this id "+bookingRequestDTO.getCustomerId());
		}
			
		Booking booking = new Booking();

		booking.setCustomerId(bookingRequestDTO.getCustomerId());
		booking.setCategoryId(bookingRequestDTO.getCategoryId());
		booking.setServiceType(bookingRequestDTO.getServiceType());
		booking.setAddress(bookingRequestDTO.getAddress());
		booking.setScheduleDateTime(bookingRequestDTO.getScheduleDateTime());

		booking.setStatus(BookingStatus.CREATED);
		booking.setCreatedAt(LocalDateTime.now());
		
		Booking saveBooking = bookingRepo.save(booking);
		
		return modelMapper.map(saveBooking, BookingResponseDTO.class );
	}
	
	public BookingResponseDTO createBookingFallback(BookingRequestDTO bookingRequestDTO, Exception ex) {
		throw new ServiceUnavailableException("Category Service or Customer Service is currently unavailable. Please try again later.");
//		 ex.printStackTrace();
//	    throw new RuntimeException("REAL ERROR : " + ex.getMessage());
	}
	

	
	
	
	// Find By Id 
	@Override
	public BookingResponseDTO findBookingById(Long requestId) {
		Booking booking = bookingRepo.findById(requestId).orElseThrow(() -> new BookingNotFoundException("Booking "+ requestId +" not found"));
		return modelMapper.map(booking, BookingResponseDTO.class);
	}

	
	
	
	// Update Booking
	@Override
	public BookingResponseDTO updateBooking(Long requestId, BookingUpdateDTO bookingUpdateDTO) {
		Booking booking = bookingRepo.findById(requestId).orElseThrow(() -> new BookingNotFoundException("Bookin "+requestId+" not found"));
		if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.IN_PROGRESS) {
		    throw new InvalidBookingStateException( "Now Booking cannot be modified");
		}
		booking.setAddress(bookingUpdateDTO.getAddress());
		booking.setScheduleDateTime(bookingUpdateDTO.getScheduleDateTime());

		Booking updateBooking = bookingRepo.save(booking);
		
		return modelMapper.map(updateBooking, BookingResponseDTO.class);
	}

	
	
	
	// Cancel Booking
	@Override
	public String cancelBooking(Long requestId) {
		Booking booking = bookingRepo.findById(requestId).orElseThrow(() -> new BookingNotFoundException("Bookin "+requestId+" not found"));
		if (booking.getStatus() != BookingStatus.CREATED
			&& (booking.getStatus() != BookingStatus.ASSIGNED)) {
			throw new InvalidBookingStateException("Only CREATED or ASSIGNED bookings can be cancelled");
		}
		booking.setStatus(BookingStatus.CANCELLED);
		bookingRepo.save(booking);
		
		return "Booking Cancelled Successfully";
	}

	
	
	
	// Show All Bookings
	@Override
	public List<BookingResponseDTO> showAllBookings() {
		List<Booking> bookingList = bookingRepo.findAllByOrderByRequestIdDesc();
		return bookingList.stream().map(b -> modelMapper.map(b, BookingResponseDTO.class)).toList();
	}



	// Find Booking Service By Customer Id
	@Override
	public Page<BookingResponseDTO> getBookingsByCustomer(Long customerId, Pageable pageable) {
		return bookingRepo.findByCustomerId(customerId, pageable).map(b -> modelMapper.map(b,  BookingResponseDTO.class));
	}



	// Updating Booking Status used by FeignClient
	@Override
	public BookingResponseDTO updateBookingStatus(Long requestId, BookingStatus status) {
		Booking booking = bookingRepo.findById(requestId).orElseThrow(() -> new BookingNotFoundException("Booking " + requestId + " not found"));
		booking.setStatus(status);
		Booking updateBooking = bookingRepo.save(booking);
		return modelMapper.map(updateBooking, BookingResponseDTO.class);
	}




	@Override
	public String markAsAssigned(Long requestId) {
		Booking booking = bookingRepo.findById(requestId).orElseThrow(() -> new BookingNotFoundException("Booking not found"));
		booking.setStatus(BookingStatus.ASSIGNED);
		bookingRepo.save(booking);
		return "Booking Assigned Successfully";
	}



	
	
}
