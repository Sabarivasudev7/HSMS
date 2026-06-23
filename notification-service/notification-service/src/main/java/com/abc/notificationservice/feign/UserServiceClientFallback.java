package com.abc.notificationservice.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.abc.notificationservice.dto.UserResponseDTO;

@Component
public class UserServiceClientFallback implements UserServiceClient {

    @Override
    public ResponseEntity<UserResponseDTO> getUserProfile(Long userId, String role) {
        UserResponseDTO fallback = new UserResponseDTO();
        fallback.setUserId(userId);
        fallback.setName("User-" + userId);
        fallback.setEmail("user" + userId + "@fallback.com");
        return ResponseEntity.ok(fallback);
    }
}
