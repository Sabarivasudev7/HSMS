package com.abc.feedbackservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackDetailResponseDTO {

    private Long id;

    private Long serviceRequestId;

    private Integer rating;

    private String comments;
}