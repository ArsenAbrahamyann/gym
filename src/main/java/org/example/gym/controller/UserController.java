package org.example.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.ChangeLoginRequestDto;
import org.example.gym.dto.response.JwtResponse;
import org.example.gym.security.JWTTokenProvider;
import org.example.gym.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController handles user-related operations such as login and changing passwords.
 */
@Tag(name = "User-Controller")
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final JWTTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

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
    public ResponseEntity<JwtResponse> login(@RequestHeader String username, @RequestHeader String password) {
        log.info("Controller: User login attempt for username: {}", username);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        log.info("Controller: User {} logged in successfully", username);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    /**
     * Changes the password for a user based on the provided ChangeLoginRequestDto.
     *
     * @param requestDto the request containing the user's username and new password
     * @return a ResponseEntity indicating the success of the password change
     */
    @PutMapping("/change/login")
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
