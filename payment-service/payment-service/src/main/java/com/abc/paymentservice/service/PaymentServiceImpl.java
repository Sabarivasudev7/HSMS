package com.abc.paymentservice.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.abc.paymentservice.dto.BookingResponseDTO;
import com.abc.paymentservice.dto.PaymentRequestDTO;
import com.abc.paymentservice.dto.PaymentResponseDTO;
import com.abc.paymentservice.dto.PaymentDetailResponseDTO;
import com.abc.paymentservice.entity.Payment;
import com.abc.paymentservice.enums.PaymentStatus;
import com.abc.paymentservice.exception.InvalidPaymentException;
import com.abc.paymentservice.exception.PaymentNotFoundException;
import com.abc.paymentservice.exception.ServiceUnavailableException;
import com.abc.paymentservice.feign.BookingFeignClient;
import com.abc.paymentservice.repository.PaymentRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepo;

	private final BookingFeignClient bookingFeignClient;

	private final ModelMapper modelMapper;

	private final com.abc.paymentservice.feign.NotificationServiceClient notificationServiceClient;

	// Process Payment
	@Override
	@CircuitBreaker(name = "paymentService", fallbackMethod = "processPaymentFallback")
	public PaymentResponseDTO processPayment(PaymentRequestDTO dto) {

		// Validate Booking
		BookingResponseDTO booking = bookingFeignClient.getBookingById(dto.getServiceRequestId(), "ADMIN");

		if (booking == null) {
			throw new InvalidPaymentException("Booking not found");
		}

		// Payment allowed only for COMPLETED requests
		if (!"COMPLETED".equalsIgnoreCase(booking.getStatus())) {
			throw new InvalidPaymentException("Payment allowed only for COMPLETED requests");
		}

		// Prevent Duplicate Payment or Trigger Retry
		Optional<Payment> existingOpt = paymentRepo.findByServiceRequestId(dto.getServiceRequestId());
		Payment payment;
		
		if (existingOpt.isPresent()) {
			payment = existingOpt.get();
			if (payment.getStatus() == PaymentStatus.SUCCESS) {
				throw new InvalidPaymentException("Payment already exists and is successful for booking " + dto.getServiceRequestId());
			}
			// Update existing record (retry operation)
			payment.setAmount(dto.getAmount());
			payment.setMethod(dto.getMethod());
		} else {
			payment = new Payment();
			payment.setServiceRequestId(dto.getServiceRequestId());
			payment.setAmount(dto.getAmount());
			payment.setMethod(dto.getMethod());
		}

		// Determine status: simulate FAILED if amount is zero or negative
		PaymentStatus outcomeStatus = (dto.getAmount() == null || dto.getAmount() <= 0) ? PaymentStatus.FAILED : PaymentStatus.SUCCESS;
		payment.setStatus(outcomeStatus);

		Payment saved = paymentRepo.save(payment);
        if (outcomeStatus == PaymentStatus.SUCCESS) {
            notificationServiceClient.sendNotification(new com.abc.paymentservice.feign.NotificationServiceClient.NotificationRequest(
                    booking.getCustomerId(),
                    "PAYMENT_SUCCESS",
                    "Payment of $" + saved.getAmount() + " for Service Request ID: "
                            + saved.getServiceRequestId() + " was successful!"
            ));
        }

		PaymentResponseDTO response = new PaymentResponseDTO();
		response.setId(saved.getId());
		response.setStatus(saved.getStatus());
		return response;
	}

	// Fallback Method
	public PaymentResponseDTO processPaymentFallback(PaymentRequestDTO dto, Exception ex) {
		throw new ServiceUnavailableException("Booking Service is currently unavailable. Please try again later.");
	}

	// Get Payment By Id
	@Override
	public PaymentDetailResponseDTO getPaymentById(Long paymentId) {
		Payment payment = paymentRepo.findById(paymentId)
				.orElseThrow(() -> new PaymentNotFoundException("Payment " + paymentId + " not found"));

		return modelMapper.map(payment, PaymentDetailResponseDTO.class);
	}

	// Get All Payments
	@Override
	public List<PaymentDetailResponseDTO> getAllPayments() {
		return paymentRepo.findAll().stream()
				.map(payment -> modelMapper.map(payment, PaymentDetailResponseDTO.class))
				.toList();
	}

	// Get Payments By Request
	@Override
	public List<PaymentDetailResponseDTO> getPaymentsByRequest(Long serviceRequestId) {
		return paymentRepo.findByServiceRequestId(serviceRequestId).stream()
				.map(payment -> modelMapper.map(payment, PaymentDetailResponseDTO.class))
				.toList();
	}

	// Update Payment Status
	@Override
	public PaymentResponseDTO updatePaymentStatus(Long paymentId, PaymentStatus status) {
		Payment payment = paymentRepo.findById(paymentId)
				.orElseThrow(() -> new PaymentNotFoundException("Payment " + paymentId + " not found"));

		payment.setStatus(status);
		Payment updated = paymentRepo.save(payment);

		PaymentResponseDTO response = new PaymentResponseDTO();
		response.setId(updated.getId());
		response.setStatus(updated.getStatus());
		return response;
	}

}