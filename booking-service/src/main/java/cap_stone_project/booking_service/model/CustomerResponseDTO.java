package cap_stone_project.booking_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class CustomerResponseDTO {
	
	private Long customerId;

    private String name;

    private String phone;

    private String address;

    private String city;

}
