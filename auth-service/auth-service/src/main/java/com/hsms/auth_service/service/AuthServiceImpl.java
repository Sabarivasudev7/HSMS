package com.hsms.auth_service.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hsms.auth_service.entity.AuthUser;
import com.hsms.auth_service.enums.Role;
import com.hsms.auth_service.exception.InvalidCredentialsException;
import com.hsms.auth_service.exception.UserAlreadyExistsException;
import com.hsms.auth_service.exception.UserNotFoundException;
import com.hsms.auth_service.model.AuthResponse;
import com.hsms.auth_service.model.LoginRequest;
import com.hsms.auth_service.model.RegisterRequest;
import com.hsms.auth_service.model.RegisterResponse;
import com.hsms.auth_service.model.UserRegistrationDto;
import com.hsms.auth_service.repository.UserRepository;
import com.hsms.auth_service.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserServiceFeignClient userServiceFeignClient;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        AuthUser user = new AuthUser();
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() == null ? Role.CUSTOMER : request.getRole());
        user.setEnabled(true);

        AuthUser savedUser = userRepository.save(user);
        sendUserToUserService(savedUser);

        return new RegisterResponse(savedUser.getId(), "User registered successfully");
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        AuthUser user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        UserDetails userDetails = User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();

        String token = jwtUtil.generateToken(userDetails, user.getId(), user.getRole().name());
        return new AuthResponse(token, user.getId(), user.getRole().name(), user.getName(), "WELCOME " + user.getName());
    }

    @Override
    public UserRegistrationDto getUserById(Long userId) {
        AuthUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return toUserRegistrationDto(user);
    }

    @Override
    public UserRegistrationDto getUserByEmail(String email) {
        AuthUser user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return toUserRegistrationDto(user);
    }

    private void sendUserToUserService(AuthUser savedUser) {
        UserRegistrationDto dto = toUserRegistrationDto(savedUser);
        userServiceFeignClient.createUser(dto, "INTERNAL");
    }

    private UserRegistrationDto toUserRegistrationDto(AuthUser user) {
        return new UserRegistrationDto(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }
}
