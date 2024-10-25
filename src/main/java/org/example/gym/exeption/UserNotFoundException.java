package org.example.gym.exeption;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user is not found in the system.
 */
public class UserNotFoundException extends ApplicationException {

    /**
     * Constructs a new UserNotFoundException with the specified message.
     *
     * @param message The detail message.
     */
    public UserNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
