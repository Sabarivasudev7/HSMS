package com.abc.notificationservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "assignment-service")
public interface AssignmentServiceClient {
    @GetMapping("/api/assignments/{assignmentId}")
    ResponseEntity<Object> getAssignmentById(@PathVariable Long assignmentId);
}