package com.abc.categoryservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.abc.categoryservice.dto.CategoryRequest;
import com.abc.categoryservice.dto.CategoryResponse;
import com.abc.categoryservice.dto.CategoryStatusDTO;
import com.abc.categoryservice.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private static final String ADMIN = "ADMIN";
    private static final String ACCESS_DENIED = "Access Denied";

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestHeader("X-Role") String role,
                                                           @Valid @RequestBody CategoryRequest request) {
        validateAdmin(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCategory(request));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(@RequestHeader("X-Role") String role) {
        validateAuthenticated(role);
        return ResponseEntity.ok(service.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@RequestHeader("X-Role") String role,
                                                            @PathVariable Long id) {
        validateAuthenticated(role);
        return ResponseEntity.ok(service.getCategoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@RequestHeader("X-Role") String role,
                                                           @PathVariable Long id,
                                                           @Valid @RequestBody CategoryRequest request) {
        validateAdmin(role);
        return ResponseEntity.ok(service.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@RequestHeader("X-Role") String role,
                                                 @PathVariable Long id) {
        validateAdmin(role);
        service.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CategoryResponse> updateCategoryStatus(@RequestHeader("X-Role") String role,
                                                                 @PathVariable Long id,
                                                                 @Valid @RequestBody CategoryStatusDTO statusDTO) {
        validateAdmin(role);
        return ResponseEntity.ok(service.updateCategoryStatus(id, statusDTO));
    }

    private void validateAuthenticated(String role) {
        if (role == null || role.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ACCESS_DENIED);
        }
    }

    private void validateAdmin(String role) {
        validateAuthenticated(role);
        if (!ADMIN.equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
    }
}
