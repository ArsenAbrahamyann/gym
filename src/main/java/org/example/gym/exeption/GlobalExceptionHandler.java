package org.example.gym.exeption;

import java.security.NoSuchAlgorithmException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
     * @param e The exception that occurred.
     * @return Response entity containing the error response.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
        log.warn("Validation error occurred: {}", e.getMessage());
        return buildErrorResponse("Validation error occurred", e.getHttpStatus());
    }

    /**
     * Builds the error response based on the exception details and request information.
     *
     * @param message The error message.
     * @param status  The HTTP status of the error.
     * @return Response entity containing the formatted error response.
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(message, status);
        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Handles unauthorized access exceptions, which occur when a user attempts to access
     * a resource without proper authorization.
     *
     * @param ex The {@link UnauthorizedException} representing an unauthorized access attempt.
     * @return A {@link ResponseEntity} with a structured {@link ErrorResponse} indicating unauthorized access.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        log.warn("Unauthorized access attempt: {}", ex.getMessage());
        return buildErrorResponse("Unauthorized access attempt", HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles GymAuthenticationException by logging the error and returning an appropriate error response.
     *
     * <p>This method is invoked when a {@link GymAuthenticationException} is thrown during the authentication process.
     * It logs the details of the unauthorized access attempt and returns a response with the specified error message
     * and the HTTP status code.</p>
     *
     * @param ex the exception that was thrown
     * @return a {@link ResponseEntity} containing an error response with the exception details and HTTP status
     */
    @ExceptionHandler(GymAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleGymAuthenticationException(GymAuthenticationException ex) {
        log.error("Authentication error: Unauthorized access attempt. Message: {}", ex.getMessage(), ex);
        return buildErrorResponse("Authentication error: Unauthorized access attempt. Message",
                HttpStatus.FORBIDDEN);
    }

    /**
     * Handles the LockedException when a user is blocked due to multiple failed login attempts.
     * Logs the warning and returns a standardized error response with a 401 Unauthorized status.
     *
     * @param ex the exception thrown when a user is blocked
     * @return a ResponseEntity containing the error details and status code
     */
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> handleLockedException(LockedException ex) {
        log.warn("User is blocked for 5 minutes due to multiple failed login attempts: {}", ex.getMessage());
        return buildErrorResponse("User is blocked for 5 minutes due to multiple failed login attempts",
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.warn("invalid method argument: {}", ex.getMessage());
        return buildErrorResponse("invalid method argument", HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles the ResponseStatusException when the response status is set to a specific status code,
     * typically indicating invalid credentials or other authorization issues.
     * Logs the warning and returns a standardized error response with a 401 Unauthorized status.
     *
     * @param ex the exception thrown due to invalid status or authorization issue
     * @return a ResponseEntity containing the error details and status code
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        log.warn("Invalid credentials: {}", ex.getMessage());
        return buildErrorResponse("Invalid credentials", HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles the BadCredentialsException when invalid credentials are provided during authentication.
     * Logs the warning and returns a standardized error response with a 401 Unauthorized status.
     *
     * @param ex the exception thrown when invalid credentials are provided
     * @return a ResponseEntity containing the error details and status code
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Invalid credentials: {}", ex.getMessage());
        return buildErrorResponse("Invalid credentials", HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles the NoSuchAlgorithmException when an unsupported algorithm is used for encryption or hashing.
     * Logs the warning and returns a standardized error response with a 400 Bad Request status.
     *
     * @param ex the exception thrown when the algorithm is not found
     * @return a ResponseEntity containing the error details and status code
     */
    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchAlgorithmException(NoSuchAlgorithmException ex) {
        log.warn("No Algorithm: {}", ex.getMessage());
        return buildErrorResponse("No Algorithm", HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles cases where a specific trainee cannot be found in the system.
     *
     * @param ex The {@link TraineeNotFoundException} representing a missing trainee.
     * @return A {@link ResponseEntity} containing an {@link ErrorResponse} with a 404 status code.
     */
    @ExceptionHandler(TraineeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTraineeNotFound(TraineeNotFoundException ex) {
        log.error("Trainee not found: {}", ex.getMessage());
        return buildErrorResponse("Trainee not found", HttpStatus.NOT_FOUND);
    }

    /**
     * Handles cases where a specific trainer cannot be found in the system.
     *
     * @param ex The {@link TrainerNotFoundException} representing a missing trainer.
     * @return A {@link ResponseEntity} containing an {@link ErrorResponse} with a 404 status code.
     */
    @ExceptionHandler(TrainerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTrainerNotFound(TrainerNotFoundException ex) {
        log.error("Trainer not found: {}", ex.getMessage());
        return buildErrorResponse("Trainer not found", HttpStatus.NOT_FOUND);
    }

    /**
     * Handles cases where a specific training session cannot be found in the system.
     *
     * @param ex The {@link TrainingNotFoundException} representing a missing training session.
     * @return A {@link ResponseEntity} containing an {@link ErrorResponse} with a 404 status code.
     */
    @ExceptionHandler(TrainingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTrainingNotFound(TrainingNotFoundException ex) {
        log.error("Training not found: {}", ex.getMessage());
        return buildErrorResponse("Training not found", HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exceptions arising from failures during workload updates.
     *
     * @param ex The {@link WorkloadUpdateException} encapsulating details of what went wrong during
     *           a workload update attempt.
     * @return A {@link ResponseEntity} containing a detailed ErrorResponse indicating the failure.
     */
    @ExceptionHandler(WorkloadUpdateException.class)
    public ResponseEntity<ErrorResponse> handleWorkloadUpdateException(WorkloadUpdateException ex) {
        log.error("Workload update failed - Status: {}, Details: {}", ex.getHttpStatus(), ex.getMessage());
        return buildErrorResponse("Failed to update workload: "
                + ex.getMessage(), ex.getHttpStatus());
    }

    /**
     * Handles cases where a specific user cannot be found in the system.
     *
     * @param ex The {@link UserNotFoundException} representing a missing user.
     * @return A {@link ResponseEntity} containing an {@link ErrorResponse} with a 404 status code.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        log.error("User not found: {}", ex.getMessage());
        return buildErrorResponse("User not found", HttpStatus.NOT_FOUND);
    }

    /**
     * Handles general application-level exceptions, providing a consistent response format.
     *
     * @param e The {@link ApplicationException} thrown within the application.
     * @return A {@link ResponseEntity} with a standardized {@link ErrorResponse}.
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e) {
        log.error("An Application error occurred: {}", e.getMessage(), e);
        return buildErrorResponse("An Application error occurred", HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles general, unexpected exceptions, providing a fallback error response.
     *
     * @param e The {@link Exception} that occurred unexpectedly.
     * @return A {@link ResponseEntity} with a standardized {@link ErrorResponse}.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        log.error("An unexpected error occurred: {}", e.getMessage(), e);
        return buildErrorResponse("An unexpected error occurred", HttpStatus.BAD_REQUEST);
    }


}
