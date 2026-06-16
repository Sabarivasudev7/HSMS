package com.feedback_service.dto;



import lombok.Data;

@Data
public class FeedbackResponseDTO {

    private Long id;
    private Long serviceRequestId;
    private Integer rating;
    private String comments;
}