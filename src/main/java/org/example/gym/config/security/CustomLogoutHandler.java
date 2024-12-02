package org.example.gym.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.GymAuthenticationException;
import org.example.gym.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * CustomLogoutHandler is responsible for handling the logout process in the application.
 * It invalidates the JWT token for the user when they log out and ensures that all tokens
 * associated with the user are revoked.
 * This class is a Spring component that implements the {@link LogoutHandler} interface
 * for handling logout logic.
 */
@Component
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtUtils jwtUtils;
    private final JwtDecoder jwtDecoder;
    private final UserService userService;

    /**
     * Constructs a CustomLogoutHandler instance.
     *
     * @param jwtUtils      The utility class for handling JWT-related operations.
     * @param jwtDecoder    The decoder used to decode JWT tokens.
     * @param userService   The service for interacting with user-related data.
     */
    public CustomLogoutHandler(JwtUtils jwtUtils, JwtDecoder jwtDecoder, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.jwtDecoder = jwtDecoder;
        this.userService = userService;
    }

    /**
     * Handles the logout process by revoking all JWT tokens associated with the user.
     * It checks for the presence of a valid JWT token in the request header, decodes it,
     * and then invalidates all the user's tokens. If the token is revoked or the token
     * cannot be found, a {@link GymAuthenticationException} is thrown.
     *
     * @param request       The {@link HttpServletRequest} that contains the logout request.
     * @param response      The {@link HttpServletResponse} used to send the logout response.
     * @param authentication The {@link Authentication} object containing the authentication information.
     * @throws GymAuthenticationException if the token is invalid or already revoked.
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.debug("Running logout handler.");
        String authHeader = request.getHeader(SecurityConstants.HEADER_STRING);
        log.debug(authHeader);
        if (authHeader == null || ! authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            log.debug("No token provided.");
            throw new GymAuthenticationException("Authentication failed.");
        }
        String jwtToken = authHeader.substring(SecurityConstants.SUBSTRING_PREFIX);
        Jwt jwt = jwtDecoder.decode(jwtToken);
        if (jwtUtils.isTokenRevoked(jwt.getTokenValue())) {
            throw new GymAuthenticationException("Authentication failed.");
        }
        String username = jwt.getClaim(SecurityConstants.SUB);
        UserEntity user = userService.findByUsername(username);
        jwtUtils.revokeAllUserTokens(user);
        log.debug("Successfully logged out and invalidated users tokens.");
    }
}
