package com.hsms.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hsms.user_service.model.UserRegistrationDto;
import com.hsms.user_service.service.UserProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserProfileController {

    private static final String ADMIN = "ADMIN";
    private static final String SERVICE_MANAGER = "SERVICE_MANAGER";
    private static final String INTERNAL = "INTERNAL";
    private static final String ACCESS_DENIED = "Access Denied";

    private final UserProfileService service;

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRegistrationDto dto,
                                             @RequestHeader("X-Role") String role) {
        if (!INTERNAL.equalsIgnoreCase(role) && !ADMIN.equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
        service.createUserFromAuth(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User profile created");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id,
                                          @RequestHeader("X-Role") String role) {
        validateAdminOrManager(role);
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers(@RequestHeader("X-Role") String role) {
        validateAdminOrManager(role);
        return ResponseEntity.ok(service.getAll());
    }

    private void validateAdminOrManager(String role) {
        if (!ADMIN.equalsIgnoreCase(role) && !SERVICE_MANAGER.equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
    }
}
