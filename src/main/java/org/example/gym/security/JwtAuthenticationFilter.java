package org.example.gym.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * A filter that intercepts incoming HTTP requests to validate JWT tokens in the request headers.
 *
 * <p>This filter checks for the presence of a valid JWT token in the request header, verifies its validity,
 * and if valid, sets the authentication context for the current request.</p>
 *
 * <p>The filter processes the JWT token in the following manner:
 * <ul>
 *     <li>If a valid JWT token is found, it is checked against a blacklist and its validity is verified.</li>
 *     <li>If the token is valid, the user details are loaded from the custom user details service.</li>
 *     <li>An authentication token is created and set in the security context for further processing.</li>
 * </ul>
 * </p>
 *
 * @see JwtUtils
 * @see CustomUserDetailsService
 * @see SecurityContextHolder
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final ApplicationContext context;

    /**
     * Intercepts the request to extract and validate the JWT token from the authorization header.
     *
     * <p>If a valid JWT token is found, the corresponding user details are loaded, and the authentication
     * context is populated with the user information. The filter proceeds with the request if the token is valid,
     * otherwise, it allows the request to continue without setting the authentication context.</p>
     *
     * @param request the HTTP request to be processed
     * @param response the HTTP response to be sent
     * @param filterChain the filter chain to pass the request and response to the next filter
     * @throws ServletException if the request processing fails
     * @throws IOException if an I/O error occurs during the request processing
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(SecurityConstants.HEADER_STRING);
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            token = authHeader.substring(7);

            if (jwtUtils.isTokenBlacklisted(token) && jwtUtils.extractUsername(token) != null) {
                username = jwtUtils.extractUsername(token);

                UserDetails userDetails = context.getBean(CustomUserDetailsService.class)
                        .loadUserByUsername(username);
                if (jwtUtils.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } else {
                log.warn("Invalid or blacklisted token: {}", token);
            }
        }

        filterChain.doFilter(request, response);
    }
}
