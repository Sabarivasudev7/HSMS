package com.abc.analyticsservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.abc.analyticsservice.dto.CategoryAnalyticsDTO;
import com.abc.analyticsservice.dto.DashboardResponseDTO;
import com.abc.analyticsservice.dto.RevenueReportDTO;
import com.abc.analyticsservice.dto.TechnicianReportDTO;
import com.abc.analyticsservice.service.AnalyticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private static final String ADMIN = "ADMIN";
    private static final String SERVICE_MANAGER = "SERVICE_MANAGER";
    private static final String ACCESS_DENIED = "Access Denied";

    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponseDTO> getDashboardSummary(@RequestHeader("X-Role") String role) {
        validateAnalyticsRole(role);
        return ResponseEntity.ok(analyticsService.getDashboardSummary());
    }

    @GetMapping("/revenue")
    public ResponseEntity<RevenueReportDTO> getRevenueReport(@RequestHeader("X-Role") String role) {
        validateAnalyticsRole(role);
        return ResponseEntity.ok(analyticsService.getRevenueReport());
    }

    @GetMapping("/technicians")
    public ResponseEntity<List<TechnicianReportDTO>> getTechnicianReport(@RequestHeader("X-Role") String role) {
        validateAnalyticsRole(role);
        return ResponseEntity.ok(analyticsService.getTechnicianPerformanceReport());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryAnalyticsDTO>> getCategoryReport(@RequestHeader("X-Role") String role) {
        validateAnalyticsRole(role);
        return ResponseEntity.ok(analyticsService.getCategoryAnalyticsReport());
    }

    private void validateAnalyticsRole(String role) {
        if (!ADMIN.equalsIgnoreCase(role) && !SERVICE_MANAGER.equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
    }
}
