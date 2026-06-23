package com.abc.servicerequestservice.dto;

import java.time.LocalDateTime;

import com.abc.servicerequestservice.enums.RequestStatus;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestDetailResponse {
		private Long requestId;
		private Long customerId;
		private Long categoryId;
		private String serviceType;
		private RequestStatus status;
		private String address;
		private LocalDateTime scheduledTime;
		private Long technicianId;
		private String technicianName;
}
