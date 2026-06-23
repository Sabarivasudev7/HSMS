package com.abc.assignmentservice.exception;

public class TechnicianNotAvailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public TechnicianNotAvailableException(String message) {
        super(message);
    }
}