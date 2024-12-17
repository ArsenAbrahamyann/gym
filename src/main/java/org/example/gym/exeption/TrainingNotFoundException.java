package org.example.gym.exeption;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a training is not found in the system.
 */
public class TrainingNotFoundException extends ApplicationException {

    /**
     * Constructs a new TrainingNotFoundException with the specified message.
     *
     * @param message The detail message.
     */
    public TrainingNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
