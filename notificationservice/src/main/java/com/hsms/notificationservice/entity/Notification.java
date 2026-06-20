package com.hsms.notificationservice.entity;

import java.time.LocalDateTime;

import com.hsms.notificationservice.enums.NotificationStatus;
import com.hsms.notificationservice.enums.NotificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "notification_tbl")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long notificationId;

    private Long serviceRequestId;

    private String recipientEmail;

    private String subject;

    @Column(length = 2000)
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private LocalDateTime sentAt;
}