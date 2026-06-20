package com.feedback_service.dto;

import lombok.Data;

@Data
public class FeedbackDetailResponseDTO {

    private Long feedbackId;
    private Long serviceRequestId;
    private Integer rating;
    private String comments;
}