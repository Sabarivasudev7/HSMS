package com.hsms.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserRegistrationDto {

    private Long userId;
    
    private String name;

    private String email;

    private String role;
}