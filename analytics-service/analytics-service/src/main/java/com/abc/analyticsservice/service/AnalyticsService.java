package com.abc.analyticsservice.service;

import java.util.List;

import com.abc.analyticsservice.dto.CategoryAnalyticsDTO;
import com.abc.analyticsservice.dto.DashboardResponseDTO;
import com.abc.analyticsservice.dto.RevenueReportDTO;
import com.abc.analyticsservice.dto.TechnicianReportDTO;

public interface AnalyticsService {

    DashboardResponseDTO getDashboardSummary();

    RevenueReportDTO getRevenueReport();

    List<TechnicianReportDTO> getTechnicianPerformanceReport();

    List<CategoryAnalyticsDTO> getCategoryAnalyticsReport();

}