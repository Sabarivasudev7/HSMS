package com.hsms.paymentservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hsms.paymentservice.FeignClient.BookingFeignClient;
import com.hsms.paymentservice.dto.BookingResponseDTO;
import com.hsms.paymentservice.dto.PaymentRequestDTO;
import com.hsms.paymentservice.dto.PaymentResponseDTO;
import com.hsms.paymentservice.entity.Payment;
import com.hsms.paymentservice.enums.PaymentMethod;
import com.hsms.paymentservice.enums.PaymentStatus;
import com.hsms.paymentservice.exception.InvalidPaymentException;
import com.hsms.paymentservice.exception.PaymentNotFoundException;
import com.hsms.paymentservice.exception.ServiceUnavailableException;
import com.hsms.paymentservice.repository.PaymentRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentRepository paymentRepo;

	@Autowired
	private BookingFeignClient bookingFeignClient;

	@Autowired
	private ModelMapper modelMapper;

	// Create Payment
	@Override
	@CircuitBreaker(name = "paymentService", fallbackMethod = "createPaymentFallback")
	public PaymentResponseDTO createPayment(String token, PaymentRequestDTO dto) {

		// Validate Payment Method
		PaymentMethod method;
		try {
			method = PaymentMethod.valueOf(dto.getPaymentMethod().toUpperCase());
		} catch (Exception e) {
			throw new InvalidPaymentException("Invalid payment method. Allowed methods: UPI, CARD, WALLET");
		}

		// Validate Booking
		BookingResponseDTO booking = bookingFeignClient.getBookingById(token, dto.getServiceRequestId());

		if (booking == null) {
			throw new InvalidPaymentException("Booking not found");
		}

		if (!"COMPLETED".equalsIgnoreCase(booking.getStatus())) {
			throw new InvalidPaymentException("Payment allowed only for COMPLETED requests");
		}

		// Prevent Duplicate Payment / Handle Retry
		Payment payment;
		Optional<Payment> existingPaymentOpt = paymentRepo.findByServiceRequestId(dto.getServiceRequestId());
		if (existingPaymentOpt.isPresent()) {
			Payment existing = existingPaymentOpt.get();
			if (existing.getPaymentStatus() == PaymentStatus.SUCCESS) {
				throw new InvalidPaymentException("Payment already exists and is SUCCESS for booking " + dto.getServiceRequestId());
			}
			// Retry flow: reuse the existing payment record
			payment = existing;
			payment.setAmount(java.math.BigDecimal.valueOf(dto.getAmount()));
			payment.setPaymentMethod(method);
			payment.setCustomerId(dto.getCustomerId());
		} else {
			payment = modelMapper.map(dto, Payment.class);
			payment.setPaymentMethod(method);
		}

		// Simulate payment failure for specific amounts (e.g., <= 0 or 999) to test failure and retry options
		PaymentStatus statusToSet = (dto.getAmount() == null || dto.getAmount() <= 0 || dto.getAmount() == 999.0) 
				? PaymentStatus.FAILED 
				: PaymentStatus.SUCCESS;

		payment.setPaymentStatus(statusToSet);
		payment.setPaymentDate(LocalDateTime.now());

		Payment saved = paymentRepo.save(payment);

		if (statusToSet == PaymentStatus.SUCCESS) {
			try {
				bookingFeignClient.updateBookingStatus(token, dto.getServiceRequestId(), "PAID");
			} catch (Exception e) {
				System.err.println("Failed to update booking status to PAID: " + e.getMessage());
			}
		}

		PaymentResponseDTO response = modelMapper.map(saved, PaymentResponseDTO.class);
		response.setBookingId(saved.getServiceRequestId());
		return response;
	}

	// Fallback Method
	public PaymentResponseDTO createPaymentFallback(String token, PaymentRequestDTO dto, Throwable ex) {
		if (ex instanceof InvalidPaymentException) {
			throw (InvalidPaymentException) ex;
		}
		if (ex instanceof PaymentNotFoundException) {
			throw (PaymentNotFoundException) ex;
		}
		throw new ServiceUnavailableException("Booking Service is currently unavailable. Please try again later.");
	}

	// Get Payment By Id
	@Override
	public PaymentResponseDTO getPaymentById(Long paymentId) {

		Payment payment = paymentRepo.findById(paymentId)
				.orElseThrow(() -> new PaymentNotFoundException("Payment " + paymentId + " not found"));

		PaymentResponseDTO response = modelMapper.map(payment, PaymentResponseDTO.class);
		response.setBookingId(payment.getServiceRequestId());
		return response;
	}

	// Get Payments By Customer
	@Override
	public Page<PaymentResponseDTO> getPaymentsByCustomer(Long customerId, Pageable pageable) {

		return paymentRepo.findByCustomerId(customerId, pageable)
				.map(payment -> {
					PaymentResponseDTO response = modelMapper.map(payment, PaymentResponseDTO.class);
					response.setBookingId(payment.getServiceRequestId());
					return response;
				});
	}

	// Update Payment Status
	@Override
	public PaymentResponseDTO updatePaymentStatus(String token, Long paymentId, PaymentStatus status) {

		Payment payment = paymentRepo.findById(paymentId)
				.orElseThrow(() -> new PaymentNotFoundException("Payment " + paymentId + " not found"));

		payment.setPaymentStatus(status);

		Payment updated = paymentRepo.save(payment);

		if (status == PaymentStatus.SUCCESS) {
			try {
				bookingFeignClient.updateBookingStatus(token, updated.getServiceRequestId(), "PAID");
			} catch (Exception e) {
				System.err.println("Failed to update booking status to PAID: " + e.getMessage());
			}
		}

		PaymentResponseDTO response = modelMapper.map(updated, PaymentResponseDTO.class);
		response.setBookingId(updated.getServiceRequestId());
		return response;
	}

	// Show All Payments
	@Override
	public List<PaymentResponseDTO> showAllPayments() {

		return paymentRepo.findAll().stream().map(payment -> {
			PaymentResponseDTO response = modelMapper.map(payment, PaymentResponseDTO.class);
			response.setBookingId(payment.getServiceRequestId());
			return response;
		}).toList();
	}

	@Override
	public void deletePayment(Long paymentId) {

		Payment payment = paymentRepo.findById(paymentId)
				.orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

		paymentRepo.delete(payment);
	}

	@Override
	public PaymentResponseDTO getPaymentByServiceRequestId(Long serviceRequestId) {

		Payment payment = paymentRepo.findByServiceRequestId(serviceRequestId)
				.orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

		PaymentResponseDTO response = modelMapper.map(payment, PaymentResponseDTO.class);
		response.setBookingId(payment.getServiceRequestId());
		return response;
	}

}