package cap_stone_project.booking_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class UserResponseDTO {

    private Long userId;

    private String name;

    private String email;
}