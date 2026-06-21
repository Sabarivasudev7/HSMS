package com.hsms.paymentservice.dto;

import java.time.LocalDateTime;

import com.hsms.paymentservice.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data

public class BookingResponseDTO {
	

	private Long requestId;
	

	private Long customerId;
	

	private Long categoryId;
	

	private String serviceType;
	
	
	private String address;
	

	private String status;
	

	private LocalDateTime createdAt;
	
}