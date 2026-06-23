package com.abc.notificationservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.abc.notificationservice.dto.UserResponseDTO;

@FeignClient(name = "user-service", fallback = UserServiceClientFallback.class)
public interface UserServiceClient {

    @GetMapping("/api/users/{userId}")
    ResponseEntity<UserResponseDTO> getUserProfile(
            @PathVariable("userId") Long userId,
            @RequestHeader("X-Role") String role);
}
