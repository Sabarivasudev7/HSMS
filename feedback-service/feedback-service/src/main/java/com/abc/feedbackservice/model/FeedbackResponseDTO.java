package com.abc.feedbackservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackResponseDTO {

    private Long id;

    private Integer rating;
}