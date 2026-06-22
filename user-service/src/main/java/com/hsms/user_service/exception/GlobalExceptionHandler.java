package com.hsms.user_service.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String MESSAGE = "message";

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCustomerNotFound(CustomerNotFoundException ex) {
        Map<String, String> err = new HashMap<>();
        err.put(MESSAGE, ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TechnicianNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTechnicianNotFound(TechnicianNotFoundException ex) {
        Map<String, String> err = new HashMap<>();
        err.put(MESSAGE, ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleMismatchException.class)
    public ResponseEntity<Map<String, String>> handleRoleMismatch(RoleMismatchException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Role Mismatch");
        response.put(MESSAGE, ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(io.github.resilience4j.ratelimiter.RequestNotPermitted.class)
    public ResponseEntity<Map<String, String>> handleRateLimiter(Exception ex) {
        Map<String, String> err = new HashMap<>();
        err.put("error", "Too many requests! Please calm down and try again later.");
        return new ResponseEntity<>(err, HttpStatus.TOO_MANY_REQUESTS);
    }
}