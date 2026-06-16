package com.feedback_service.contoller;



import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.feedback_service.dto.FeedbackRequestDTO;
import com.feedback_service.dto.FeedbackResponseDTO;
import com.feedback_service.service.FeedbackService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@Validated
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> submitFeedback(
            @Valid @RequestBody FeedbackRequestDTO request) {

        return new ResponseEntity<>(
                feedbackService.submitFeedback(request),
                HttpStatus.CREATED);
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbackByRequest(
            @PathVariable Long requestId) {

        return ResponseEntity.ok(
                feedbackService.getFeedbackByRequest(requestId));
    }
}