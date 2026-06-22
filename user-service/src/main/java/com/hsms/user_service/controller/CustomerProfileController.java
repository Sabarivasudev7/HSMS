package com.hsms.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hsms.user_service.exception.CustomerNotFoundException;
import com.hsms.user_service.model.CustomerProfileDto;
import com.hsms.user_service.service.CustomerProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerProfileController {

    private static final String CUSTOMER = "CUSTOMER";
    private static final String ACCESS_DENIED = "Access Denied";

    private final CustomerProfileService service;

    @PostMapping
    public ResponseEntity<Object> createCustomer(
            @Valid @RequestBody CustomerProfileDto dto,
            @RequestHeader("X-Role") String role,
            @RequestHeader("X-User-Id") Long userId) {

        if (!CUSTOMER.equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ACCESS_DENIED);
        }

        return ResponseEntity.ok(service.createCustomer(userId, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomer(
            @PathVariable Long id,
            @RequestHeader("X-Role") String role,
            @RequestHeader("X-User-Id") Long userId) {

        if ("ADMIN".equals(role)
                || "SERVICE_MANAGER".equals(role)
                || (CUSTOMER.equals(role) && userId.equals(id))) {

            return ResponseEntity.ok(service.getById(id));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ACCESS_DENIED);
    }

    @GetMapping
    public ResponseEntity<Object> getAllCustomers(
            @RequestHeader("X-Role") String role) {

        if (!"ADMIN".equals(role) && !"SERVICE_MANAGER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ACCESS_DENIED);
        }

        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping
    public ResponseEntity<Object> updateCustomer(
            @Valid @RequestBody CustomerProfileDto dto,
            @RequestHeader("X-Role") String role,
            @RequestHeader("X-User-Id") Long userId) {

        if (!CUSTOMER.equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ACCESS_DENIED);
        }

        return ResponseEntity.ok(service.update(userId, dto));
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteCustomer(
            @RequestHeader("X-Role") String role,
            @RequestHeader("X-User-Id") Long userId) {

        if (!CUSTOMER.equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ACCESS_DENIED);
        }

        service.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFound(CustomerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}