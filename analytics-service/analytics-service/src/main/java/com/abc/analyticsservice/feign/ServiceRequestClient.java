package com.abc.analyticsservice.feign;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "service-request-service",
        url = "http://localhost:8084"
)
public interface ServiceRequestClient {

    @GetMapping("/api/service-requests")
    List<Object> getAllServiceRequests(@RequestHeader("X-Role") String role);

}