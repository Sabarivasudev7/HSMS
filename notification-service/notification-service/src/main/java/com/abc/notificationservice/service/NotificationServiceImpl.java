package com.abc.notificationservice.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.abc.notificationservice.dto.NotificationRequestDTO;
import com.abc.notificationservice.dto.NotificationResponseDTO;
import com.abc.notificationservice.dto.UserResponseDTO;
import com.abc.notificationservice.entity.Notification;
import com.abc.notificationservice.exception.NotificationNotFoundException;
import com.abc.notificationservice.feign.UserServiceClient;
import com.abc.notificationservice.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;
    private final JavaMailSender mailSender;
    private final UserServiceClient userServiceClient;

    @Override
    public NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO) {
        Notification notification = modelMapper.map(requestDTO, Notification.class);
        Notification saved = notificationRepository.save(notification);

        ResponseEntity<UserResponseDTO> response = userServiceClient.getUserProfile(requestDTO.getUserId(), "ADMIN");
        UserResponseDTO user = response.getBody();
        String recipientEmail = user == null ? "user" + requestDTO.getUserId() + "@example.com" : user.getEmail();
        String recipientName = user == null ? "User" : user.getName();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject("HSMS Notification: " + requestDTO.getType());
        mailMessage.setText("Dear " + recipientName + ",\n\n" + requestDTO.getMessage()
                + "\n\nBest regards,\nHome Service Management System");
        mailSender.send(mailMessage);

        log.info("[SMS NOTIFICATION] userId={}, message={}", requestDTO.getUserId(), requestDTO.getMessage());
        log.info("[PUSH NOTIFICATION] userId={}, message={}", requestDTO.getUserId(), requestDTO.getMessage());

        return modelMapper.map(saved, NotificationResponseDTO.class);
    }

    @Override
    public List<NotificationResponseDTO> getNotificationsByUser(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        if (notifications.isEmpty()) {
            throw new NotificationNotFoundException("No notifications found for user: " + userId);
        }
        return notifications.stream()
                .map(notification -> modelMapper.map(notification, NotificationResponseDTO.class))
                .toList();
    }

    @Override
    public List<NotificationResponseDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(notification -> modelMapper.map(notification, NotificationResponseDTO.class))
                .toList();
    }

    @Override
    public void deleteNotification(Long notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new NotificationNotFoundException("Notification not found: " + notificationId);
        }
        notificationRepository.deleteById(notificationId);
    }
}
