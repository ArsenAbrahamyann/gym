package org.example.gym.exeption;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an unauthorized access attempt is made.
 * <p>
 * This exception is a runtime exception that indicates that a user
 * has attempted to access a resource or perform an action without
 * the necessary permissions.
 * </p>
 */
public class UnauthorizedException  extends ApplicationException {

    /**
     * Constructs a new UnauthorizedException with the specified detail message.
     *
     * @param message The detail message.
     */
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
