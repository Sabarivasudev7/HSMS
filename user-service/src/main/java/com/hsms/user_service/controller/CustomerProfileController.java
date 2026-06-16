package com.hsms.user_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hsms.user_service.model.CustomerProfileDto;
import com.hsms.user_service.service.CustomerProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerProfileController {

    private final CustomerProfileService service;

    @PostMapping("/{id}")
    public ResponseEntity<CustomerProfileDto> createCustomer(@PathVariable Long id,@Valid @RequestBody CustomerProfileDto dto) {
        return ResponseEntity.ok(service.createCustomer(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerProfileDto> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerProfileDto>> getAllCustomers() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerProfileDto> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerProfileDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
