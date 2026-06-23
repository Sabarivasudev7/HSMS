package com.abc.feedbackservice.exception;

public class FeedbackAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public FeedbackAlreadyExistsException(String message) {

		super(message);
	}

}
