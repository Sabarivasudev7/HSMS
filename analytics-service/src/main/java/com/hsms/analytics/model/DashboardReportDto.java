package com.hsms.analytics.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class DashboardReportDto {
    private Long totalBookings;
    private Double revenue;
    private List<TechnicianPerformanceDto> topTechnicians;
    private Map<String, Long> categoryDistribution;
}