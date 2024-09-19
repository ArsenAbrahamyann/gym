package org.example.service;

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

}
