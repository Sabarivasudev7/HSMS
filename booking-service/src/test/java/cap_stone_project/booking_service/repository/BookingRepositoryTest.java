package cap_stone_project.booking_service.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import cap_stone_project.booking_service.entity.Booking;

@DataJpaTest
public class BookingRepositoryTest {
	
	@Autowired
	private BookingRepository bookingRepo;
	
	
	// Find By Customer Id
	@Test
	void testFindByCustomerId() {
		Booking booking = new Booking(null, "Plumbing", "CREATED", "Hyd", 
				LocalDateTime.now().plusDays(1), LocalDateTime.now(), 1L, 101L);
		bookingRepo.save(booking);
		
		List<Booking> result = bookingRepo.findByCustomerId(101L);
		assertEquals(1, result.size());
	}
	
	
	
	// Find By Status
	@Test
	void testFindByStatus() {
		Booking booking = new Booking(null, "Cleaning", "CREATED", "Vijayawada", 
				LocalDateTime.now().plusDays(1), LocalDateTime.now(), 2L, 102L);
		bookingRepo.save(booking);
		List<Booking> result = bookingRepo.findByStatus("CREATED");
		assertEquals(1, result.size());
	}
	
	
	
	
	// Find By Id
	@Test
	void testFindById() {
		Booking booking = new Booking(null, "Electrical", "CREATED", "Kakinada", 
				LocalDateTime.now().plusDays(1), LocalDateTime.now(), 3L, 132L);
		Booking saved = bookingRepo.save(booking);
		Optional<Booking> result = bookingRepo.findById(saved.getRequestId());
		assertTrue(result.isPresent());
	}
	
	
	
	

}
