package com.abc.categoryservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(min = 3, max = 50, message = "Category name must be between 3 and 50 characters")
    private String categoryName;

    @NotBlank(message = "Description is required")
    @Size(max = 100, message = "Description cannot exceed 100 characters")
    private String description;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be greater than 0")
    private Double basePrice;

    @NotNull(message = "Active status is required")
    private Boolean active;

    // Backward compatibility constructor for legacy tests and callers
    public CategoryRequest(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
        this.basePrice = 100.0; // Default test price
        this.active = true;    // Default active status
    }

    // Backward compatibility methods for legacy mapping/tests expecting name
    public String getName() {
        return this.categoryName;
    }

    public void setName(String name) {
        this.categoryName = name;
    }
}