package cap_stone_project.caregory_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class CategoryResponseDTO {
	
	
	@NotBlank(message = "Category Name can not be Empty.")
	@Size(max = 30)
	private String categoryName;
	
	@NotBlank(message = "Description can not be Blank.")
	private String description;
	
	@Positive(message = "Enter Positive digits only.")
	private double basePrice;
	
	private boolean isActive;

}
