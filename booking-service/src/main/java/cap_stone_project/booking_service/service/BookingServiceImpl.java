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
import cap_stone_project.booking_service.feignclient.CategoryFeignClient;
import cap_stone_project.booking_service.feignclient.CustomerFeignClient;
import cap_stone_project.booking_service.model.BookingRequestDTO;
import cap_stone_project.booking_service.model.BookingResponseDTO;
import cap_stone_project.booking_service.model.BookingUpdateDTO;
import cap_stone_project.booking_service.model.CategoryResponseDTO;
import cap_stone_project.booking_service.model.CustomerResponseDTO;
import cap_stone_project.booking_service.repository.BookingRepository;

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
	public BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO) {
		CategoryResponseDTO cate = cateFeignClient.getCategoryById(bookingRequestDTO.getCategoryId());
		if (cate == null || !cate.isActive()) {
			throw new BookingNotFoundException("Category "+ bookingRequestDTO.getCategoryId() +" not found.");
		}
		if (cate.isActive() == false) {
			throw new BookingNotFoundException("This "+bookingRequestDTO.getCategoryId()+" Category is not Activated.");
		}
		
		CustomerResponseDTO customer = customerFeignClient.findCustomerById(bookingRequestDTO.getCustomerId());
		if (customer == null) {
			throw new BookingNotFoundException("Customer not found with this id "+bookingRequestDTO.getCustomerId());
		}
			
		Booking booking = modelMapper.map(bookingRequestDTO, Booking.class);
		
		booking.setStatus(BookingStatus.CREATED);
		booking.setCreatedAt(LocalDateTime.now());
		
		Booking saveBooking = bookingRepo.save(booking);
		
		return modelMapper.map(saveBooking, BookingResponseDTO.class );
	}

	
	
	
	// Find By Id 
	@Override
	public BookingResponseDTO findBookingById(Long requestId) {
		Booking booking = bookingRepo.findById(requestId).orElseThrow(() -> 
							new BookingNotFoundException("Booking "+ requestId +" not found"));
		return modelMapper.map(booking, BookingResponseDTO.class);
	}

	
	
	
	// Update Booking
	@Override
	public BookingResponseDTO updateBooking(Long requestId, BookingUpdateDTO bookingUpdateDTO) {
		Booking booking = bookingRepo.findById(requestId).orElseThrow(() -> 
							new BookingNotFoundException("Bookin "+requestId+" not found"));
		if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CANCELLED) {
		    throw new InvalidBookingStateException( "Completed or Cancelled booking cannot be modified");
		}
		
		if (booking.getStatus() == BookingStatus.ACCEPTED || (booking.getStatus() == BookingStatus.IN_PROGRESS)) {
			booking.setAddress(bookingUpdateDTO.getAddress());
			booking.setScheduleDateTime(bookingUpdateDTO.getScheduleDateTime());
		}

		Booking updateBooking = bookingRepo.save(booking);
		
		return modelMapper.map(updateBooking, BookingResponseDTO.class);
	}

	
	
	
	// Cancel Booking
	@Override
	public String cancelBooking(Long requestId) {
		Booking booking = bookingRepo.findById(requestId).orElseThrow(() -> 
							new BookingNotFoundException("Bookin "+requestId+" not found"));
		if (booking.getStatus() == BookingStatus.CREATED
			&& (booking.getStatus() == BookingStatus.ASSIGNED)) {
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
		return bookingList.stream().map(booking -> modelMapper.map(booking, BookingResponseDTO.class)).toList();
	}



	// Find Booking Service By Customer Id
	@Override
	public Page<BookingResponseDTO> getBookingsByCustomer(Long customerId, Pageable pageable) {
		return bookingRepo.findByCustomerId(customerId, pageable).map(booking -> modelMapper.map(booking,  BookingResponseDTO.class));
	}

}
