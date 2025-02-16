package org.example.gym.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.response.InvalidLoginResponseDto;
import org.example.gym.exeption.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Custom implementation of {@link AuthenticationEntryPoint} for handling unauthorized access.
 * This component is used to return a custom JSON response with a 401 Unauthorized status
 * when an authentication error occurs, such as when a request is made without valid credentials.
 *
 * <p>It returns a JSON response containing an {@link InvalidLoginResponseDto} object when
 * an authentication exception is thrown.</p>
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    /**
     * Called when an authentication exception is thrown (i.e., when the user is not authenticated).
     *
     * <p>This method constructs a custom response with a 401 Unauthorized status and a JSON
     * body containing the details of the invalid login response. The response is then sent to
     * the client.</p>
     *
     * @param httpServletRequest the HTTP request that caused the exception
     * @param httpServletResponse the HTTP response to send to the client
     * @param e the exception that triggered the authentication failure
     * @throws IOException if an input or output error occurs during the response handling
     * @throws ServletException if a servlet-related error occurs during the response handling
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        log.debug("Authentication entry point.");
        log.debug("Authentication failed: {}", e.getMessage());
        ErrorResponse responseDto;
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType(SecurityConstants.CONTENT_TYPE);
        Throwable authExceptionCause = e.getCause();
        if (authExceptionCause instanceof JwtValidationException) {
            log.debug("JwtValidationException: Invalid jwt was sent.");
            responseDto = new ErrorResponse("Authentication failed: The provided JWT token is invalid",
                    HttpStatus.BAD_REQUEST);
            objectMapper.writeValue(httpServletResponse.getWriter(), responseDto);
            return;
        }
        responseDto = new ErrorResponse("Access denied!", HttpStatus.BAD_REQUEST);
        objectMapper.writeValue(httpServletResponse.getWriter(), responseDto);
    }
}
