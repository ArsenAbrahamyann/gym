package org.example.gym.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.ChangeLoginRequestDto;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.UnauthorizedException;
import org.example.gym.exeption.UserNotFoundException;
import org.example.gym.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
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
    private final TrainerService trainerService;
    private final TraineeService traineeService;

    /**
     * Constructs a {@link UserService} with the specified user repository and services.
     *
     * @param userRepository the repository for managing user entities.
     * @param trainerService the service for managing trainer entities.
     * @param traineeService the service for managing trainee entities.
     */
    public UserService(UserRepository userRepository, @Lazy TrainerService trainerService,
                       @Lazy TraineeService traineeService) {
        this.userRepository = userRepository;
        this.trainerService = trainerService;
        this.traineeService = traineeService;
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
        log.debug("Entering authenticateUser() with username: {}", username);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Authentication failed for username: {}. User not found.", username);
                    return new UnauthorizedException("Invalid credentials");
                });

        if (!user.getPassword().equals(password)) {
            log.warn("Invalid credentials provided for username: {}", username);
            throw new UnauthorizedException("Invalid credentials");
        }

        log.info("User {} authenticated successfully.", username);
        return true;
    }

    /**
     * Retrieves all usernames from the repository.
     *
     * @return a list of usernames. If no usernames are found, an empty list is returned.
     */
    @Transactional(readOnly = true)
    public List<String> findAllUsernames() {
        log.debug("Fetching all usernames from the repository.");
        List<String> allUsernames = userRepository.findAllUsername();
        log.info("Retrieved {} usernames from the repository.", allUsernames.size());
        return allUsernames.isEmpty() ? Collections.emptyList() : allUsernames;
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

    }

}
