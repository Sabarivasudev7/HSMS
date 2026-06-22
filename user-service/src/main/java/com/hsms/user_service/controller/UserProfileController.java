package com.hsms.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hsms.user_service.exception.UserNotFoundException;
import com.hsms.user_service.model.UserRegistrationDto;
import com.hsms.user_service.service.UserProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserProfileController {

    private static final String ADMIN = "ADMIN";
    private static final String ACCESS_DENIED = "Access Denied";

    private final UserProfileService service;

    @PostMapping
    public ResponseEntity<String> createUser(
            @RequestBody UserRegistrationDto dto,
            @RequestHeader("X-Role") String role) {

        service.createUserFromAuth(dto);
        return ResponseEntity.ok("User Profile Created");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(
            @PathVariable Long id,
            @RequestHeader("X-Role") String role) {

        if (!ADMIN.equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ACCESS_DENIED);
        }

        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers(
            @RequestHeader("X-Role") String role) {

        if (!ADMIN.equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ACCESS_DENIED);
        }

        return ResponseEntity.ok(service.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(
            @PathVariable Long id,
            @RequestHeader("X-Role") String role) {

        if (!ADMIN.equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ACCESS_DENIED);
        }

        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}