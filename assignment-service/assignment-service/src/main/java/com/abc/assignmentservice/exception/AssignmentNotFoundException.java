package com.abc.assignmentservice.exception;

public class AssignmentNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public AssignmentNotFoundException(String message) {
        super(message);
    }
}