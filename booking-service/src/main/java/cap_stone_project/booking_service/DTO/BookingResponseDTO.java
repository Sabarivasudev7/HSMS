package cap_stone_project.booking_service.DTO;

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

    private String serviceType;

    private String status;
}