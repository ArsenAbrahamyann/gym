package org.example.gym.exeption;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a training type is not found in the system.
 */
public class TrainingTypeNotFoundException extends ApplicationException {

    /**
     * Constructs a new TrainingTypeNotFoundException with the specified message.
     *
     * @param message The detail message.
     */
    public TrainingTypeNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
