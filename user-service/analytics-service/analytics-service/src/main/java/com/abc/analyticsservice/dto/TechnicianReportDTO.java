package com.abc.analyticsservice.dto;

public class TechnicianReportDTO {

    private Long technicianId;
    private String technicianName;
    private Long assignedJobs;
    private Long completedJobs;
    private Double averageRating;

    public TechnicianReportDTO() {
    }

    public Long getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public Long getAssignedJobs() {
        return assignedJobs;
    }

    public void setAssignedJobs(Long assignedJobs) {
        this.assignedJobs = assignedJobs;
    }

    public Long getCompletedJobs() {
        return completedJobs;
    }

    public void setCompletedJobs(Long completedJobs) {
        this.completedJobs = completedJobs;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}