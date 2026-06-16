package cap_stone_project.booking_service.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class CategoryResponseDTO {
	
	private Long categoryId;
	
	private String categoryName;

    private String description;

    private double basePrice;
    
    private boolean isActive;

}
