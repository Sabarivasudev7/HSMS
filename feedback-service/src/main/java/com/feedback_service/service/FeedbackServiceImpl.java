package com.feedback_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feedback_service.dto.BookingResponseDTO;
import com.feedback_service.dto.FeedbackRequestDTO;
import com.feedback_service.dto.FeedbackResponseDTO;
import com.feedback_service.dto.PaymentResponseDTO;
import com.feedback_service.entity.Feedback;
import com.feedback_service.exception.FeedbackNotFoundException;
import com.feedback_service.feign.BookingFeignClient;
import com.feedback_service.feign.PaymentFeignClient;
import com.feedback_service.repository.FeedbackRepository;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private BookingFeignClient bookingFeignClient;

    @Autowired
    private PaymentFeignClient paymentFeignClient;

    @Override
    public FeedbackResponseDTO submitFeedback(FeedbackRequestDTO request) {

        // Validate Booking Exists
        BookingResponseDTO booking =
                bookingFeignClient.getBookingById(
                        request.getServiceRequestId());

        if (booking == null) {
            throw new RuntimeException(
                    "Booking not found with id: "
                            + request.getServiceRequestId());
        }

        // Validate Payment Exists
        PaymentResponseDTO payment =
                paymentFeignClient.getPaymentByRequestId(
                        request.getServiceRequestId());

        if (payment == null) {
            throw new RuntimeException(
                    "Payment not found for request id: "
                            + request.getServiceRequestId());
        }

        // Optional: Allow feedback only after successful payment
        if (!"SUCCESS".equalsIgnoreCase(payment.getStatus())) {
            throw new RuntimeException(
                    "Feedback can be submitted only after successful payment");
        }

        Feedback feedback = new Feedback();
        feedback.setServiceRequestId(request.getServiceRequestId());
        feedback.setRating(request.getRating());
        feedback.setComments(request.getComments());

        Feedback savedFeedback = feedbackRepository.save(feedback);

        FeedbackResponseDTO response = new FeedbackResponseDTO();
        response.setId(savedFeedback.getId());
        response.setServiceRequestId(savedFeedback.getServiceRequestId());
        response.setRating(savedFeedback.getRating());
        response.setComments(savedFeedback.getComments());

        return response;
    }

    @Override
    public List<FeedbackResponseDTO> getFeedbackByRequest(Long requestId) {

        List<Feedback> feedbackList =
                feedbackRepository.findByServiceRequestId(requestId);

        if (feedbackList.isEmpty()) {
            throw new FeedbackNotFoundException(
                    "No feedback found for service request id: "
                            + requestId);
        }

        return feedbackList.stream()
                .map(feedback -> {
                    FeedbackResponseDTO dto =
                            new FeedbackResponseDTO();

                    dto.setId(feedback.getId());
                    dto.setServiceRequestId(
                            feedback.getServiceRequestId());
                    dto.setRating(feedback.getRating());
                    dto.setComments(feedback.getComments());

                    return dto;
                })
                .collect(Collectors.toList());
    }
}