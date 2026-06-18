package cap_stone_project.assignment_service.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class BookingUpdateDTO {

	@NotBlank(message = "Address con not be Blank")
    private String address;

    @Future
    private LocalDateTime scheduleDateTime;
    
}