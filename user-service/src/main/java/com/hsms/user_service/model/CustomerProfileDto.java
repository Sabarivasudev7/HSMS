package com.hsms.user_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CustomerProfileDto {

   
    private Long customerId;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[7-9][0-9]{9}$", message = "Phone number must be 10 digits starting with 7, 8, or 9")
    private String phone;

    @NotBlank(message = "Address is required")
    @Size(max = 100, message = "Address cannot exceed 100 characters")
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 30, message = "City cannot exceed 30 characters")
    private String city;
    
    @NotBlank(message = "Pincode is required")
    @Size(max = 6, message = "PinCode cannot exceed 6 characters")
    private String pincode;
}