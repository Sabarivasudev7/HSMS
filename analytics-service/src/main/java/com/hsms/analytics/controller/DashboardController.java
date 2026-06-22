package com.hsms.analytics.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.hsms.analytics.model.DashboardReportDto;
import com.hsms.analytics.service.DashboardService;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<Object> getDashboard(@RequestHeader("X-Role") String role) {
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("Access Denied");
        }
        DashboardReportDto report = dashboardService.getDashboardReport();
        return ResponseEntity.ok(report);
    }
}
