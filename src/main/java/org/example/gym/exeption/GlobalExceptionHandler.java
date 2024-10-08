package org.example.gym.exeption;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for managing exceptions in the application.
 * This class provides centralized handling of exceptions thrown by
 * controllers, allowing for consistent response formatting.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles validation exceptions and returns a response entity with the error message.
     *
     * @param e the validation exception
     * @return response entity with error message and appropriate HTTP status
     */
    @ExceptionHandler(org.example.gym.exeption.ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException e) {
        log.warn("Validation error occurred: {}", e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    /**
     * Handles resource not found exceptions and returns a response entity with the error details.
     *
     * @param ex the resource not found exception
     * @param request the web request
     * @return response entity with error details and NOT_FOUND status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                               WebRequest request) {
        log.error("Resource not found: {}", ex.getMessage());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());

        // Timestamp as an array of integers
        LocalDateTime now = LocalDateTime.now();
        errorResponse.put("timestamp", new int[]{
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                now.getHour(),
                now.getMinute(),
                now.getSecond(),
                now.getNano() // nanoseconds for precision
        });

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles trainee not found exceptions and returns a response entity with the error details.
     *
     * @param ex the trainee not found exception
     * @return response entity with error details and NOT_FOUND status
     */
    @ExceptionHandler(org.example.gym.exeption.TraineeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTraineeNotFound(TraineeNotFoundException ex) {
        log.error("Trainee not found: {}", ex.getMessage());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());

        // Timestamp as an array of integers
        LocalDateTime now = LocalDateTime.now();
        errorResponse.put("timestamp", new int[]{
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                now.getHour(),
                now.getMinute(),
                now.getSecond(),
                now.getNano()
        });

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles general exceptions and returns a response entity with an error message.
     *
     * @param e the general exception
     * @return response entity with an unexpected error message and INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        log.error("An unexpected error occurred: {}", e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + e.getMessage());
    }
}
