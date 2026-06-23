package com.abc.assignmentservice.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.abc.assignmentservice.dto.NotificationRequestDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NotificationServiceClientFallback implements NotificationServiceClient {

    @Override
    public ResponseEntity<Object> sendNotification(NotificationRequestDTO requestDTO) {
        log.error("Failed to send notification to user {}: service offline.", requestDTO.getUserId());
        return ResponseEntity.ok(new Object());
    }
}
