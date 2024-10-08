package org.example.gym.exeption;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(int value, String message, String format, String description) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
