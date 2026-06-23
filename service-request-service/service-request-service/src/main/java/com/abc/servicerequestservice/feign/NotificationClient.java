package com.abc.servicerequestservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.Data;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/api/notifications")
    ResponseEntity<?> sendNotification(@RequestBody NotificationRequest request);

    @Data
    class NotificationRequest {
        private Long userId;
        private String type;
        private String message;
    }
}
