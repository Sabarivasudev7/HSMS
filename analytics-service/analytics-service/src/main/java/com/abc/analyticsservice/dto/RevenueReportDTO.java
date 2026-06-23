package com.abc.analyticsservice.dto;

public class RevenueReportDTO {

    private Double totalRevenue;
    private Long successfulPayments;
    private Long failedPayments;

    public RevenueReportDTO() {
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Long getSuccessfulPayments() {
        return successfulPayments;
    }

    public void setSuccessfulPayments(Long successfulPayments) {
        this.successfulPayments = successfulPayments;
    }

    public Long getFailedPayments() {
        return failedPayments;
    }

    public void setFailedPayments(Long failedPayments) {
        this.failedPayments = failedPayments;
    }
}