package com.abc.servicerequestservice.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.abc.servicerequestservice.entity.ServiceRequest;

public interface ServiceRequestRepository
        extends JpaRepository<ServiceRequest, Long> {

	Page<ServiceRequest> findByCustomerId(
	        Long customerId,
	        Pageable pageable);
}