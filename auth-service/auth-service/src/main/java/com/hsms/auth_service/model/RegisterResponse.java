package com.hsms.auth_service.model;

public class RegisterResponse {

    private Long userId;
    private String message;

    public RegisterResponse(Long userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }
}