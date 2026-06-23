package com.abc.servicerequestservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import lombok.Data;

@FeignClient(name = "user-service", fallback = UserClient.UserClientFallback.class)
public interface UserClient {

    @GetMapping("/api/users/{userId}")
    ResponseEntity<UserDto> getUserProfile(
        @PathVariable("userId") Long userId,
        @RequestHeader("X-Role") String role
    );

    @Data
    class UserDto {
        private Long userId;
        private String name;
        private String email;
        private String role;
    }

    @Component
    class UserClientFallback implements UserClient {
        @Override
        public ResponseEntity<UserDto> getUserProfile(Long userId, String role) {
            return ResponseEntity.ok(null);
        }
    }
}

