package org.example.gym.exeption;

import java.security.NoSuchAlgorithmException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

/**
 * Centralized exception handler for handling exceptions globally in the application.
 * Provides consistent error response formatting for various exception types.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles ValidationException and returns an appropriate response.
     *
     * @param e       The exception that occurred.
     * @param request The web request information.
     * @return Response entity containing the error response.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e, WebRequest request) {
        log.warn("Validation error occurred: {}", e.getMessage());
        return buildErrorResponse(e.getMessage(), e.getHttpStatus(), request);
    }

    /**
     * Builds the error response based on the exception details and request information.
     *
     * @param message The error message.
     * @param status  The HTTP status of the error.
     * @param request The web request information.
     * @return Response entity containing the formatted error response.
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        ErrorResponse errorResponse = new ErrorResponse(message, status, path);
        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Handles unauthorized access exceptions, which occur when a user attempts to access
     * a resource without proper authorization.
     *
     * @param ex The {@link UnauthorizedException} representing an unauthorized access attempt.
     * @param request The {@link WebRequest} information for the current request.
     * @return A {@link ResponseEntity} with a structured {@link ErrorResponse} indicating unauthorized access.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        log.warn("Unauthorized access attempt: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> handleLockedException(LockedException ex, WebRequest request) {
        log.warn("User is blocked for 5 minutes due to multiple failed login attempts: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        log.warn("Invalid credentials: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, request);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, WebRequest request) {
        log.warn("Invalid credentials: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchAlgorithmException(NoSuchAlgorithmException ex, WebRequest request) {
        log.warn("No Algorithm: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Handles cases where a specific trainee cannot be found in the system.
     *
     * @param ex The {@link TraineeNotFoundException} representing a missing trainee.
     * @param request The {@link WebRequest} information for the current request.
     * @return A {@link ResponseEntity} containing an {@link ErrorResponse} with a 404 status code.
     */
    @ExceptionHandler(TraineeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTraineeNotFound(TraineeNotFoundException ex, WebRequest request) {
        log.error("Trainee not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handles cases where a specific trainer cannot be found in the system.
     *
     * @param ex The {@link TrainerNotFoundException} representing a missing trainer.
     * @param request The {@link WebRequest} information for the current request.
     * @return A {@link ResponseEntity} containing an {@link ErrorResponse} with a 404 status code.
     */
    @ExceptionHandler(TrainerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTrainerNotFound(TrainerNotFoundException ex, WebRequest request) {
        log.error("Trainer not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handles cases where a specific training session cannot be found in the system.
     *
     * @param ex The {@link TrainingNotFoundException} representing a missing training session.
     * @param request The {@link WebRequest} information for the current request.
     * @return A {@link ResponseEntity} containing an {@link ErrorResponse} with a 404 status code.
     */
    @SneakyThrows
    @ExceptionHandler(TrainingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTrainingNotFound(TrainingNotFoundException ex, WebRequest request) {
        log.error("Training not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handles cases where a specific user cannot be found in the system.
     *
     * @param ex The {@link UserNotFoundException} representing a missing user.
     * @param request The {@link WebRequest} information for the current request.
     * @return A {@link ResponseEntity} containing an {@link ErrorResponse} with a 404 status code.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, WebRequest request) {
        log.error("User not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }


    /**
     * Handles general application-level exceptions, providing a consistent response format.
     *
     * @param e The {@link ApplicationException} thrown within the application.
     * @param request The {@link WebRequest} information for the current request.
     * @return A {@link ResponseEntity} with a standardized {@link ErrorResponse}.
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e, WebRequest request) {
        log.error("An Application error occurred: {}", e.getMessage(), e);
        return buildErrorResponse("An Application error occurred", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Handles general, unexpected exceptions, providing a fallback error response.
     *
     * @param e The {@link Exception} that occurred unexpectedly.
     * @param request The {@link WebRequest} information for the current request.
     * @return A {@link ResponseEntity} with a standardized {@link ErrorResponse}.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e, WebRequest request) {
        log.error("An unexpected error occurred: {}", e.getMessage(), e);
        return buildErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


}
