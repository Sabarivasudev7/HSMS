package cap_stone_project.assignment_service.model;

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
    private String status;
}
