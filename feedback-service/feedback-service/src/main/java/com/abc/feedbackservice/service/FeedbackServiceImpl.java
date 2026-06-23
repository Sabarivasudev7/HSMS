package com.abc.feedbackservice.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.abc.feedbackservice.entity.Feedback;
import com.abc.feedbackservice.exception.FeedbackAlreadyExistsException;
import com.abc.feedbackservice.exception.FeedbackNotFoundException;
import com.abc.feedbackservice.exception.UnauthorizedException;
import com.abc.feedbackservice.feign.ServiceRequestClient;
import com.abc.feedbackservice.model.FeedbackDetailResponseDTO;
import com.abc.feedbackservice.model.FeedbackRequestDTO;
import com.abc.feedbackservice.model.FeedbackResponseDTO;
import com.abc.feedbackservice.model.ServiceRequestResponse;
import com.abc.feedbackservice.repository.FeedbackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

	private final FeedbackRepository feedbackRepository;
	private final ModelMapper modelMapper;
	private final ServiceRequestClient serviceRequestClient;

	@Override
	public FeedbackResponseDTO submitFeedback(FeedbackRequestDTO requestDTO) {

		feedbackRepository.findFirstByServiceRequestId(requestDTO.getServiceRequestId()).ifPresent(feedback -> {
			throw new FeedbackAlreadyExistsException("Feedback already submitted for this service request");
		});

		ServiceRequestResponse request = serviceRequestClient.getServiceRequest(requestDTO.getServiceRequestId(), "ADMIN");

		if (request == null) {
			throw new FeedbackNotFoundException("Service Request not found");
		}

		if (!"COMPLETED".equalsIgnoreCase(request.getStatus())) {

			throw new UnauthorizedException("Feedback can be submitted only after service completion");
		}

		Feedback feedback = modelMapper.map(requestDTO, Feedback.class);
		feedback.setId(null);

		Feedback savedFeedback = feedbackRepository.save(feedback);

		return modelMapper.map(savedFeedback, FeedbackResponseDTO.class);
	}

	@Override
	public FeedbackDetailResponseDTO getFeedbackById(Long feedbackId) {

		Feedback feedback = feedbackRepository.findById(feedbackId)
				.orElseThrow(() -> new FeedbackNotFoundException("Feedback not found with ID: " + feedbackId));

		return modelMapper.map(feedback, FeedbackDetailResponseDTO.class);
	}

	@Override
	public List<FeedbackDetailResponseDTO> getAllFeedbacks() {

		return feedbackRepository.findAll().stream()
				.map(feedback -> modelMapper.map(feedback, FeedbackDetailResponseDTO.class)).toList();
	}

	@Override
	public List<FeedbackDetailResponseDTO> getFeedbackByRequest(Long serviceRequestId) {

		return feedbackRepository.findAllByServiceRequestId(serviceRequestId).stream()
				.map(feedback -> modelMapper.map(feedback, FeedbackDetailResponseDTO.class)).toList();
	}

	@Override
	public void deleteFeedback(Long feedbackId) {

		Feedback feedback = feedbackRepository.findById(feedbackId)
				.orElseThrow(() -> new FeedbackNotFoundException("Feedback not found with ID: " + feedbackId));

		feedbackRepository.delete(feedback);
	}

}
