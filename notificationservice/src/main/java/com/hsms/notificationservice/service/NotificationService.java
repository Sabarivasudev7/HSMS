package com.hsms.notificationservice.service;

import java.util.List;

import com.hsms.notificationservice.dto.NotificationRequestDTO;
import com.hsms.notificationservice.dto.NotificationResponseDTO;

public interface NotificationService {

	
	
    NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO);

    NotificationResponseDTO getNotificationById(Long notificationId);

    List<NotificationResponseDTO> getAllNotifications();

    String deleteNotification(Long notificationId);
}