package org.example.gym.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.ChangeLoginRequestDto;
import org.example.gym.dto.response.JwtResponse;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.UnauthorizedException;
import org.example.gym.exeption.UserNotFoundException;
import org.example.gym.repository.UserRepository;
import org.example.gym.security.JwtUtils;
import org.example.gym.security.SecurityConstants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing user-related operations.
 * Provides methods to authenticate users, update passwords, and toggle user status.
 */
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final MetricsService metricsService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructs a {@link UserService} with the specified user repository and services.
     *
     * @param userRepository the repository for managing user entities.
     * @param metricsService the metrics for managing user entities.
     * @param jwtUtils .
     * @param authenticationManager .
     */
    public UserService(UserRepository userRepository, MetricsService metricsService,
                       JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.metricsService = metricsService;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Authenticates a user by checking their username and password.
     *
     * @param username The username of the user to authenticate.
     * @param password The password of the user to authenticate.
     * @return {@code true} if the username and password match, {@code false} otherwise.
     * @throws EntityNotFoundException If the user with the specified username is not found.
     */
    @Transactional
    public boolean authenticateUser(String username, String password) {
        log.debug("Authenticating user with username: {}", username);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    metricsService.recordLoginFailure();
                    log.warn("Authentication failed - User not found for username: {}", username);
                    return new UnauthorizedException("Invalid credentials");
                });
        if (!user.getPassword().equals(password)) {
            metricsService.recordLoginFailure();
            log.warn("Authentication failed - Incorrect password for username: {}", username);
            throw new UnauthorizedException("Invalid credentials");
        }

        log.info("Authentication successful for username: {}", username);
        metricsService.recordLoginSuccess();
        return true;
    }


    /**
     * Retrieves a {@link UserEntity} by its username.
     *
     * @param username the username of the {@link UserEntity} to be retrieved.
     * @return an {@link Optional} containing the found {@link UserEntity}, or an empty {@link Optional} if no user is found.
     */
    @Transactional(readOnly = true)
    public UserEntity findByUsername(String username) {
        log.debug("Retrieving user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User not found for username: {}", username);
                    return new UserNotFoundException("User name not found: "
                            + username);
                });
    }


    /**
     * Changes the password for the user.
     *
     * @param changeLoginRequestDto contains the username and old/new passwords.
     */
    public void changePassword(ChangeLoginRequestDto changeLoginRequestDto) {
        log.debug("Changing password for user: {}", changeLoginRequestDto.getUsername());

        UserEntity user = userRepository.findByUsername(changeLoginRequestDto.getUsername())
                .orElseThrow(() -> {
                    log.warn("User not found for username: {}", changeLoginRequestDto.getUsername());
                    return new UserNotFoundException("User not found");
                });

        user.setPassword(changeLoginRequestDto.getNewPassword());
        userRepository.save(user);
        log.info("User password changed successfully for username: {}", user.getUsername());
        metricsService.recordPasswordChange();
    }

    /**
     * check exist this username.
     *
     * @param username .
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Integer countByUsernameStartingWith(String username) {

        return userRepository.countByUsernameStartingWith(username);
    }

    /**
     * get userEntity from userId.
     *
     * @param userId .
     */
    public UserEntity findById(long userId) {
        return userRepository.findById(userId)
              .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public JwtResponse login(String username, String password) {

        Authentication authentication;

        authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username,
                    password
        ));

        log.info("Authentication" + authentication.getName());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtUtils.generateToken(authentication);

        return new JwtResponse(jwt);
    }
}
