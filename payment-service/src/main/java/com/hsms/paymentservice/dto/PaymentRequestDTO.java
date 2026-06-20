package com.hsms.paymentservice.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class PaymentRequestDTO {
	
	
	private Long serviceRequestId;

	private Long customerId;

	private Double amount;

	private String paymentMethod;

    
}