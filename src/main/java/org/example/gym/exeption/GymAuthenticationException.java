package org.example.gym.exeption;


/**
 * Custom exception class to handle authentication errors within the gym application.
 *
 * <p>This exception is thrown when there is an authentication failure, such as invalid credentials
 * or a revoked token. It extends {@link RuntimeException}, meaning it is an unchecked exception and
 * does not need to be explicitly declared in method signatures.</p>
 *
 * @author [Your Name]
 */
public class GymAuthenticationException extends RuntimeException {

    /**
     * Constructs a new {@link GymAuthenticationException} with the specified detail message.
     *
     * <p>This message will be included in the exception when it is thrown, providing details about
     * the authentication error.</p>
     *
     * @param message the detail message explaining the reason for the exception
     */
    public GymAuthenticationException(String message) {
        super(message);
    }
}
