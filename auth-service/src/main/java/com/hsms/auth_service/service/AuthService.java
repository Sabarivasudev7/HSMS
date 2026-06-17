package com.hsms.auth_service.service;

import com.hsms.auth_service.model.AuthResponse;
import com.hsms.auth_service.model.LoginRequest;

import com.hsms.auth_service.model.RegisterRequest;

public interface AuthService {

    String register(RegisterRequest request);   
    AuthResponse login(LoginRequest request);
}