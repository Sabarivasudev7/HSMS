package com.hsms.analytics.model;

import lombok.Data;

@Data
public class FeedbackResponseDto {
    private Long feedbackId;
    private Long requestId;
    private Long technicianId;
    private String technicianName;
    private Double rating;
    private String comments;
}