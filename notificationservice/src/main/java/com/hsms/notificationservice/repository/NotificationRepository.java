package com.hsms.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hsms.notificationservice.entity.Notification;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    
}