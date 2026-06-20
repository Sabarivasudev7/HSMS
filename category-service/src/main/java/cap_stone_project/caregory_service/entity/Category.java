package cap_stone_project.caregory_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "category_tbl")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long categoryId;
	
	@Column(length = 30, unique = true)	
	private String categoryName;
	
	@Column(length = 50, nullable = false)
	private String description;
	
	private double basePrice;
	
	
	private boolean isActive;
	

}
