package org.example.gym.service;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.example.gym.config.security.SecurityConstants;
import org.springframework.stereotype.Service;

/**
 * Service for tracking and managing login attempts, including failed login attempts
 * and blocking users after exceeding the maximum allowed attempts.
 *
 * <p>This service helps in preventing brute force attacks by limiting the number of
 * login attempts a user can make within a certain time period. After reaching the
 * maximum failed attempts, the user's account is blocked for a defined duration.</p>
 */
@Service
public class LoginAttemptService {
    private final Map<String, Integer> failedAttempts = new ConcurrentHashMap<>();
    private final Map<String, Long> blockTime = new ConcurrentHashMap<>();

    /**
     * Registers a failed login attempt for the given username.
     *
     * <p>If the number of failed attempts reaches the configured maximum, the user will be blocked
     * for a specified duration. The failed attempts counter is incremented and the user is blocked
     * if the threshold is exceeded.</p>
     *
     * @param username the username for which the failed attempt is registered
     */
    public void registerFailedAttempt(String username) {
        int attempts = failedAttempts.getOrDefault(username, 0) + 1;
        failedAttempts.put(username, attempts);

        if (attempts >= SecurityConstants.MAX_ATTEMPTS) {
            blockTime.put(username, System.currentTimeMillis());
        }
    }

    /**
     * Resets the failed login attempts and block time for a specified user.
     *
     * <p>This method removes the user's failed login attempt count and any block time
     * associated with the given username. It is typically used when a user successfully
     * logs in or after a certain condition that resets the user's login status.</p>
     *
     * @param username the username of the user whose login attempts and block time
     *                 should be reset
     */
    public void resetAttempts(String username) {
        failedAttempts.remove(username);
        blockTime.remove(username);
    }


    /**
     * Checks if the user is currently blocked due to excessive failed login attempts.
     *
     * <p>If the user is blocked, this method returns true. If the block duration has expired,
     * the block is removed, and the user can attempt to log in again.</p>
     *
     * @param username the username to check for block status
     * @return {@code true} if the user is blocked, {@code false} otherwise
     */
    public boolean isBlocked(String username) {
        Long blockedTime = blockTime.get(username);
        if (blockedTime == null) {
            return false;
        }
        if (System.currentTimeMillis() - blockedTime > SecurityConstants.BLOCK_DURATION) {
            blockTime.remove(username);
            failedAttempts.remove(username);
            return false;
        }
        return true;
    }
}
