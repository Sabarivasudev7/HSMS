package cap_stone_project.booking_service.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import cap_stone_project.booking_service.model.BookingResponseDTO;
import cap_stone_project.booking_service.service.BookingService;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {
	
	@Autowired
	private MockMvc mocMvc;
	
	@MockitoBean
	private BookingService bookingService;
	
	
	// Find Booking By Id
	@Test
	void testFindBookingById() throws Exception {
		BookingResponseDTO dto = new BookingResponseDTO();
		dto.setRequestId(1L);
		dto.setServiceType("Plumbing");
		
		when(bookingService.findBookingById(1L)).thenReturn(dto);
		mocMvc.perform(get("/api/booking/1")).andExpect(status().isOk());
		
	}

}
