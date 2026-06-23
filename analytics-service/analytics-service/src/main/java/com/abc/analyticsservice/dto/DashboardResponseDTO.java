package com.abc.analyticsservice.dto;

public class DashboardResponseDTO {

    private Long totalRequests;
    private Long completedRequests;
    private Long pendingRequests;
    private Long totalTechnicians;
    private Double totalRevenue;
    private Double averageRating;

    public DashboardResponseDTO() {
    }

    public Long getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(Long totalRequests) {
        this.totalRequests = totalRequests;
    }

    public Long getCompletedRequests() {
        return completedRequests;
    }

    public void setCompletedRequests(Long completedRequests) {
        this.completedRequests = completedRequests;
    }

    public Long getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(Long pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public Long getTotalTechnicians() {
        return totalTechnicians;
    }

    public void setTotalTechnicians(Long totalTechnicians) {
        this.totalTechnicians = totalTechnicians;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}