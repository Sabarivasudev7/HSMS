package com.hsms.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hsms.user_service.model.TechnicianProfileDto;
import com.hsms.user_service.service.TechnicianProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/technicians")
@RequiredArgsConstructor
public class TechnicianProfileController {

    private static final String TECHNICIAN = "TECHNICIAN";
    private static final String ACCESS_DENIED = "Access Denied";

    private final TechnicianProfileService service;

    @PostMapping
    public ResponseEntity<Object> createTechnician(
            @Valid @RequestBody TechnicianProfileDto dto,
            @RequestHeader("X-Role") String role,
            @RequestHeader("X-User-Id") Long userId) {

        if (!TECHNICIAN.equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ACCESS_DENIED);
        }

        return ResponseEntity.ok(service.createTechnician(userId, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTechnician(
            @PathVariable Long id,
            @RequestHeader("X-Role") String role,
            @RequestHeader("X-User-Id") Long userId) {

        if ("ADMIN".equals(role)
                || "SERVICE_MANAGER".equals(role)
                || (TECHNICIAN.equals(role) && userId.equals(id))) {

            return ResponseEntity.ok(service.getById(id));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ACCESS_DENIED);
    }

    @GetMapping
    public ResponseEntity<Object> getAllTechnicians(
            @RequestHeader("X-Role") String role) {

        if (!"ADMIN".equals(role) && !"SERVICE_MANAGER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ACCESS_DENIED);
        }

        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping
    public ResponseEntity<Object> updateTechnician(
            @Valid @RequestBody TechnicianProfileDto dto,
            @RequestHeader("X-Role") String role,
            @RequestHeader("X-User-Id") Long userId) {

        if (!TECHNICIAN.equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ACCESS_DENIED);
        }

        return ResponseEntity.ok(service.update(userId, dto));
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteTechnician(
            @RequestHeader("X-Role") String role,
            @RequestHeader("X-User-Id") Long userId) {

        if (!TECHNICIAN.equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ACCESS_DENIED);
        }

        service.delete(userId);
        return ResponseEntity.noContent().build();
    }
}