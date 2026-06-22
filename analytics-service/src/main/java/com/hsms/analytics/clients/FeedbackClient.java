package com.hsms.analytics.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.hsms.analytics.model.FeedbackResponseDto;

@FeignClient(name = "FEEDBACK-SERVICE", contextId = "feedbackClient")
public interface FeedbackClient {
	@GetMapping("/api/feedback/all")
	List<FeedbackResponseDto> getAllFeedbacks();
}