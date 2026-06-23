package com.hsms.user_service.exception;

public class RoleMismatchException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public RoleMismatchException(String message) {
        super(message);
    }
}
