package com.abc.servicerequestservice.feign;

import java.util.Collections;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import lombok.Data;

@FeignClient(name = "assignment-service", fallback = AssignmentClient.AssignmentClientFallback.class)
public interface AssignmentClient {

    @GetMapping("/api/assignments")
    ResponseEntity<List<AssignmentDto>> getAllAssignments(
        @RequestHeader("X-Role") String role
    );

    @Data
    class AssignmentDto {
        private Long assignmentId;
        private Long technicianId;
        private Long serviceRequestId;
        private String status;
    }

    @Component
    class AssignmentClientFallback implements AssignmentClient {
        @Override
        public ResponseEntity<List<AssignmentDto>> getAllAssignments(String role) {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }
}
