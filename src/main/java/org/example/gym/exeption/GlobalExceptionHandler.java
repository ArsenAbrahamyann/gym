package org.example.gym.exeption;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Global exception handler for managing exceptions in the application.
 * This class provides centralized handling of exceptions thrown by
 * controllers, allowing for consistent response formatting.
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Handles ValidationException and returns a response entity with the
     * appropriate HTTP status and error message.
     *
     * @param e the ValidationException to handle
     * @return ResponseEntity with the error message and HTTP status
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    /**
     * Handles ResourceNotFoundException and returns a response entity with
     * an ErrorResponse object and the appropriate HTTP status.
     *
     * @param ex the ResourceNotFoundException to handle
     * @return ResponseEntity with the ErrorResponse and HTTP status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(TraineeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTraineeNotFound(TraineeNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles generic exceptions and returns a response entity with a
     * generic error message and an internal server error status.
     *
     * @param e the Exception to handle
     * @return ResponseEntity with the error message and HTTP status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + e.getMessage());
    }
}
