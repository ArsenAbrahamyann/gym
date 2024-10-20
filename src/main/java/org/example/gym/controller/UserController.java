package org.example.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.annotation.Authenticated;
import org.example.gym.dto.request.ChangeLoginRequestDto;
import org.example.gym.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController handles user-related operations such as login and changing passwords.
 */
@RestController
@RequestMapping("api/user")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    /**
     * Authenticates a user based on the provided username and password.
     *
     * @param username the username of the user attempting to log in
     * @param password the password of the user attempting to log in
     * @return a ResponseEntity indicating the success or failure of the login attempt
     */
    @GetMapping("/login")
    @Operation(summary = "User login", description = "Authenticates based on the provided username and password.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid username or password")
    })
    public ResponseEntity<Void> login(@RequestHeader String username, @RequestHeader String password) {
        log.info("Controller: User login attempt for username: {}", username);
        boolean isAuthenticated = userService.authenticateUser(username, password);

        if (isAuthenticated) {
            log.info("Controller: User {} logged in successfully", username);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            log.warn("Controller: User {} failed to log in", username);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Changes the password for a user based on the provided ChangeLoginRequestDto.
     *
     * @param requestDto the request containing the user's username and new password
     * @return a ResponseEntity indicating the success of the password change
     */
    @PutMapping("/change/login")
    @Authenticated
    @Operation(summary = "Change user password", description = "Allows a user to change their password.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password changed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<Void> changeLogin(@RequestBody ChangeLoginRequestDto requestDto) {
        log.info("Controller: Change login for user: {}", requestDto.getUsername());
        userService.changePassword(requestDto);
        log.info("Controller: User {} changed password successfully", requestDto.getUsername());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
