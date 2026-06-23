package com.abc.assignmentservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianDetailResponseDTO {
    private Long userId;
    private String skill;
    private Integer experience;
    private String availability;
    private Double rating;
}