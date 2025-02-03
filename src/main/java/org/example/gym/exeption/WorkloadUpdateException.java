package org.example.gym.exeption;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user is not found in the system.
 */
public class WorkloadUpdateException extends ApplicationException {

    /**
     * Constructs a new WorkloadUpdateException with the specified message.
     *
     * @param message The detail message.
     */
    public WorkloadUpdateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
