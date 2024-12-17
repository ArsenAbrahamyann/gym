package org.example.gym.exeption;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a trainer is not found in the system.
 */
public class TrainerNotFoundException extends ApplicationException {

    /**
     * Constructs a new TrainerNotFoundException with the specified message.
     *
     * @param message The detail message.
     */
    public TrainerNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
