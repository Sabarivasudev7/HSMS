package com.feedback_service.service;



import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.feedback_service.dto.FeedbackRequestDTO;
import com.feedback_service.dto.FeedbackResponseDTO;
import com.feedback_service.entity.Feedback;
import com.feedback_service.repository.FeedbackRepository;
import com.feedback_service.service.FeedbackService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Override
    public FeedbackResponseDTO submitFeedback(FeedbackRequestDTO request) {

        Feedback feedback = new Feedback();
        feedback.setServiceRequestId(request.getServiceRequestId());
        feedback.setRating(request.getRating());
        feedback.setComments(request.getComments());

        Feedback saved = feedbackRepository.save(feedback);

        return mapToResponse(saved);
    }

    @Override
    public List<FeedbackResponseDTO> getFeedbackByRequest(Long requestId) {

        return feedbackRepository.findByServiceRequestId(requestId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private FeedbackResponseDTO mapToResponse(Feedback feedback) {

        FeedbackResponseDTO response = new FeedbackResponseDTO();

        response.setId(feedback.getId());
        response.setServiceRequestId(feedback.getServiceRequestId());
        response.setRating(feedback.getRating());
        response.setComments(feedback.getComments());

        return response;
    }
}