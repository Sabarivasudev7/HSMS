package com.abc.categoryservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private Long categoryId;
    private String categoryName;
    private String description;
    private Double basePrice;
    private Boolean active;

    // Backward compatibility constructor for legacy tests and callers
    public CategoryResponse(Long categoryId, String categoryName, String description) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.basePrice = 100.0; // Default test price
        this.active = true;
    }

    // Backward compatibility methods for legacy clients expecting id/name
    public Long getId() {
        return this.categoryId;
    }

    public void setId(Long id) {
        this.categoryId = id;
    }

    public String getName() {
        return this.categoryName;
    }

    public void setName(String name) {
        this.categoryName = name;
    }
}