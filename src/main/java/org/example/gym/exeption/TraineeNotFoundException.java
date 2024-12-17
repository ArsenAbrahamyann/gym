package org.example.gym.exeption;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a trainee is not found in the system.
 */
public class TraineeNotFoundException extends ApplicationException {

    /**
     * Constructs a new TraineeNotFoundException with the specified detail message.
     *
     * @param message The detail message.
     */
    public TraineeNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
