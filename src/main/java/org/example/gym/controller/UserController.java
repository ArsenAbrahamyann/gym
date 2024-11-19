package org.example.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.ChangeLoginRequestDto;
import org.example.gym.dto.response.JwtResponse;
import org.example.gym.security.JwtUtils;
import org.example.gym.service.LoginAttemptService;
import org.example.gym.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * UserController handles user-related operations such as login and changing passwords.
 */
@Tag(name = "User-Controller",
        description = "Controller for user operations such as login, logout, and changing passwords.")
@RestController
@CrossOrigin
@RequestMapping("user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final AuthenticationProvider authenticationProvider;
    private final JwtUtils jwtUtils;
    private final LoginAttemptService loginAttemptService;


    /**
     * Logs in a user by validating the provided username and password.
     * If authentication is successful, a JWT token is generated and returned.
     * If the user is blocked due to multiple failed login attempts, an error is thrown.
     *
     * @param username the username provided by the user
     * @param password the password provided by the user
     * @return a {@link JwtResponse} containing the generated JWT token
     * @throws LockedException if the user is blocked due to multiple failed login attempts
     * @throws ResponseStatusException if the credentials are invalid
     */
    @GetMapping("/login")
    @Operation(summary = "User login", description = "Logs in the user and generates a JWT token.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful, JWT token returned"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "423", description = "User is blocked due to multiple failed login attempts")
    })
    public ResponseEntity<?> login(@RequestHeader String username, @RequestHeader String password) {
        if (loginAttemptService.isBlocked(username)) {
            throw new LockedException("User is blocked ");
        }
        try {
            Authentication authentication = authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtils.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (BadCredentialsException e) {
            loginAttemptService.registerFailedAttempt(username);
            if (loginAttemptService.isBlocked(username)) {
                throw new LockedException("User is blocked for 5 minutes due to multiple failed login attempts");
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    /**
     * Logs out a user by invalidating the provided JWT token.
     * The token is removed from the {@link SecurityContextHolder}, effectively logging out the user.
     *
     * @param token the JWT token sent by the client for invalidation
     * @return a ResponseEntity with a message indicating successful logout
     */
    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Invalidates the provided JWT token and logs out the user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout successful")
    })
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        jwtUtils.invalidateToken(jwt); // Example: Invalidate the token (you need to implement this)
        SecurityContextHolder.clearContext();
        log.info("User logged out successfully, token invalidated: {}", jwt);
        return ResponseEntity.ok("Logout successful");
    }

    /**
     * Changes the password for a user based on the provided ChangeLoginRequestDto.
     *
     * @param requestDto the request containing the user's username and new password
     * @return a ResponseEntity indicating the success of the password change
     */
    @PutMapping("/change/password")
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
