package com.abc.feedbackservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abc.feedbackservice.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

	List<Feedback> findAllByServiceRequestId(Long serviceRequestId);

	Optional<Feedback> findFirstByServiceRequestId(Long serviceRequestId);

}
