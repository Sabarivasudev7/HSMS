package com.abc.executionservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.abc.executionservice.dto.ServiceRequestResponseDTO;

@FeignClient(name = "service-request-service", fallback = ServiceRequestClientFallback.class)
public interface ServiceRequestClient {

    @GetMapping("/api/service-requests/{requestId}")
    ServiceRequestResponseDTO getServiceRequestById(
            @RequestHeader("X-Role") String role,
            @PathVariable("requestId") Long requestId);

    @PatchMapping("/api/service-requests/{requestId}/status")
    ServiceRequestResponseDTO updateServiceRequestStatus(
            @RequestHeader("X-Role") String role,
            @PathVariable("requestId") Long requestId,
            @RequestParam("status") String status);
}