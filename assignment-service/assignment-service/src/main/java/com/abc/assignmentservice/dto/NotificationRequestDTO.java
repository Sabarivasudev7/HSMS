package com.abc.assignmentservice.dto;

import com.abc.assignmentservice.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {
    private Long userId;
    private NotificationType type;
    private String message;
}
