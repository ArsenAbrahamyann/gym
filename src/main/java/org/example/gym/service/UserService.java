package org.example.gym.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.ChangeLoginRequestDto;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.UnauthorizedException;
import org.example.gym.exeption.UserNotFoundException;
import org.example.gym.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing user-related operations.
 * Provides methods to authenticate users, update passwords, and toggle user status.
 */
@Service
@Slf4j
public class UserService  {
    private final UserRepository userRepository;
    private final MetricsService metricsService;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructs a {@link UserService} with the specified user repository and services.
     *
     * @param userRepository the repository for managing user entities.
     * @param metricsService the metrics for managing user entities.
     */
    public UserService(UserRepository userRepository, MetricsService metricsService,
                       BCryptPasswordEncoder passwordEncoder) {
        this.metricsService = metricsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
     * Saves the given {@link UserEntity} to the database.
     *
     * <p>This method persists the provided user entity in the database. If the entity
     * does not already exist (i.e., it does not have an ID), it will be inserted as a new record.
     * If the entity already exists (i.e., it has an ID), it will be updated with the new data.</p>
     *
     * @param userEntity the {@link UserEntity} to be saved
     * @return the saved {@link UserEntity} with updated data, including any auto-generated fields such as ID
     */
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
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

        String newPassword = changeLoginRequestDto.getNewPassword();
        String encode = passwordEncoder.encode(newPassword);
        user.setPassword(encode);
        userRepository.save(user);
        log.info("User password changed successfully for username: {}", user.getUsername());
        metricsService.recordPasswordChange();
    }

    /**
     * Checks if a user with the given username exists in the database.
     *
     * <p>This method queries the database to determine whether a user with the specified
     * username is already present.</p>
     *
     * @param username the username to check for existence
     * @return {@code true} if a user with the given username exists, {@code false} otherwise
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Counts the number of users whose usernames start with the given prefix.
     *
     * <p>This method queries the database and returns the count of users whose
     * usernames begin with the provided prefix.</p>
     *
     * @param username the prefix of the username to count users by
     * @return the number of users whose usernames start with the specified prefix
     */
    public Integer countByUsernameStartingWith(String username) {
        return userRepository.countByUsernameStartingWith(username);
    }
}
