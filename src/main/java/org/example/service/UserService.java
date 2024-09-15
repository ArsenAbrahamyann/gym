package org.example.service;

import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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

    public void updateUserPassword(String username, String newPassword) {
        log.info("Updating password for user: {}", username);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setPassword(newPassword);
        userRepository.update(user);
        log.info("Password updated successfully for user: {}", username);
    }

    public void toggleUserStatus(String username) {
        log.info("Toggling status for user: {}", username);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setIsActive(! user.getIsActive());
        userRepository.update(user);
        log.info("User status toggled successfully for {}", username);
    }
}
