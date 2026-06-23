package com.abc.paymentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@FeignClient(name = "notification-service", fallback = NotificationServiceClient.NotificationServiceClientFallback.class)
public interface NotificationServiceClient {

    @PostMapping("/api/notifications")
    ResponseEntity<?> sendNotification(@RequestBody NotificationRequest request);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class NotificationRequest {
        private Long userId;
        private String type;
        private String message;
    }

    @Component
    class NotificationServiceClientFallback implements NotificationServiceClient {
        @Override
        public ResponseEntity<?> sendNotification(NotificationRequest request) {
            return ResponseEntity.accepted().build();
        }
    }
}
