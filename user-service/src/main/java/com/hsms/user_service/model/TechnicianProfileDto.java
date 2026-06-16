package com.hsms.user_service.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianProfileDto {

   
    private Long technicianId;

    @NotBlank(message = "Skill is required")
    @Size(min = 2, max = 50, message = "Skill must be between 2 and 50 characters")
    private String skill;

    @NotNull(message = "Experience is required")
    @Min(value = 0, message = "Experience must be at least 0 years")
    @Max(value = 50, message = "Experience cannot exceed 50 years")
    private Integer experience;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[7-9][0-9]{9}$", message = "Phone number must be 10 digits starting with 7, 8, or 9")
    private String phone;

    @NotNull(message = "Availability status is required")
    private Boolean availability;

    @NotNull(message = "Rating is required")
    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private Double rating;
}
