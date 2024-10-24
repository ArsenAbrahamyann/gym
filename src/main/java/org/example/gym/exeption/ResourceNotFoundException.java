package org.example.gym.exeption;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * Custom exception class to represent a resource not found scenario.
 */
@Getter
@Slf4j
public class ResourceNotFoundException extends RuntimeException {

    /**
     * -- GETTER --
     *  Getter for the HTTP status.
     *
     * @return the HTTP status associated with the exception
     */
    private final HttpStatus httpStatus;

    /**
     * Constructor that takes a message and a specific HTTP status.
     *
     * @param message   the detail message
     * @param httpStatus the HTTP status associated with the exception
     */
    public ResourceNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        logError(message, httpStatus);
    }

    /**
     * Constructor that only takes a message, defaulting to NOT_FOUND status.
     *
     * @param message the detail message
     */
    public ResourceNotFoundException(String message) {
        this(message, HttpStatus.NOT_FOUND);
    }

    /**
     * Logs the error message and HTTP status.
     *
     * @param message   the detail message
     * @param httpStatus the HTTP status associated with the exception
     */
    private void logError(String message, HttpStatus httpStatus) {
        log.error("ResourceNotFoundException: {}, HTTP Status: {}", message, httpStatus);
    }
}
