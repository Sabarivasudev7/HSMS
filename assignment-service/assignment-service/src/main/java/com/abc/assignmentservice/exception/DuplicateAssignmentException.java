package com.abc.assignmentservice.exception;

public class DuplicateAssignmentException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public DuplicateAssignmentException(String message) {
        super(message);
    }
}