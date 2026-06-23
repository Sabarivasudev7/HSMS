package com.abc.assignmentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.abc.assignmentservice.dto.NotificationRequestDTO;

@FeignClient(name = "notification-service", fallback = NotificationServiceClientFallback.class)
public interface NotificationServiceClient {

    @PostMapping("/api/notifications")
    ResponseEntity<Object> sendNotification(@RequestBody NotificationRequestDTO requestDTO);
}
