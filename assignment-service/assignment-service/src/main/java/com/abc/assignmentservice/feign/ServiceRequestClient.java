package com.abc.assignmentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.abc.assignmentservice.dto.ServiceRequestDetailResponseDTO;
import com.abc.assignmentservice.enums.Roles;

@FeignClient(name = "service-request-service", fallbackFactory = ServiceRequestClientFallbackFactory.class)
public interface ServiceRequestClient {

    @GetMapping("/api/service-requests/{id}")
    ResponseEntity<ServiceRequestDetailResponseDTO> getRequestById(
        @PathVariable("id") Long id,
        @RequestHeader("X-Role") Roles role
    );

    @PatchMapping("/api/service-requests/{id}/status")
    ResponseEntity<ServiceRequestDetailResponseDTO> updateStatus(
        @PathVariable("id") Long id,
        @RequestParam("status") String status,
        @RequestHeader("X-Role") Roles role
    );
}
