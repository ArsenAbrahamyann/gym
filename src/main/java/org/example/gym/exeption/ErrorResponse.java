package org.example.gym.exeption;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * Represents an error response structure that includes the error message, HTTP status,
 * timestamp, and request path.
 */
@Getter
@Setter
public class ErrorResponse {
    private String message;
    private HttpStatus status;
    private LocalDateTime timestamp;

    /**
     * Constructs an ErrorResponse with specified details.
     *
     * @param message The error message.
     * @param status  The HTTP status of the error.
     * @param path    The request path where the error occurred.
     */
    public ErrorResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}
