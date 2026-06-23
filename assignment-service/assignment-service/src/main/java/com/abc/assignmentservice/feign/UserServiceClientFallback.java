package com.abc.assignmentservice.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.abc.assignmentservice.dto.TechnicianDetailResponseDTO;
import com.abc.assignmentservice.enums.Roles;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserServiceClientFallback implements UserServiceClient {

    @Override
    public ResponseEntity<TechnicianDetailResponseDTO> getTechnicianById(Long userId, Roles role, String loggedInUserId) {
        log.error("user-service is unavailable. Could not fetch technician with id: {}", userId);
        return ResponseEntity.ok(null);
    }
}
