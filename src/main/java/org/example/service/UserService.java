package org.example.service;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.exeption.ResourceNotFoundException;
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
     * Retrieves a list of all usernames from the system.
     *
     * <p>This method is marked as {@code @Transactional}, meaning that it is executed
     * within a transactional context. If called within a transaction, it will
     * participate in that transaction; otherwise, a new transaction will be started.
     *
     * <p>The method interacts with the underlying {@link UserRepository} to fetch
     * all the usernames from the database. The repository should handle querying
     * for the usernames and return a list of results.
     *
     * @return a {@link List} of {@link String} containing all usernames in the system.
     */
    @Transactional
    public List<String> findAllUsernames() {
        return userRepository.findAllUsername()
                .orElseThrow(() -> new ResourceNotFoundException("Usernames not found"));
    }

    /**
     * Authenticates a user by checking their username and password.
     *
     * @param username The username of the user to authenticate.
     * @param password The password of the user to authenticate.
     * @return {@code true} if the username and password match, {@code false} otherwise.
     * @throws EntityNotFoundException If the user with the specified username is not found.
     */
    public boolean authenticateUser(String username, String password) {
        log.info("Authenticating user: {}", username);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (user.getPassword().equals(password)) {
            log.info("User {} authenticated successfully", username);
            return true;
        } else {
            log.warn("Authentication failed for user {}", username);
            return false;
        }
    }

    /**
     * Updates the password for a specified user.
     *
     * @param username The username of the user whose password is to be updated.
     * @param newPassword The new password to set for the user.
     * @throws EntityNotFoundException If the user with the specified username is not found.
     */
    public void updateUserPassword(String username, String newPassword) {
        log.info("Updating password for user: {}", username);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setPassword(newPassword);
        userRepository.update(user);
        log.info("Password updated successfully for user: {}", username);
    }

    /**
     * Toggles the active status of a specified user.
     *
     * @param username The username of the user whose status is to be toggled.
     * @throws EntityNotFoundException If the user with the specified username is not found.
     */
    public void toggleUserStatus(String username) {
        log.info("Toggling status for user: {}", username);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setIsActive(! user.getIsActive());
        userRepository.update(user);
        log.info("User status toggled successfully for {}", username);
    }
}
