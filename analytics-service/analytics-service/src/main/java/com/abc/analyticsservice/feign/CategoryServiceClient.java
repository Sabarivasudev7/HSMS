package com.abc.analyticsservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "category-service",
        url = "http://localhost:8083"
)
public interface CategoryServiceClient {

    @GetMapping("/api/categories/{id}")
    Object getCategoryById(
        @PathVariable("id") Long id,
        @RequestHeader("X-Role") String role
    );

}
