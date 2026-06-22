package com.hsms.analytics.service;

import com.hsms.analytics.clients.BookingClient;
import com.hsms.analytics.clients.PaymentClient;
import com.hsms.analytics.clients.FeedbackClient;
import com.hsms.analytics.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class DashboardServiceTest {

    @Mock private BookingClient bookingClient;
    @Mock private PaymentClient paymentClient;
    @Mock private FeedbackClient feedbackClient;

    @InjectMocks private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDashboardReport() {
        BookingResponseDto booking = new BookingResponseDto();
        booking.setCategoryName("Plumbing");
        when(bookingClient.getAllBookings()).thenReturn(List.of(booking));

        PaymentResponseDto payment = new PaymentResponseDto();
        payment.setAmount(200.0);
        when(paymentClient.getAllPayments()).thenReturn(List.of(payment));

        FeedbackResponseDto feedback = new FeedbackResponseDto();
        feedback.setTechnicianId(1L);
        feedback.setTechnicianName("Arun");
        feedback.setRating(4.5);
        when(feedbackClient.getAllFeedbacks()).thenReturn(List.of(feedback));

        DashboardReportDto report = dashboardService.getDashboardReport();

        assertEquals(1L, report.getTotalBookings());
        assertEquals(200.0, report.getRevenue());
        assertEquals(Map.of("Plumbing", 1L), report.getCategoryDistribution());
        assertEquals("Arun", report.getTopTechnicians().get(0).getTechnicianName());
    }
}
