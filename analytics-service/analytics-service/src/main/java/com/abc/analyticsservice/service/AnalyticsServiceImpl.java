package com.abc.analyticsservice.service;

import java.util.*;
import org.springframework.stereotype.Service;

import com.abc.analyticsservice.dto.CategoryAnalyticsDTO;
import com.abc.analyticsservice.dto.DashboardResponseDTO;
import com.abc.analyticsservice.dto.RevenueReportDTO;
import com.abc.analyticsservice.dto.TechnicianReportDTO;
import com.abc.analyticsservice.feign.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final ServiceRequestClient serviceRequestClient;
    private final PaymentServiceClient paymentServiceClient;
    private final AssignmentServiceClient assignmentServiceClient;
    private final FeedbackServiceClient feedbackServiceClient;
    private final UserServiceClient userServiceClient;
    private final CategoryServiceClient categoryServiceClient;

    @Override
    public DashboardResponseDTO getDashboardSummary() {
        DashboardResponseDTO dto = new DashboardResponseDTO();

        // 1. Service Requests
        List<Object> requests = serviceRequestClient.getAllServiceRequests("ADMIN");
        long totalRequests = requests.size();
        long completedRequests = 0;
        long pendingRequests = 0;
        for (Object req : requests) {
            if (req instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) req;
                String status = toStringVal(map.get("status"));
                if ("COMPLETED".equalsIgnoreCase(status)) {
                    completedRequests++;
                } else if ("CREATED".equalsIgnoreCase(status) || "ASSIGNED".equalsIgnoreCase(status) || 
                           "ACCEPTED".equalsIgnoreCase(status) || "IN_PROGRESS".equalsIgnoreCase(status)) {
                    pendingRequests++;
                }
            }
        }

        // 2. Assignments & Technicians
        List<Object> assignments = assignmentServiceClient.getAllAssignments("ADMIN");
        Set<Long> uniqueTechs = new HashSet<>();
        for (Object ass : assignments) {
            if (ass instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) ass;
                Long techId = toLong(map.get("technicianId"));
                if (techId != null) {
                    uniqueTechs.add(techId);
                }
            }
        }

        // 3. Payments
        List<?> payments = Collections.emptyList();
        Object res = paymentServiceClient.getAllPayments();
        if (res instanceof List) {
            payments = (List<?>) res;
        }
        double totalRevenue = 0.0;
        for (Object pay : payments) {
            if (pay instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) pay;
                String status = toStringVal(map.get("status"));
                if ("SUCCESS".equalsIgnoreCase(status)) {
                    Double amt = toDouble(map.get("amount"));
                    if (amt != null) {
                        totalRevenue += amt;
                    }
                }
            }
        }

        // 4. Feedback
        List<Object> feedbacks = feedbackServiceClient.getAllFeedbacks("ADMIN");
        double sumRating = 0.0;
        int ratingCount = 0;
        for (Object feed : feedbacks) {
            if (feed instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) feed;
                Double rating = toDouble(map.get("rating"));
                if (rating != null) {
                    sumRating += rating;
                    ratingCount++;
                }
            }
        }
        double avgRating = ratingCount > 0 ? sumRating / ratingCount : 0.0;

        dto.setTotalRequests(totalRequests);
        dto.setCompletedRequests(completedRequests);
        dto.setPendingRequests(pendingRequests);
        dto.setTotalTechnicians((long) uniqueTechs.size());
        dto.setTotalRevenue(totalRevenue);
        dto.setAverageRating(avgRating);

        return dto;
    }

    @Override
    public RevenueReportDTO getRevenueReport() {
        RevenueReportDTO dto = new RevenueReportDTO();

        List<?> payments = Collections.emptyList();
        Object res = paymentServiceClient.getAllPayments();
        if (res instanceof List) {
            payments = (List<?>) res;
        }

        double totalRevenue = 0.0;
        long successfulPayments = 0;
        long failedPayments = 0;

        for (Object pay : payments) {
            if (pay instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) pay;
                String status = toStringVal(map.get("status"));
                if ("SUCCESS".equalsIgnoreCase(status)) {
                    successfulPayments++;
                    Double amt = toDouble(map.get("amount"));
                    if (amt != null) {
                        totalRevenue += amt;
                    }
                } else if ("FAILED".equalsIgnoreCase(status)) {
                    failedPayments++;
                }
            }
        }

        dto.setTotalRevenue(totalRevenue);
        dto.setSuccessfulPayments(successfulPayments);
        dto.setFailedPayments(failedPayments);

        return dto;
    }

    @Override
    public List<TechnicianReportDTO> getTechnicianPerformanceReport() {
        List<TechnicianReportDTO> reports = new ArrayList<>();

        // 1. Fetch all assignments
        List<Object> assignments = assignmentServiceClient.getAllAssignments("ADMIN");

        // Group assignments and serviceRequestId by technicianId
        Map<Long, List<Long>> techToRequestsMap = new HashMap<>();
        Map<Long, Long> techToAssignedJobsCount = new HashMap<>();
        
        for (Object ass : assignments) {
            if (ass instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) ass;
                Long techId = toLong(map.get("technicianId"));
                Long reqId = toLong(map.get("serviceRequestId"));
                String status = toStringVal(map.get("status"));
                
                if (techId != null) {
                    // Do not count rejected assignments as assigned jobs
                    if (!"REJECTED".equalsIgnoreCase(status)) {
                        techToAssignedJobsCount.put(techId, techToAssignedJobsCount.getOrDefault(techId, 0L) + 1);
                        if (reqId != null) {
                            techToRequestsMap.computeIfAbsent(techId, k -> new ArrayList<>()).add(reqId);
                        }
                    }
                }
            }
        }

        // 2. Fetch all service requests to know their statuses
        List<Object> requests = serviceRequestClient.getAllServiceRequests("ADMIN");
        Map<Long, String> requestStatusMap = new HashMap<>();
        for (Object req : requests) {
            if (req instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) req;
                Long reqId = toLong(map.get("requestId")); // in DTO it is requestId
                if (reqId == null) {
                    reqId = toLong(map.get("id")); // fallback
                }
                String status = toStringVal(map.get("status"));
                if (reqId != null && status != null) {
                    requestStatusMap.put(reqId, status);
                }
            }
        }

        // 3. Fetch all feedback to know rating per service request
        List<Object> feedbacks = feedbackServiceClient.getAllFeedbacks("ADMIN");
        Map<Long, Double> requestRatingMap = new HashMap<>();
        for (Object feed : feedbacks) {
            if (feed instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) feed;
                Long reqId = toLong(map.get("serviceRequestId"));
                Double rating = toDouble(map.get("rating"));
                if (reqId != null && rating != null) {
                    requestRatingMap.put(reqId, rating);
                }
            }
        }

        // 4. Generate report for each technician
        for (Long techId : techToAssignedJobsCount.keySet()) {
            TechnicianReportDTO report = new TechnicianReportDTO();
            report.setTechnicianId(techId);

            // Fetch name
            String name = "Technician " + techId;
            Object profile = userServiceClient.getUserProfile(techId, "ADMIN");
            if (profile instanceof Map) {
                String pName = toStringVal(((Map<?, ?>) profile).get("name"));
                if (pName != null && !pName.trim().isEmpty()) {
                    name = pName;
                }
            }
            report.setTechnicianName(name);

            long assignedJobs = techToAssignedJobsCount.getOrDefault(techId, 0L);
            long completedJobs = 0;
            double ratingSum = 0.0;
            int ratedJobsCount = 0;

            List<Long> reqIds = techToRequestsMap.getOrDefault(techId, Collections.emptyList());
            for (Long reqId : reqIds) {
                String reqStatus = requestStatusMap.get(reqId);
                if ("COMPLETED".equalsIgnoreCase(reqStatus)) {
                    completedJobs++;
                }
                Double rating = requestRatingMap.get(reqId);
                if (rating != null) {
                    ratingSum += rating;
                    ratedJobsCount++;
                }
            }

            report.setAssignedJobs(assignedJobs);
            report.setCompletedJobs(completedJobs);
            report.setAverageRating(ratedJobsCount > 0 ? ratingSum / ratedJobsCount : 0.0);

            reports.add(report);
        }

        return reports;
    }

    @Override
    public List<CategoryAnalyticsDTO> getCategoryAnalyticsReport() {
        List<CategoryAnalyticsDTO> reports = new ArrayList<>();

        // 1. Fetch all service requests
        List<Object> requests = serviceRequestClient.getAllServiceRequests("ADMIN");

        // 2. Fetch all payments to sum category revenues
        List<?> payments = Collections.emptyList();
        Object res = paymentServiceClient.getAllPayments();
        if (res instanceof List) {
            payments = (List<?>) res;
        }
        Map<Long, Double> requestRevenueMap = new HashMap<>();
        for (Object pay : payments) {
            if (pay instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) pay;
                String status = toStringVal(map.get("status"));
                if ("SUCCESS".equalsIgnoreCase(status)) {
                    Long reqId = toLong(map.get("serviceRequestId"));
                    Double amt = toDouble(map.get("amount"));
                    if (reqId != null && amt != null) {
                        requestRevenueMap.put(reqId, requestRevenueMap.getOrDefault(reqId, 0.0) + amt);
                    }
                }
            }
        }

        // Group by categoryId
        Map<Long, List<Map<?, ?>>> categoryRequestsMap = new HashMap<>();
        for (Object req : requests) {
            if (req instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) req;
                Long catId = toLong(map.get("categoryId"));
                if (catId != null) {
                    categoryRequestsMap.computeIfAbsent(catId, k -> new ArrayList<>()).add(map);
                }
            }
        }

        // 3. Generate category reports
        for (Long catId : categoryRequestsMap.keySet()) {
            CategoryAnalyticsDTO report = new CategoryAnalyticsDTO();
            report.setCategoryId(catId);

            // Fetch name
            String name = "Category " + catId;
            Object cat = categoryServiceClient.getCategoryById(catId, "ADMIN");
            if (cat instanceof Map) {
                String cName = toStringVal(((Map<?, ?>) cat).get("name"));
                if (cName != null && !cName.trim().isEmpty()) {
                    name = cName;
                }
            }
            report.setCategoryName(name);

            long totalRequests = 0;
            long completedRequests = 0;
            double totalRevenue = 0.0;

            List<Map<?, ?>> catReqs = categoryRequestsMap.getOrDefault(catId, Collections.emptyList());
            for (Map<?, ?> req : catReqs) {
                totalRequests++;
                String status = toStringVal(req.get("status"));
                if ("COMPLETED".equalsIgnoreCase(status)) {
                    completedRequests++;
                }

                Long reqId = toLong(req.get("requestId"));
                if (reqId == null) {
                    reqId = toLong(req.get("id"));
                }
                if (reqId != null) {
                    Double rev = requestRevenueMap.get(reqId);
                    if (rev != null) {
                        totalRevenue += rev;
                    }
                }
            }

            report.setTotalRequests(totalRequests);
            report.setCompletedRequests(completedRequests);
            report.setTotalRevenue(totalRevenue);

            reports.add(report);
        }

        return reports;
    }

    private Long toLong(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number number) {
            return number.longValue();
        }
        String value = obj.toString();
        return value.matches("-?\\d+") ? Long.valueOf(value) : null;
    }

    private Double toDouble(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number number) {
            return number.doubleValue();
        }
        String value = obj.toString();
        return value.matches("-?\\d+(\\.\\d+)?") ? Double.valueOf(value) : null;
    }

    private String toStringVal(Object obj) {
        return obj == null ? null : obj.toString();
    }
}