package org.example.gym.config.security;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.gym.dto.response.InvalidLoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Custom implementation of {@link AuthenticationEntryPoint} for handling unauthorized access.
 * This component is used to return a custom JSON response with a 401 Unauthorized status
 * when an authentication error occurs, such as when a request is made without valid credentials.
 *
 * <p>It returns a JSON response containing an {@link InvalidLoginResponse} object when
 * an authentication exception is thrown.</p>
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

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
        InvalidLoginResponse loginResponse = new InvalidLoginResponse();
        String jsonLoginResponse = new Gson().toJson(loginResponse);
        httpServletResponse.setContentType(SecurityConstants.CONTENT_TYPE);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.getWriter().println(jsonLoginResponse);
    }
}
