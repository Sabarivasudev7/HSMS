package com.hsms.analytics.controller;

import com.hsms.analytics.model.DashboardReportDto;
import com.hsms.analytics.service.DashboardService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private DashboardService dashboardService;

    @Test
    void testGetDashboardAsAdmin() throws Exception {
        DashboardReportDto report = new DashboardReportDto();
        report.setTotalBookings(10L);
        report.setRevenue(500.0);
        when(dashboardService.getDashboardReport()).thenReturn(report);

        mockMvc.perform(get("/api/reports/dashboard")
                .header("X-Role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalBookings").value(10))
                .andExpect(jsonPath("$.revenue").value(500.0));
    }

    @Test
    void testGetDashboardAsUser() throws Exception {
        mockMvc.perform(get("/api/reports/dashboard")
                .header("X-Role", "USER"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Access Denied"));
    }
}
