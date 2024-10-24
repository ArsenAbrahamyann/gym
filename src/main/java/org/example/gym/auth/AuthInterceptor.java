package org.example.gym.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.exeption.ResourceNotFoundException;
import org.example.gym.exeption.UnauthorizedException;
import org.example.gym.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor responsible for authenticating incoming HTTP requests based on
 * the provided username and password headers.
 * It uses the {@link UserService} to authenticate users and handles various
 * exceptions such as invalid credentials or missing headers.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private final UserService userService;
    private final ObjectMapper mapper;

    private static final String USERNAME_HEADER = "Username";
    private static final String PASSWORD_HEADER = "Password";

    /**
     * Pre-handle method that intercepts each HTTP request and performs
     * authentication based on the headers.
     * If the request does not contain valid `Username` and `Password` headers, or if the credentials are incorrect,
     * an appropriate error response is returned.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param handler  the handler (or controller) for the request
     * @return {@code true} if the request passes authentication, {@code false} otherwise
     * @throws Exception if any exception occurs during authentication
     */
    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        if (requestUri.equals("/swagger-ui/favicon-16x16.png") || requestUri.equals("/favicon.ico")) {
            return true;
        }

        String username = request.getHeader(USERNAME_HEADER);
        String password = request.getHeader(PASSWORD_HEADER);

        log.debug("Performing authentication for user: {}", username);

        if (username == null || password == null) {
            sendErrorResponse(response, "Username and password must be provided.", HttpStatus.BAD_REQUEST);
            return false;
        }

        try {
            userService.authenticateUser(username, password);
        } catch (ResourceNotFoundException e) {
            log.warn("Authentication failed for user: {}. Reason: {}", username, e.getMessage());
            sendErrorResponse(response, "Authentication failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
            return false;
        } catch (UnauthorizedException e) {
            log.warn("Invalid credentials for user: {}. Reason: {}", username, e.getMessage());
            sendErrorResponse(response, "Invalid credentials: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
            return false;
        } catch (Exception e) {
            log.error("An unexpected error occurred during authentication for user: {}", username, e);
            sendErrorResponse(response, "An error occurred during authentication.", HttpStatus.INTERNAL_SERVER_ERROR);
            return false;
        }

        log.debug("Authentication succeeded for user: {}", username);
        return true;
    }

    /**
     * Sends an error response to the client when authentication fails.
     *
     * @param response the HTTP response to send the error message
     * @param message  the error message to include in the response
     * @param status   the HTTP status code to return
     * @throws Exception if an error occurs while writing the response
     */
    private void sendErrorResponse(HttpServletResponse response, String message, HttpStatus status) throws Exception {
        response.setContentType("application/json");
        response.setStatus(status.value());
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(new ResourceNotFoundException(message, status));
        response.getWriter().write(json);
    }
}
