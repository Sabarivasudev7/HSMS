package com.abc.assignmentservice.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.abc.assignmentservice.dto.CategoryResponseDTO;
import com.abc.assignmentservice.enums.Roles;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CategoryClientFallback implements CategoryClient {

    @Override
    public ResponseEntity<CategoryResponseDTO> getCategoryById(Long id, Roles role) {
        log.error("category-service is unavailable. Could not fetch category with id: {}, role: {}", id, role);
        return ResponseEntity.ok(new CategoryResponseDTO(id, "Unknown", "Unknown", false));
    }
}
