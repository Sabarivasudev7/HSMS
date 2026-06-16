package com.hsms.user_service.controller;

import java.util.List;

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

    @PostMapping("/{id}")
    public ResponseEntity<TechnicianProfileDto> createTechnician(@PathVariable Long id,@Valid @RequestBody TechnicianProfileDto dto) {
        return ResponseEntity.ok(service.createTechnician(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnicianProfileDto> getTechnician(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<TechnicianProfileDto>> getAllTechnicians() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TechnicianProfileDto> updateTechnician(@PathVariable Long id,
                                                                 @Valid @RequestBody TechnicianProfileDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnician(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
