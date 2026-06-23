package com.abc.assignmentservice.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abc.assignmentservice.entity.Assignment;
import com.abc.assignmentservice.enums.AssignmentStatus;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByTechnicianId(Long technicianId);
    Optional<Assignment> findByServiceRequestId(Long serviceRequestId);

    List<Assignment> findByAssignedDateBetweenAndStatusNot(LocalDateTime start, LocalDateTime end, AssignmentStatus status);
}

