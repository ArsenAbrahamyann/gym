package org.example.gym.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
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
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Successfully logged out");
    }
}



