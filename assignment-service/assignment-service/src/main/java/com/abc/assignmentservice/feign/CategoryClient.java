package com.abc.assignmentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.abc.assignmentservice.dto.CategoryResponseDTO;
import com.abc.assignmentservice.enums.Roles;

@FeignClient(name = "category-service", fallback = CategoryClientFallback.class)
public interface CategoryClient {

    @GetMapping("/api/categories/{id}")
    ResponseEntity<CategoryResponseDTO> getCategoryById(
        @PathVariable("id") Long id,
        @RequestHeader("X-Role") Roles role
    );
}
