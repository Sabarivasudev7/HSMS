package com.abc.feedbackservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.abc.feedbackservice.enums.Role;
import com.abc.feedbackservice.exception.UnauthorizedException;
import com.abc.feedbackservice.model.FeedbackDetailResponseDTO;
import com.abc.feedbackservice.model.FeedbackRequestDTO;
import com.abc.feedbackservice.model.FeedbackResponseDTO;
import com.abc.feedbackservice.service.FeedbackService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

	private static final String ROLE_HEADER = "X-Role";

	private final FeedbackService feedbackService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FeedbackResponseDTO submitFeedback(@RequestHeader(ROLE_HEADER) String role,
			@Valid @RequestBody FeedbackRequestDTO requestDTO) {

		Role userRole = Role.valueOf(role.toUpperCase());

		if (userRole != Role.CUSTOMER) {
			throw new UnauthorizedException("Only CUSTOMER can submit feedback");
		}

		return feedbackService.submitFeedback(requestDTO);
	}

	@GetMapping("/{feedbackId}")
	public FeedbackDetailResponseDTO getFeedbackById(@PathVariable Long feedbackId,
			@RequestHeader(ROLE_HEADER) String role) {

		Role userRole = Role.valueOf(role.toUpperCase());

		if (!(userRole == Role.CUSTOMER || userRole == Role.TECHNICIAN || userRole == Role.ADMIN
				|| userRole == Role.SERVICE_MANAGER)) {

			throw new UnauthorizedException("You are not authorized to view feedback");
		}

		return feedbackService.getFeedbackById(feedbackId);
	}

	@GetMapping
	public List<FeedbackDetailResponseDTO> getAllFeedbacks(@RequestHeader(ROLE_HEADER) String role) {

		Role userRole = Role.valueOf(role.toUpperCase());

		if (!(userRole == Role.ADMIN || userRole == Role.SERVICE_MANAGER)) {

			throw new UnauthorizedException("Only ADMIN or SERVICE_MANAGER can access all feedbacks");
		}

		return feedbackService.getAllFeedbacks();
	}

	@GetMapping("/request/{serviceRequestId}")
	public List<FeedbackDetailResponseDTO> getFeedbackByRequest(@PathVariable Long serviceRequestId,
			@RequestHeader(ROLE_HEADER) String role) {

		Role userRole = Role.valueOf(role.toUpperCase());

		if (!(userRole == Role.CUSTOMER || userRole == Role.TECHNICIAN || userRole == Role.ADMIN
				|| userRole == Role.SERVICE_MANAGER)) {

			throw new UnauthorizedException("You are not authorized to access feedback");
		}

		return feedbackService.getFeedbackByRequest(serviceRequestId);
	}

	@DeleteMapping("/{feedbackId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteFeedback(@PathVariable Long feedbackId, @RequestHeader(ROLE_HEADER) String role) {

		Role userRole = Role.valueOf(role.toUpperCase());

		if (userRole != Role.ADMIN) {
			throw new UnauthorizedException("Only ADMIN can delete feedback");
		}

		feedbackService.deleteFeedback(feedbackId);
	}

}
