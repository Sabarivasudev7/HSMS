package com.abc.paymentservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abc.paymentservice.dto.PaymentRequestDTO;
import com.abc.paymentservice.dto.PaymentResponseDTO;
import com.abc.paymentservice.dto.PaymentDetailResponseDTO;
import com.abc.paymentservice.enums.PaymentStatus;
import com.abc.paymentservice.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // POST /api/payments
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> processPayment(
            @Valid @RequestBody PaymentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.processPayment(dto));
    }

    // GET /api/payments
    @GetMapping
    public ResponseEntity<List<PaymentDetailResponseDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    // GET /api/payments/{paymentId}
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDetailResponseDTO> getPaymentById(
            @PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

    // GET /api/payments/request/{serviceRequestId}
    @GetMapping("/request/{serviceRequestId}")
    public ResponseEntity<List<PaymentDetailResponseDTO>> getPaymentsByRequest(
            @PathVariable Long serviceRequestId) {
        return ResponseEntity.ok(paymentService.getPaymentsByRequest(serviceRequestId));
    }

    // PATCH /api/payments/{paymentId}/status
    @PatchMapping("/{paymentId}/status")
    public ResponseEntity<PaymentResponseDTO> updatePaymentStatus(
            @PathVariable Long paymentId,
            @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(paymentId, status));
    }

}