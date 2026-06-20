package com.hsms.notificationservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.hsms.notificationservice.dto.NotificationRequestDTO;
import com.hsms.notificationservice.dto.NotificationResponseDTO;
import com.hsms.notificationservice.entity.Notification;
import com.hsms.notificationservice.enums.NotificationStatus;
import com.hsms.notificationservice.exception.NotificationFailedException;
import com.hsms.notificationservice.exception.NotificationNotFoundException;
import com.hsms.notificationservice.repository.NotificationRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationRepository notificationRepo;

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO) {

		Notification notification = new Notification();

		notification.setServiceRequestId(requestDTO.getServiceRequestId());
		notification.setRecipientEmail(requestDTO.getRecipientEmail());
		notification.setSubject(requestDTO.getSubject());
		notification.setMessage(requestDTO.getMessage());
		notification.setNotificationType(requestDTO.getNotificationType());

		SimpleMailMessage mail = new SimpleMailMessage();

		mail.setTo(requestDTO.getRecipientEmail());
		mail.setSubject(requestDTO.getSubject());
		mail.setText(requestDTO.getMessage());

		mailSender.send(mail);

		notification.setStatus(NotificationStatus.SENT);
		notification.setSentAt(LocalDateTime.now());

		Notification saved = notificationRepo.save(notification);

		return new NotificationResponseDTO(saved.getNotificationId(), saved.getServiceRequestId(),
				saved.getRecipientEmail(), saved.getStatus(), saved.getSentAt());
	}

	@Override
	public NotificationResponseDTO getNotificationById(Long notificationId) {

		Notification notification = notificationRepo.findById(notificationId)
				.orElseThrow(() -> new NotificationNotFoundException("Notification Not Found"));

		return new NotificationResponseDTO(notification.getNotificationId(), notification.getServiceRequestId(),
				notification.getRecipientEmail(), notification.getStatus(), notification.getSentAt());
	}

	@Override
	public List<NotificationResponseDTO> getAllNotifications() {

		return notificationRepo.findAll().stream()
				.map(notification -> new NotificationResponseDTO(notification.getNotificationId(),
						notification.getServiceRequestId(), notification.getRecipientEmail(), notification.getStatus(),
						notification.getSentAt()))
				.collect(Collectors.toList());
	}

	@Override
	public String deleteNotification(Long notificationId) {

		Notification notification = notificationRepo.findById(notificationId)
				.orElseThrow(() -> new NotificationNotFoundException("Notification Not Found"));
		notificationRepo.delete(notification);
		return "Notification Deleted Successfully";
	}
}