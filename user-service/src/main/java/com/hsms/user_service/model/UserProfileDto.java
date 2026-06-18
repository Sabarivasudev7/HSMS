package com.hsms.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserProfileDto {
   
    private Long userId;
    private String name;
    private String email;
    private String role;
    private CustomerProfileDto customerProfile;
    private TechnicianProfileDto technicianProfile;
}
