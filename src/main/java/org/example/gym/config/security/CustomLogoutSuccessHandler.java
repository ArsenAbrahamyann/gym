package org.example.gym.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.example.gym.exeption.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * CustomLogoutSuccessHandler is responsible for handling the success scenario when a user logs out.
 * It implements the {@link LogoutSuccessHandler} interface from Spring Security to define custom
 * behavior after a successful logout. The handler clears the security context and sends a response
 * indicating the logout was successful.
 */
@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final ObjectMapper objectMapper;

    /**
     * Handles the actions to take after a successful logout.
     * It clears the security context and sends a response indicating the success of the logout operation.
     *
     * @param request       The {@link HttpServletRequest} that contains the logout request.
     * @param response      The {@link HttpServletResponse} used to send the logout success response.
     * @param authentication The {@link Authentication} object containing the authenticated user's details.
     * @throws IOException if an I/O error occurs while sending the response.
     * @throws ServletException if the request cannot be processed.
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        ErrorResponse responseDto;
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(SecurityConstants.CONTENT_TYPE);
        responseDto = new ErrorResponse("Successfully logged out", HttpStatus.OK);
        objectMapper.writeValue(response.getWriter(), responseDto);

    }
}



