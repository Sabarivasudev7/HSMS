package com.abc.notificationservice.service;

import java.util.List;

import com.abc.notificationservice.dto.NotificationRequestDTO;
import com.abc.notificationservice.dto.NotificationResponseDTO;

public interface NotificationService {
    NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO);
    List<NotificationResponseDTO> getNotificationsByUser(Long userId);
    List<NotificationResponseDTO> getAllNotifications();
    void deleteNotification(Long notificationId);
}
