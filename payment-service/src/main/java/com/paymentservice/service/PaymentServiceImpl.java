package com.paymentservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymentservice.dto.PaymentDetailResponseDTO;
import com.paymentservice.dto.PaymentRequestDTO;
import com.paymentservice.dto.PaymentResponseDTO;
import com.paymentservice.entity.Payment;
import com.paymentservice.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO requestDTO) {

        Payment payment = new Payment();

        payment.setServiceRequestId(requestDTO.getServiceRequestId());
        payment.setAmount(requestDTO.getAmount());
        payment.setMethod(requestDTO.getMethod());
        payment.setStatus("SUCCESS");

        Payment savedPayment = paymentRepository.save(payment);

        return mapToResponse(savedPayment);
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return mapToResponse(payment);
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponseDTO updatePayment(Long paymentId,
                                            PaymentRequestDTO requestDTO) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setServiceRequestId(requestDTO.getServiceRequestId());
        payment.setAmount(requestDTO.getAmount());
        payment.setMethod(requestDTO.getMethod());

        Payment updatedPayment = paymentRepository.save(payment);

        return mapToResponse(updatedPayment);
    }

    @Override
    public void deletePayment(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        paymentRepository.delete(payment);
    }

    private PaymentResponseDTO mapToResponse(Payment payment) {

        PaymentResponseDTO response = new PaymentResponseDTO();

        response.setPaymentId(payment.getId());
        response.setServiceRequestId(payment.getServiceRequestId());

        // IMPORTANT: these fields were missing
        response.setAmount(payment.getAmount());
        response.setMethod(payment.getMethod());

        response.setStatus(payment.getStatus());

        return response;
    }

    @Override
    public PaymentDetailResponseDTO findPaymentById(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        PaymentDetailResponseDTO response = new PaymentDetailResponseDTO();

        response.setPaymentId(payment.getId());
        response.setServiceRequestId(payment.getServiceRequestId());
        response.setAmount(payment.getAmount());
        response.setMethod(payment.getMethod());
        response.setStatus(payment.getStatus());

        return response;
    }
}