package org.example.gym.exeption;

import org.springframework.http.HttpStatus;

/**
 * Custom exception class to handle validation errors in the application.
 */
public class ValidationException extends ApplicationException {

    /**
     * Constructs a new ValidationException with the specified message.
     *
     * @param message The detail message.
     */
    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
