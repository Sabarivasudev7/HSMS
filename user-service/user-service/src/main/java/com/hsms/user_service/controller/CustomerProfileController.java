package com.hsms.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.hsms.user_service.model.CustomerProfileDto;
import com.hsms.user_service.service.CustomerProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerProfileController {

    private static final String ADMIN = "ADMIN";
    private static final String SERVICE_MANAGER = "SERVICE_MANAGER";
    private static final String CUSTOMER = "CUSTOMER";
    private static final String ACCESS_DENIED = "Access Denied";

    private final CustomerProfileService service;

    @PostMapping
    public ResponseEntity<Object> createCustomer(@Valid @RequestBody CustomerProfileDto dto,
                                                 @RequestHeader("X-Role") String role,
                                                 @RequestHeader("X-User-Id") Long userId) {
        requireRole(role, CUSTOMER);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCustomer(userId, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomer(@PathVariable Long id,
                                              @RequestHeader("X-Role") String role,
                                              @RequestHeader("X-User-Id") Long userId) {
        if (!isAdminOrManager(role) && !(CUSTOMER.equalsIgnoreCase(role) && userId.equals(id))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<Object> getAllCustomers(@RequestHeader("X-Role") String role) {
        requireAdminOrManager(role);
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping
    public ResponseEntity<Object> updateCustomer(@Valid @RequestBody CustomerProfileDto dto,
                                                 @RequestHeader("X-Role") String role,
                                                 @RequestHeader("X-User-Id") Long userId) {
        requireRole(role, CUSTOMER);
        return ResponseEntity.ok(service.update(userId, dto));
    }

    private void requireRole(String actualRole, String expectedRole) {
        if (!expectedRole.equalsIgnoreCase(actualRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
    }

    private void requireAdminOrManager(String role) {
        if (!isAdminOrManager(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
    }

    private boolean isAdminOrManager(String role) {
        return ADMIN.equalsIgnoreCase(role) || SERVICE_MANAGER.equalsIgnoreCase(role);
    }
}
