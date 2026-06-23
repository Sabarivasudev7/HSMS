package com.abc.notificationservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-request-service")
public interface ServiceRequestClient {
    @GetMapping("/api/service-requests/{requestId}")
    ResponseEntity<Object> getServiceRequestById(@PathVariable Long requestId);
}
