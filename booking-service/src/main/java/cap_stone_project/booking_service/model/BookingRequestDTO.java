package cap_stone_project.booking_service.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class BookingRequestDTO {

	
	@NotNull(message = "Customer Id is required")
	private Long customerId;
	
	@NotNull(message = "Category Id is required")
	private Long categoryId;
	
	@NotBlank(message = "Service Type cannot be blank")
	private String serviceType;
	
	@NotBlank(message = "Address cannot be blank")
	private String address;
	
	@Future(message = "Schedule Date must be in the future")
	private LocalDateTime scheduleDateTime;


}
