package com.abc.paymentservice.dto;

import com.abc.paymentservice.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
	
	private Long id;
	private PaymentStatus status;
    
}