package com.hsms.auth_service.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.hsms.auth_service.entity.AuthUser;
import com.hsms.auth_service.enums.Role;
import com.hsms.auth_service.exception.UserAlreadyExistsException;
import com.hsms.auth_service.model.RegisterRequest;
import com.hsms.auth_service.repository.UserRepository;
import com.hsms.auth_service.security.JwtUtil;
import com.hsms.auth_service.model.UserRegistrationDto;
import com.hsms.auth_service.exception.UserNotFoundException;
import com.hsms.auth_service.model.AuthResponse;
import com.hsms.auth_service.model.LoginRequest;
import com.hsms.auth_service.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserServiceFeignClient userServiceFeignClient;

    @Override
    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        
        Role role = (request.getRole() == null) ? Role.CUSTOMER : request.getRole();

        AuthUser user = new AuthUser();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setEnabled(true);

        AuthUser savedUser = userRepository.save(user);
        Long userId = savedUser.getId();

        
        sendUserToUserService(userId, savedUser.getName(), savedUser.getEmail(), savedUser.getRole().name());

        return "User Registered Successfully";
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        AuthUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid Credentials");
        }

        UserDetails userDetails = User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();

      
        String token = jwtUtil.generateToken(userDetails, user.getId(), user.getRole().name());

        return new AuthResponse(token, user.getRole().name(), user.getName());
    }

    private void sendUserToUserService(Long userId, String name, String email, String role) {
        UserRegistrationDto dto = new UserRegistrationDto(userId, name, email, role);
        
        userServiceFeignClient.createUser(dto, role);
    }
}
