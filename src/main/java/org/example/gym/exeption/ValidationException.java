package org.example.gym.exeption;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {
    private final HttpStatus httpStatus;

    // Constructor that takes a message and a specific HTTP status
    public ValidationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    // Constructor that only takes a message (defaulting to BAD_REQUEST status)
    public ValidationException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    // Getter for the HTTP status
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
