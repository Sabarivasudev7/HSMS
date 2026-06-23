package com.abc.notificationservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.MailSendException;

import com.abc.notificationservice.dto.NotificationRequestDTO;
import com.abc.notificationservice.dto.NotificationResponseDTO;
import com.abc.notificationservice.dto.UserResponseDTO;
import com.abc.notificationservice.entity.Notification;
import com.abc.notificationservice.enums.NotificationType;
import com.abc.notificationservice.feign.UserServiceClient;
import com.abc.notificationservice.repository.NotificationRepository;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private UserServiceClient userServiceClient;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private NotificationRequestDTO requestDTO;
    private UserResponseDTO userResponseDTO;
    private Notification notification;

    @BeforeEach
    public void setup() {
        requestDTO = new NotificationRequestDTO();
        requestDTO.setUserId(10L);
        requestDTO.setType(NotificationType.SERVICE_CREATED);
        requestDTO.setMessage("Service request has been successfully created.");

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUserId(10L);
        userResponseDTO.setName("Karthick");
        userResponseDTO.setEmail("karthick@example.com");

        notification = new Notification();
        notification.setId(100L);
        notification.setUserId(10L);
        notification.setType(NotificationType.SERVICE_CREATED);
        notification.setMessage("Service request has been successfully created.");
    }

    @Test
    public void testCreateNotification_Success() {
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification n = invocation.getArgument(0);
            n.setId(100L);
            return n;
        });

        when(userServiceClient.getUserProfile(10L, "ADMIN"))
                .thenReturn(ResponseEntity.ok(userResponseDTO));

        NotificationResponseDTO response = notificationService.createNotification(requestDTO);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals(10L, response.getUserId());
        assertEquals(NotificationType.SERVICE_CREATED, response.getType());
        assertEquals("Service request has been successfully created.", response.getMessage());

        verify(notificationRepository).save(any(Notification.class));
        verify(userServiceClient).getUserProfile(10L, "ADMIN");
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testCreateNotification_MailException_SucceedsResiliently() {
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification n = invocation.getArgument(0);
            n.setId(100L);
            return n;
        });

        when(userServiceClient.getUserProfile(10L, "ADMIN"))
                .thenReturn(ResponseEntity.ok(userResponseDTO));

        // Mock mail server exception
        doThrow(new MailSendException("SMTP Server Unavailable"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        // The method should not throw an exception; it should handle it gracefully
        NotificationResponseDTO response = notificationService.createNotification(requestDTO);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        verify(notificationRepository).save(any(Notification.class));
        verify(userServiceClient).getUserProfile(10L, "ADMIN");
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
