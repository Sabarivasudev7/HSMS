package com.abc.feedbackservice.service;

import java.util.List;

import com.abc.feedbackservice.model.FeedbackDetailResponseDTO;
import com.abc.feedbackservice.model.FeedbackRequestDTO;
import com.abc.feedbackservice.model.FeedbackResponseDTO;

public interface FeedbackService {

	FeedbackResponseDTO submitFeedback(FeedbackRequestDTO requestDTO);

	FeedbackDetailResponseDTO getFeedbackById(Long feedbackId);

	List<FeedbackDetailResponseDTO> getAllFeedbacks();

	List<FeedbackDetailResponseDTO> getFeedbackByRequest(Long serviceRequestId);

	void deleteFeedback(Long feedbackId);
}
