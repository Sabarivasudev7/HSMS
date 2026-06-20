package com.hsms.notificationservice.dto;

import java.time.LocalDateTime;

import com.hsms.notificationservice.enums.NotificationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class NotificationResponseDTO {

	private Long notificationId;

    private Long serviceRequestId;

    private String recipientEmail;

    private NotificationStatus status;

    private LocalDateTime sentAt;

}