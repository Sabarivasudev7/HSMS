package com.abc.notificationservice.dto;

import java.time.LocalDateTime;

import com.abc.notificationservice.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private Long userId;
    private NotificationType type;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
}