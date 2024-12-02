package org.example.gym.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.LoginRequestDto;
import org.example.gym.dto.response.JwtResponse;
import org.example.gym.entity.TokenEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.entity.enums.TokenType;
import org.example.gym.service.LoginAttemptService;
import org.example.gym.service.TokenService;
import org.example.gym.service.UserService;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Custom login filter that extends {@link UsernamePasswordAuthenticationFilter} to handle user login authentication.
 * It handles login attempts, JWT generation, token management, and login attempt tracking.
 *
 * <p>This filter processes login requests, authenticates the user, generates a JWT token upon successful
 * authentication, revokes old tokens, and sends the token in the response. It also tracks failed login attempts
 * and blocks further login attempts if necessary.</p>
 */
@Configuration
@Slf4j
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtils jwtUtils;
    private final LoginAttemptService loginAttemptService;
    private final TokenService tokenService;
    private final UserService userService;
    private static final ThreadLocal<LoginRequestDto> loginRequestHolder = new ThreadLocal<>();

    /**
     * Constructs a {@link CustomLoginFilter} with necessary dependencies.
     *
     * @param authenticationManager the {@link AuthenticationManager} used to authenticate the user
     * @param jwtUtils the utility class responsible for generating JWT tokens
     * @param loginAttemptService service to handle login attempts and block logic
     * @param tokenService repository to manage JWT tokens
     * @param userService service to manage user entities
     */
    public CustomLoginFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                             LoginAttemptService loginAttemptService,
                             TokenService tokenService,
                             UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
        super.setAuthenticationManager(authenticationManager);
        this.jwtUtils = jwtUtils;
        this.loginAttemptService = loginAttemptService;
        setFilterProcessesUrl("/user/login");
    }

    /**
     * Attempts to authenticate the user based on the provided login request.
     *
     * <p>This method parses the {@link LoginRequestDto} from the request, checks if the user is blocked
     * due to too many failed login attempts, and authenticates the user using the {@link AuthenticationManager}.</p>
     *
     * @param request the HTTP request containing the login details
     * @param response the HTTP response
     * @return the {@link Authentication} token if successful, or null if blocked
     * @throws RuntimeException if an error occurs while reading the login request
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            LoginRequestDto loginRequest = new ObjectMapper().readValue(request.getInputStream(),
                    LoginRequestDto.class);
            loginRequestHolder.set(loginRequest);
            if (loginAttemptService.isBlocked(loginRequest.getUsername())) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType(SecurityConstants.CONTENT_TYPE);
                response.getWriter().write(new ObjectMapper().writeValueAsString("User is temporarily blocked"));
                return null;
            }
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            return getAuthenticationManager().authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse login request", e);
        }
    }

    /**
     * Handles successful authentication by generating a JWT token and saving it in the database.
     *
     * <p>Upon successful authentication, this method generates a JWT token for the authenticated user,
     * revokes any previously issued tokens, and saves the new token to the database. It also resets the failed
     * login attempt count.</p>
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param chain the filter chain
     * @param authResult the authentication result containing user details
     * @throws IOException if an error occurs while writing the response
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);

        UserEntity user = userService.findByUsername(userDetails.getUsername());

        tokenService.getAllValidTokensByUser(user.getId())
                .forEach(tokenEntity -> {
                    tokenEntity.setRevoked(true);
                    tokenService.addToken(tokenEntity);
                });

        TokenEntity tokenEntity = TokenEntity.builder()
                .token(jwt)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .user(user)
                .build();
        tokenService.addToken(tokenEntity);

        response.setContentType(SecurityConstants.CONTENT_TYPE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(new JwtResponse(jwt)));

        LoginRequestDto loginRequest = loginRequestHolder.get();
        if (loginRequest != null && StringUtils.hasText(loginRequest.getUsername())) {
            loginAttemptService.resetAttempts(loginRequest.getUsername());
        }

        loginRequestHolder.remove();
    }

    /**
     * Handles unsuccessful authentication by tracking failed login attempts.
     *
     * <p>If authentication fails, this method responds with an unauthorized status and message.
     * It also increments the failed login attempts for the user.</p>
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param failed the exception thrown during authentication
     * @throws IOException if an error occurs while writing the response
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(SecurityConstants.CONTENT_TYPE);
        response.getWriter().write(new ObjectMapper().writeValueAsString("Invalid username or password"));

        LoginRequestDto loginRequest = loginRequestHolder.get();
        if (loginRequest != null && StringUtils.hasText(loginRequest.getUsername())) {
            loginAttemptService.registerFailedAttempt(loginRequest.getUsername());
        }

        loginRequestHolder.remove();
    }
}
