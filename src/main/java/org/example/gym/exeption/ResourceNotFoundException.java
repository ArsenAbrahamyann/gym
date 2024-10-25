package org.example.gym.exeption;

import org.springframework.http.HttpStatus;

/**
 * Custom exception class to represent a resource not found scenario.
 */
public class ResourceNotFoundException extends ApplicationException {

    /**
     * Constructs a new ResourceNotFoundException with a specified message.
     *
     * @param message The detail message.
     */
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    /**
     * Constructs a new ResourceNotFoundException with a specified message.
     *
     * @param message The detail message.
     * @param httpStatus The detail message.
     */
    public ResourceNotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
