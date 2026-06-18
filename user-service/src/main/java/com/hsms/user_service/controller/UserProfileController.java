package com.hsms.user_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hsms.user_service.exception.UserNotFoundException;
import com.hsms.user_service.model.UserProfileDto;
import com.hsms.user_service.model.UserRegistrationDto;
import com.hsms.user_service.service.UserProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService service;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRegistrationDto dto) {
        service.createUserFromAuth(dto);
        return ResponseEntity.ok("User Profile Created");
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserProfileDto>> getAllUsers() {
        return ResponseEntity.ok(service.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
