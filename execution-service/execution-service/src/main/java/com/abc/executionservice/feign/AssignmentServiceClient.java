package com.abc.executionservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "assignment-service", fallback = AssignmentServiceClientFallback.class)
public interface AssignmentServiceClient {

    @GetMapping("/api/assignments/technician/{technicianId}")
    Object getAssignmentByTechnicianId(@PathVariable("technicianId") Long technicianId);
}