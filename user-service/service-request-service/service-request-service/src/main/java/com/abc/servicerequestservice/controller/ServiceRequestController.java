package com.abc.servicerequestservice.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.abc.servicerequestservice.dto.ServiceRequestDetailResponse;
import com.abc.servicerequestservice.dto.ServiceRequestRequest;
import com.abc.servicerequestservice.dto.ServiceRequestUpdateDTO;
import com.abc.servicerequestservice.enums.RequestStatus;
import com.abc.servicerequestservice.service.ServiceRequestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/service-requests")
@RequiredArgsConstructor
public class ServiceRequestController {

    private static final String ADMIN = "ADMIN";
    private static final String CUSTOMER = "CUSTOMER";
    private static final String TECHNICIAN = "TECHNICIAN";
    private static final String SERVICE_MANAGER = "SERVICE_MANAGER";
    private static final String ACCESS_DENIED = "Access Denied";

    private final ServiceRequestService service;

    @PostMapping
    public ResponseEntity<ServiceRequestDetailResponse> createRequest(@RequestHeader("X-Role") String role,
                                                                      @RequestHeader("X-User-Id") Long userId,
                                                                      @Valid @RequestBody ServiceRequestRequest request) {
        requireRole(role, CUSTOMER);
        request.setCustomerId(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRequest(request));
    }

    @GetMapping
    public ResponseEntity<List<ServiceRequestDetailResponse>> getAllRequests(@RequestHeader("X-Role") String role) {
        requireAdminOrManager(role);
        return ResponseEntity.ok(service.getAllRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequestDetailResponse> getRequestById(@RequestHeader("X-Role") String role,
                                                                       @RequestHeader("X-User-Id") Long loggedInUserId,
                                                                       @PathVariable Long id) {
        ServiceRequestDetailResponse response = service.getRequestById(id);
        if (CUSTOMER.equalsIgnoreCase(role) && !response.getCustomerId().equals(loggedInUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<ServiceRequestDetailResponse>> getRequestsByCustomer(@RequestHeader("X-Role") String role,
                                                                                   @RequestHeader("X-User-Id") Long loggedInUserId,
                                                                                   @PathVariable Long customerId,
                                                                                   @RequestParam(defaultValue = "0") int page,
                                                                                   @RequestParam(defaultValue = "5") int size) {
        if (CUSTOMER.equalsIgnoreCase(role) && !customerId.equals(loggedInUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.getRequestsByCustomer(customerId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceRequestDetailResponse> updateRequest(@RequestHeader("X-Role") String role,
                                                                      @RequestHeader("X-User-Id") Long loggedInUserId,
                                                                      @PathVariable Long id,
                                                                      @Valid @RequestBody ServiceRequestUpdateDTO dto) {
        requireRole(role, CUSTOMER);
        ServiceRequestDetailResponse existing = service.getRequestById(id);
        if (!existing.getCustomerId().equals(loggedInUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
        return ResponseEntity.ok(service.updateRequest(id, dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ServiceRequestDetailResponse> updateStatus(@RequestHeader("X-Role") String role,
                                                                     @PathVariable Long id,
                                                                     @RequestParam RequestStatus status) {
        if (!ADMIN.equalsIgnoreCase(role) && !TECHNICIAN.equalsIgnoreCase(role) && !SERVICE_MANAGER.equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
        return ResponseEntity.ok(service.updateRequestStatus(id, status));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<String> cancelRequest(@RequestHeader("X-Role") String role,
                                                @RequestHeader("X-User-Id") Long loggedInUserId,
                                                @PathVariable Long id) {
        requireRole(role, CUSTOMER);
        ServiceRequestDetailResponse existing = service.getRequestById(id);
        if (!existing.getCustomerId().equals(loggedInUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
        service.cancelRequest(id);
        return ResponseEntity.ok("Request cancelled successfully");
    }

    private void requireRole(String actualRole, String expectedRole) {
        if (!expectedRole.equalsIgnoreCase(actualRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
    }

    private void requireAdminOrManager(String role) {
        if (!ADMIN.equalsIgnoreCase(role) && !SERVICE_MANAGER.equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
    }
}
