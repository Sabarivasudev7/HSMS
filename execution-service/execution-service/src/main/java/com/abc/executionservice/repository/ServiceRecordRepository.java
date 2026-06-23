package com.abc.executionservice.repository;

import com.abc.executionservice.entity.ServiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRecordRepository extends JpaRepository<ServiceRecord, Long> {

    Optional<ServiceRecord> findByServiceRequestId(Long serviceRequestId);
}