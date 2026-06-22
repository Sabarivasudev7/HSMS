package com.hsms.analytics.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.*;
import java.util.stream.Collectors;
import com.hsms.analytics.clients.*;
import com.hsms.analytics.model.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final BookingClient bookingClient;
    private final PaymentClient paymentClient;
    private final FeedbackClient feedbackClient;
   

    public DashboardReportDto getDashboardReport() {
        DashboardReportDto report = new DashboardReportDto();

        List<BookingResponseDto> bookings = bookingClient.getAllBookings();
        report.setTotalBookings((long) bookings.size());

        List<PaymentResponseDto> payments = paymentClient.getAllPayments();
        double totalRevenue = payments.stream()
                .mapToDouble(PaymentResponseDto::getAmount)
                .sum();
        report.setRevenue(totalRevenue);

        Map<String, Long> categoryDistribution = bookings.stream()
                .collect(Collectors.groupingBy(
                        BookingResponseDto::getCategoryName,
                        Collectors.counting()
                ));
        report.setCategoryDistribution(categoryDistribution);

        List<FeedbackResponseDto> feedbacks = feedbackClient.getAllFeedbacks();
        Map<Long, TechnicianPerformanceDto> technicianStats = new HashMap<>();
        for (FeedbackResponseDto fb : feedbacks) {
            TechnicianPerformanceDto dto = technicianStats.computeIfAbsent(fb.getTechnicianId(), id -> {
                TechnicianPerformanceDto t = new TechnicianPerformanceDto();
                t.setTechnicianId(id);
                t.setTechnicianName(fb.getTechnicianName());
                t.setCompletedJobs(0L);
                t.setRating(0.0);
                t.setRevenueGenerated(0.0);
                return t;
            });
            dto.setCompletedJobs(dto.getCompletedJobs() + 1);
            dto.setRating((dto.getRating() + fb.getRating()) / 2);
        }
        report.setTopTechnicians(new ArrayList<>(technicianStats.values()));

        return report;
    }
}
