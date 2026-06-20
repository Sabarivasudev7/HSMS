package com.hsms.user_service.exception;

public class RoleMismatchException extends RuntimeException {
    public RoleMismatchException(String message) {
        super(message);
    }
}
