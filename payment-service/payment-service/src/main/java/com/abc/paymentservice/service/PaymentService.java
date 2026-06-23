package com.abc.paymentservice.service;

import java.util.List;

import com.abc.paymentservice.dto.PaymentRequestDTO;
import com.abc.paymentservice.dto.PaymentResponseDTO;
import com.abc.paymentservice.dto.PaymentDetailResponseDTO;
import com.abc.paymentservice.enums.PaymentStatus;

public interface PaymentService {

    PaymentResponseDTO processPayment(
            PaymentRequestDTO requestDTO);

    PaymentDetailResponseDTO getPaymentById(Long paymentId);

    List<PaymentDetailResponseDTO> getAllPayments();

    List<PaymentDetailResponseDTO> getPaymentsByRequest(
            Long serviceRequestId);

    PaymentResponseDTO updatePaymentStatus(
            Long paymentId,
            PaymentStatus status);
}