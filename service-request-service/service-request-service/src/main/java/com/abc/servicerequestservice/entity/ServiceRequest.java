package com.abc.servicerequestservice.entity;

import java.time.LocalDateTime;

import com.abc.servicerequestservice.enums.RequestStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service_requests")
public class ServiceRequest {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		@Column(nullable = false)
		private Long customerId;
		@Column(nullable = false)
		private Long categoryId;
		@Column(nullable = false, length = 100)
		private String serviceType;
		@Enumerated(EnumType.STRING)
		@Column(nullable = false)
		private RequestStatus status;
		@Column(nullable = false)
		private String address;
		@Column(nullable = false)
		private LocalDateTime scheduledTime;
}