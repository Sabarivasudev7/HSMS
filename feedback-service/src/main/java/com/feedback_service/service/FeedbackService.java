package com.feedback_service.service;

import java.util.List;

import com.feedback_service.dto.FeedbackRequestDTO;
import com.feedback_service.dto.FeedbackResponseDTO;

public interface FeedbackService {

    FeedbackResponseDTO submitFeedback(FeedbackRequestDTO request);

    List<FeedbackResponseDTO> getFeedbackByRequest(Long requestId);
}