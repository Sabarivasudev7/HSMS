package com.abc.executionservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.abc.executionservice.dto.ServiceRecordDetailResponseDTO;
import com.abc.executionservice.dto.ServiceRecordRequestDTO;
import com.abc.executionservice.dto.ServiceRecordResponseDTO;
import com.abc.executionservice.service.ServiceRecordService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/service-records")
@RequiredArgsConstructor
public class ServiceRecordController {

    private static final String TECHNICIAN = "TECHNICIAN";
    private static final String ADMIN = "ADMIN";
    private static final String SERVICE_MANAGER = "SERVICE_MANAGER";
    private static final String ACCESS_DENIED = "Access Denied";

    private final ServiceRecordService serviceRecordService;

    @PostMapping
    public ResponseEntity<ServiceRecordResponseDTO> createServiceRecord(@RequestHeader("X-Role") String role,
                                                                        @RequestHeader("X-User-Id") Long userId,
                                                                        @Valid @RequestBody ServiceRecordRequestDTO requestDTO) {
        requireRole(role, TECHNICIAN);
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceRecordService.createServiceRecord(requestDTO, userId));
    }

    @GetMapping
    public ResponseEntity<List<ServiceRecordDetailResponseDTO>> getAllServiceRecords(@RequestHeader("X-Role") String role) {
        requireAdminOrManager(role);
        return ResponseEntity.ok(serviceRecordService.getAllServiceRecords());
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<ServiceRecordDetailResponseDTO> getServiceRecordById(@RequestHeader("X-Role") String role,
                                                                               @PathVariable Long recordId) {
        validateAuthenticated(role);
        return ResponseEntity.ok(serviceRecordService.getServiceRecordById(recordId));
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<ServiceRecordResponseDTO> updateServiceRecord(@RequestHeader("X-Role") String role,
                                                                        @RequestHeader("X-User-Id") Long userId,
                                                                        @PathVariable Long recordId,
                                                                        @Valid @RequestBody ServiceRecordRequestDTO requestDTO) {
        requireRole(role, TECHNICIAN);
        return ResponseEntity.ok(serviceRecordService.updateServiceRecord(recordId, requestDTO, userId));
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<String> deleteServiceRecord(@RequestHeader("X-Role") String role,
                                                      @PathVariable Long recordId) {
        requireAdminOrManager(role);
        serviceRecordService.deleteServiceRecord(recordId);
        return ResponseEntity.ok("Service record deleted successfully");
    }

    private void validateAuthenticated(String role) {
        if (role == null || role.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ACCESS_DENIED);
        }
    }

    private void requireRole(String actualRole, String expectedRole) {
        validateAuthenticated(actualRole);
        if (!expectedRole.equalsIgnoreCase(actualRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
    }

    private void requireAdminOrManager(String role) {
        validateAuthenticated(role);
        if (!ADMIN.equalsIgnoreCase(role) && !SERVICE_MANAGER.equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
    }
}
