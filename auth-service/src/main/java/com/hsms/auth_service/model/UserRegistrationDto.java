package com.hsms.auth_service.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {

    private Long userId;
    private String email;
    private String role;
}