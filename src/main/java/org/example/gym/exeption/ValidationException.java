package org.example.gym.exeption;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * Custom exception class to handle validation errors in the application.
 */
@Getter
@Slf4j
public class ValidationException extends RuntimeException {
    /**
     * -- GETTER --
     *  Getter for the HTTP status.
     *
     * @return The HTTP status associated with this exception.
     */
    private final HttpStatus httpStatus;

    /**
     * Constructor that takes a message and a specific HTTP status.
     *
     * @param message   The error message associated with this exception.
     * @param httpStatus The HTTP status to be returned with this exception.
     */
    public ValidationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        log.error("ValidationException: {} - HTTP Status: {}", message, httpStatus);
    }

    /**
     * Constructor that only takes a message, defaulting to BAD_REQUEST status.
     *
     * @param message The error message associated with this exception.
     */
    public ValidationException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
        log.error("ValidationException: {} - HTTP Status: {}", message, httpStatus);
    }

}
