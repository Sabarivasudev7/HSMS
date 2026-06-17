package com.hsms.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hsms.auth_service.model.RegisterRequest;
import com.hsms.auth_service.service.AuthService;
import com.hsms.auth_service.model.AuthResponse;
import com.hsms.auth_service.model.LoginRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
       
        String response = authService.register(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
      
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    
}
