package com.hsms.auth_service.service;

import com.hsms.auth_service.model.AuthResponse;
import com.hsms.auth_service.model.LoginRequest;
import com.hsms.auth_service.model.RegisterRequest;
import com.hsms.auth_service.model.RegisterResponse;
import com.hsms.auth_service.model.UserRegistrationDto;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserRegistrationDto getUserById(Long userId);

    UserRegistrationDto getUserByEmail(String email);
}
