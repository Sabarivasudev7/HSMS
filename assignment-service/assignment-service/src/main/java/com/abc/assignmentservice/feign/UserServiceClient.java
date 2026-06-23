package com.abc.assignmentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.abc.assignmentservice.dto.TechnicianDetailResponseDTO;
import com.abc.assignmentservice.enums.Roles;

@FeignClient(name = "user-service", fallback = UserServiceClientFallback.class)
public interface UserServiceClient {

    @GetMapping("/api/technicians/{userId}")
    ResponseEntity<TechnicianDetailResponseDTO> getTechnicianById(
        @PathVariable("userId") Long userId,
        @RequestHeader("X-Role") Roles role,
        @RequestHeader("X-User-Id") String loggedInUserId
    );
}