package org.example.service;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        try {
            UserEntity user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            if (user.getPassword().equals(password)) {
                log.info("User {} authenticated successfully", username);
                return true;
            } else {
                log.warn("Authentication failed for user {}", username);
                return false;
            }
        } catch (EntityNotFoundException e) {
            log.error("Authentication error for user {}: {}", username, e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error during authentication for user {}: {}", username, e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all usernames from the repository.
     *
     * @return a list of usernames. If no usernames are found, an empty list is returned.
     */
    @Transactional
    public List<String> findAllUsernames() {
        log.info("Fetching all usernames from the repository.");
        try {
            Optional<List<String>> allUsernames = userRepository.findAllUsername();
            return allUsernames.orElseGet(List::of);
        } catch (Exception e) {
            log.error("Error fetching usernames: {}", e.getMessage());
            return List.of();
        }
    }

    /**
     * Retrieves a {@link UserEntity} by its username.
     * <p>
     * This method is transactional and queries the database for a user entity that matches the provided username.
     * </p>
     *
     * @param username the username of the {@link UserEntity} to be retrieved.
     * @return an {@link Optional} containing the found {@link UserEntity}, or an empty {@link Optional} if no user is found with the specified username.
     */
    @Transactional
    public Optional<UserEntity> findByUsername(String username) {
        log.info("Retrieving user by username: {}", username);
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            log.error("Error retrieving user by username {}: {}", username, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Saves a {@link UserEntity} to the database.
     * <p>
     * This method is transactional and persists the provided {@link UserEntity} in the database.
     * </p>
     *
     * @param user the {@link UserEntity} to be saved in the database.
     */
    @Transactional
    public void save(UserEntity user) {
        log.info("Saving user: {}", user.getUsername());
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Error saving user {}: {}", user.getUsername(), e.getMessage());
        }
    }

    /**
     * Updates an existing {@link UserEntity} in the database.
     * <p>
     * This method is transactional and saves the updated version of the provided {@link UserEntity}.
     * If the entity does not exist, it will create a new one.
     * </p>
     *
     * @param user the {@link UserEntity} to be updated in the database.
     */
    @Transactional
    public void update(UserEntity user) {
        log.info("Updating user: {}", user.getUsername());
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Error updating user {}: {}", user.getUsername(), e.getMessage());
        }
    }
}
