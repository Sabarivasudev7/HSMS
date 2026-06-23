package com.abc.analyticsservice.feign;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "assignment-service",
        url = "http://localhost:8085"
)
public interface AssignmentServiceClient {

    @GetMapping("/api/assignments")
    List<Object> getAllAssignments(@RequestHeader("X-Role") String role);

}