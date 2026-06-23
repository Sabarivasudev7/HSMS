package com.abc.servicerequestservice.exception;

public class InvalidRequestStatusException
extends RuntimeException {

    private static final long serialVersionUID = 1L;

public InvalidRequestStatusException(
    String message) {
super(message);
}
}
