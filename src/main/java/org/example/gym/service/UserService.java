package org.example.gym.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.ResourceNotFoundException;
import org.example.gym.paylod.request.ChangeLoginRequestDto;
import org.example.gym.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

/**
 * Service class for managing user-related operations.
 * Provides methods to authenticate users, update passwords, and toggle user status.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
        log.info("Authenticating user: {}", username);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User name not found." + username));

        boolean authenticated = user.getPassword().equals(password);
        if (authenticated) {
            log.info("User {} authenticated successfully", username);
        } else {
            log.warn("Authentication failed for user {}", username);
        }
        return authenticated;
    }

    /**
     * Retrieves all usernames from the repository.
     *
     * @return a list of usernames. If no usernames are found, an empty list is returned.
     */
    @Transactional(readOnly = true)
    public List<String> findAllUsernames() {
        log.info("Fetching all usernames from the repository.");
        List<String> allUsernames = userRepository.findAllUsername();
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
        log.info("Retrieving user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User name not found." + username));
    }

    /**
     * Saves a {@link UserEntity} to the database.
     *
     * @param user the {@link UserEntity} to be saved in the database.
     */
    @Transactional
    public void save(UserEntity user) {
        log.info("Saving user: {}", user.getUsername());
        userRepository.save(user);
    }

    /**
     * Updates an existing {@link UserEntity} in the database.
     *
     * @param user the {@link UserEntity} to be updated in the database.
     */
    @Transactional
    public void update(UserEntity user) {
        log.info("Updating user: {}", user.getUsername());
        userRepository.save(user);
    }

    /**
     * Changes the password for the user.
     *
     * @param changeLoginRequestDto contains the username and old/new passwords.
     */
    public void changePassword(ChangeLoginRequestDto changeLoginRequestDto) {
        UserEntity user = findByUsername(changeLoginRequestDto.getUsername());

            if (user.getPassword().equals(changeLoginRequestDto.getOldPassword())) {
                user.setPassword(changeLoginRequestDto.getNewPassword());
                log.info("Password exchange successful for user: {}", user.getUsername());
            } else {
                log.warn("Wrong password for user: {}", user.getUsername());
            }
    }
}
