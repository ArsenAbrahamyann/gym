package org.example.gym.exeption;

import lombok.extern.slf4j.Slf4j;

/**
 * Exception thrown when a trainee is not found in the system.
 */
@Slf4j
public class TraineeNotFoundException extends RuntimeException {
    /**
     * Constructs a new TraineeNotFoundException with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     */
    public TraineeNotFoundException(String message) {
        super(message);
        log.error("TraineeNotFoundException: {}", message);
    }
}
