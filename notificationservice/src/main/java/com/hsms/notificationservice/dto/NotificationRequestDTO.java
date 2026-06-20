package com.hsms.notificationservice.dto;



import com.hsms.notificationservice.enums.NotificationType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class NotificationRequestDTO {

    @NotNull
    private Long serviceRequestId;

    @Email
    @NotBlank
    private String recipientEmail;

    @NotBlank
    private String subject;

    @NotBlank
    private String message;

    @NotNull
    private NotificationType notificationType;
}