package com.abc.executionservice.exception;

public class ServiceRecordNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServiceRecordNotFoundException(String message) {
        super(message);
    }
}