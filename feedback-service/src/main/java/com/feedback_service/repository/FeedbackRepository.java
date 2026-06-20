package com.feedback_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feedback_service.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByServiceRequestId(Long serviceRequestId);
}