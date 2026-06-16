package cap_stone_project.booking_service.model;

import java.time.LocalDateTime;

import cap_stone_project.booking_service.Enum.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class BookingResponseDTO {
	

	private Long requestId;
	

	private Long customerId;
	

	private Long categoryId;
	

	private String serviceType;
	
	
	private String address;
	

	private BookingStatus status;
	

	private LocalDateTime createdAt;
	
	
	private Long technicianId;
	
	private String technicianName;
	
}
