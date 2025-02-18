package org.example.gym.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.LoginRequestDto;
import org.example.gym.dto.response.JwtResponseDto;
import org.example.gym.entity.TokenEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.entity.enums.TokenType;
import org.example.gym.exeption.ErrorResponse;
import org.example.gym.service.LoginAttemptService;
import org.example.gym.service.TokenService;
import org.example.gym.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

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

    private static final RequestMatcher LOGIN_REQUEST = new AntPathRequestMatcher("/user/login", "GET");
    private final JwtUtils jwtUtils;
    private final LoginAttemptService loginAttemptService;
    private final TokenService tokenService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a {@link CustomLoginFilter} with necessary dependencies.
     *
     * @param authenticationManager the {@link AuthenticationManager} used to authenticate the user
     * @param jwtUtils              the utility class responsible for generating JWT tokens
     * @param loginAttemptService   service to handle login attempts and block logic
     * @param tokenService          repository to manage JWT tokens
     * @param userService           service to manage user entities
     */
    public CustomLoginFilter(AuthenticationManager authenticationManager,
                             JwtUtils jwtUtils,
                             LoginAttemptService loginAttemptService,
                             TokenService tokenService,
                             UserService userService, ObjectMapper objectMapper
    ) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.objectMapper = objectMapper;
        super.setAuthenticationManager(authenticationManager);
        super.setRequiresAuthenticationRequestMatcher(LOGIN_REQUEST);
        this.jwtUtils = jwtUtils;
        this.loginAttemptService = loginAttemptService;
    }


    /**
     * Attempts to authenticate the user based on the provided login request.
     *
     * <p>This method parses the {@link LoginRequestDto} from the request, checks if the user is blocked
     * due to too many failed login attempts, and authenticates the user using the {@link AuthenticationManager}.</p>
     *
     * @param request  the HTTP request containing the login details
     * @param response the HTTP response
     * @return the {@link Authentication} token if successful, or null if blocked
     * @throws RuntimeException if an error occurs while reading the login request
     */
    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String ipAddress = request.getRemoteAddr();
        ErrorResponse responseDto;
        if (loginAttemptService.isBlocked(ipAddress)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(SecurityConstants.CONTENT_TYPE);
            responseDto = new ErrorResponse("User is blocked for 5 minutes"
                    + " due to multiple failed login attempts",
                    HttpStatus.BAD_REQUEST);
            objectMapper.writeValue(response.getWriter(), responseDto);
            return null;
        }

        String username = request.getHeader("username");
        String password = request.getHeader("password");

        if (username.isEmpty() || password.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(SecurityConstants.CONTENT_TYPE);
            responseDto = new ErrorResponse("Username and password are required",
                    HttpStatus.BAD_REQUEST);
            objectMapper.writeValue(response.getWriter(), responseDto);
            return null;
        }

        log.debug("Checking if the user is already logged in.");
        String jwtToken = request.getHeader(SecurityConstants.HEADER_STRING);


        if (jwtToken != null && jwtToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            if (!jwtUtils.isTokenRevoked(jwtToken.substring(7))) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.setContentType(SecurityConstants.CONTENT_TYPE);
                responseDto = new ErrorResponse("User is already logged in. Please use your "
                        + "valid token or log out before logging in again.",
                        HttpStatus.CONFLICT);
                objectMapper.writeValue(response.getWriter(), responseDto);
                return null;
            }
        }

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password);
        return getAuthenticationManager().authenticate(authToken);
    }

    /**
     * Handles successful authentication by generating a JWT token and saving it in the database.
     *
     * <p>Upon successful authentication, this method generates a JWT token for the authenticated user,
     * revokes any previously issued tokens, and saves the new token to the database. It also resets the failed
     * login attempt count.</p>
     *
     * @param request    the HTTP request
     * @param response   the HTTP response
     * @param chain      the filter chain
     * @param authResult the authentication result containing user details
     * @throws IOException if an error occurs while writing the response
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);

        UserEntity user = userService.findByUsername(userDetails.getUsername());

        TokenEntity tokenEntity = TokenEntity.builder()
                .token(jwt)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .user(user)
                .build();
        tokenService.save(tokenEntity);

        response.setContentType(SecurityConstants.CONTENT_TYPE);
        response.getWriter().write(objectMapper.writeValueAsString(new JwtResponseDto(jwt)));

        loginAttemptService.resetAttemptsByIp(userDetails.getUsername());
    }

    /**
     * Handles unsuccessful authentication by tracking failed login attempts.
     *
     * <p>If authentication fails, this method responds with an unauthorized status and message.
     * It also increments the failed login attempts for the user.</p>
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param failed   the exception thrown during authentication
     * @throws IOException if an error occurs while writing the response
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        String ipAddress = request.getRemoteAddr();
        ErrorResponse responseDto;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(SecurityConstants.CONTENT_TYPE);
        responseDto = new ErrorResponse("Invalid username or password",
                HttpStatus.BAD_REQUEST);
        objectMapper.writeValue(response.getWriter(), responseDto);
        loginAttemptService.registerFailedAttemptByIp(ipAddress);
    }
}
