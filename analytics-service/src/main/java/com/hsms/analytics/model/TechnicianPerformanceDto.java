package com.hsms.analytics.model;

import lombok.Data;

@Data
public class TechnicianPerformanceDto {
    private Long technicianId;
    private String technicianName;
    private Long completedJobs;
    private Double rating;
    private Double revenueGenerated;
}