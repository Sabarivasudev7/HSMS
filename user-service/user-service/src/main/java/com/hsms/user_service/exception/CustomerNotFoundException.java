package com.hsms.user_service.exception;

public class CustomerNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
