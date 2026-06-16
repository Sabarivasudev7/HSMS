package com.paymentservice.service;

import java.util.List;

import com.paymentservice.dto.PaymentDetailResponseDTO;
import com.paymentservice.dto.PaymentRequestDTO;
import com.paymentservice.dto.PaymentResponseDTO;

public interface PaymentService {

    PaymentResponseDTO createPayment(PaymentRequestDTO requestDTO);

    PaymentResponseDTO getPaymentById(Long paymentId);

    PaymentDetailResponseDTO findPaymentById(Long paymentId);

    List<PaymentResponseDTO> getAllPayments();

    PaymentResponseDTO updatePayment(Long paymentId,
                                     PaymentRequestDTO requestDTO);

    void deletePayment(Long paymentId);
}