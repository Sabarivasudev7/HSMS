package com.hsms.execution_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hsms.execution_service.entity.Execution;


@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Long>{
	
    Optional<Execution> findByServiceRequestId(Long serviceRequestId);

    
}
