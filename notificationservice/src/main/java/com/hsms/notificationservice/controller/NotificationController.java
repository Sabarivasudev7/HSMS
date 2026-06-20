package com.hsms.notificationservice.controller;

import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hsms.notificationservice.dto.NotificationRequestDTO;
import com.hsms.notificationservice.dto.NotificationResponseDTO;
import com.hsms.notificationservice.service.NotificationService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/notification")
public class NotificationController {

	
    @Autowired
    private NotificationService notificationService;

    
    
    @PostMapping("/create")
    public ResponseEntity<NotificationResponseDTO> createNotification(@Valid  @RequestBody NotificationRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(notificationService.createNotification(dto));
    }

    
    
    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponseDTO> getNotificationById(@PathVariable Long notificationId) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(notificationService.getNotificationById(notificationId));
    }

    
    
    @GetMapping("/all")
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotifications() {
        return ResponseEntity.status(HttpStatus.SC_OK).body(notificationService.getAllNotifications());
    }

    
    
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long notificationId) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(notificationService.deleteNotification(notificationId));   
    }
    
    
}