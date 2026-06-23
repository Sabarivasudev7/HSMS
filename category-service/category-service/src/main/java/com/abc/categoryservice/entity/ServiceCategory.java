package com.abc.categoryservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service_categories")
public class ServiceCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long categoryId;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String categoryName;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String description;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false)
    private Double basePrice;

    @Column(nullable = false)
    private Boolean active;

    // Backward compatibility helper methods for id and name
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