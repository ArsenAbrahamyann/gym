package org.example.gym.exeption;

/**
 * Exception thrown when an unauthorized access attempt is made.
 * <p>
 * This exception is a runtime exception that indicates that a user
 * has attempted to access a resource or perform an action without
 * the necessary permissions.
 * </p>
 */
public class UnauthorizedException  extends RuntimeException {

    /**
     * Constructs a new {@code UnauthorizedException} with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval
     *                by the {@link Throwable#getMessage()} method
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
