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

    private final TechnicianProfileService service;

   
    @PostMapping
    public ResponseEntity<?> createTechnician(
            @Valid @RequestBody TechnicianProfileDto dto,
            @RequestHeader("X-Role") String role,
            @RequestHeader("X-User-Id") Long userId) {

        if (!"TECHNICIAN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        return ResponseEntity.ok(service.createTechnician(userId, dto));
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<?> getTechnician(
            @PathVariable Long id,
            @RequestHeader("X-Role") String role) {

        if (!"ADMIN".equals(role) && !"SERVICE_MANAGER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        return ResponseEntity.ok(service.getById(id));
    }

   
    @GetMapping
    public ResponseEntity<?> getAllTechnicians(@RequestHeader("X-Role") String role) {
        if (!"ADMIN".equals(role) && !"SERVICE_MANAGER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        return ResponseEntity.ok(service.getAll());
    }

    
    @PutMapping
    public ResponseEntity<?> updateTechnician(
            @Valid @RequestBody TechnicianProfileDto dto,
            @RequestHeader("X-Role") String role,
            @RequestHeader("X-User-Id") Long userId) {

        if (!"TECHNICIAN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        return ResponseEntity.ok(service.update(userId, dto));
    }

    
    @DeleteMapping
    public ResponseEntity<?> deleteTechnician(
            @RequestHeader("X-Role") String role,
            @RequestHeader("X-User-Id") Long userId) {

        if (!"TECHNICIAN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        service.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
