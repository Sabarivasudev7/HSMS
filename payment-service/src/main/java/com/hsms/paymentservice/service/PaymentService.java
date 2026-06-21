package com.hsms.paymentservice.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hsms.paymentservice.dto.PaymentRequestDTO;
import com.hsms.paymentservice.dto.PaymentResponseDTO;
import com.hsms.paymentservice.enums.PaymentStatus;

public interface PaymentService {

	PaymentResponseDTO createPayment(String token, PaymentRequestDTO dto);

	PaymentResponseDTO getPaymentById(Long paymentId);

	Page<PaymentResponseDTO> getPaymentsByCustomer(Long customerId, Pageable pageable);

	PaymentResponseDTO updatePaymentStatus(String token, Long paymentId, PaymentStatus status);

	PaymentResponseDTO getPaymentByServiceRequestId(Long serviceRequestId);

	List<PaymentResponseDTO> showAllPayments();

	void deletePayment(Long paymentId);

}