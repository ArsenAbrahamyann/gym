package org.example.gym.exeption;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException {

    private final HttpStatus httpStatus;

    // Constructor that takes a message and a specific HTTP status
    public ResourceNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    // Constructor that only takes a message (defaulting to NOT_FOUND status)
    public ResourceNotFoundException(String message) {
        this(message, HttpStatus.NOT_FOUND);
    }

    // Getter for the HTTP status
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }}
