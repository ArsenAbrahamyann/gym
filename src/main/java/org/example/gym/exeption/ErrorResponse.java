package org.example.gym.exeption;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents an error response returned to the client.
 */
@Getter
@Setter
@Slf4j
public class ErrorResponse {
    private String message;
    private LocalDateTime timestamp;

    /**
     * Constructs a new ErrorResponse.
     *
     * @param value       The HTTP status code value.
     * @param message     The error message to be returned.
     * @param format      A format string for additional error information (currently unused).
     * @param description A detailed description of the error (currently unused).
     */
    public ErrorResponse(int value, String message, String format, String description) {
        this.message = message;
        this.timestamp = LocalDateTime.now();

        log.error("Error occurred: HTTP Status Code {} - {}", value, message);
    }
}
