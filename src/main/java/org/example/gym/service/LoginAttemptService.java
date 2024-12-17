package org.example.gym.service;


import java.util.concurrent.ConcurrentHashMap;
import org.example.gym.config.security.SecurityConstants;
import org.springframework.stereotype.Service;

/**
 * Service for tracking and managing login attempts and blocking users
 * after exceeding the maximum allowed failed attempts.
 *
 * <p>This service is designed to prevent brute-force attacks by limiting
 * the number of login attempts a user can make within a defined time period.
 * After reaching the maximum failed attempts, the user's account is temporarily
 * blocked for a specified duration.</p>
 */
@Service
public class LoginAttemptService {
    private final ConcurrentHashMap<String, Integer> attemptsByIp = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> blockUntilByIp = new ConcurrentHashMap<>();

    /**
     * Checks if the given IP address is currently blocked due to excessive failed attempts.
     *
     * @param ipAddress the IP address to check
     * @return {@code true} if the IP is blocked, {@code false} otherwise
     */
    public boolean isBlocked(String ipAddress) {
        if (blockUntilByIp.containsKey(ipAddress)) {
            if (blockUntilByIp.get(ipAddress) > System.currentTimeMillis()) {
                return true;
            }
            blockUntilByIp.remove(ipAddress);
        }
        return false;
    }

    /**
     * Registers a failed login attempt from a specific IP address.
     * If the maximum number of failed attempts is reached, the IP is blocked for a set duration.
     *
     * @param ipAddress the IP address from which the failed attempt occurred
     */
    public void registerFailedAttemptByIp(String ipAddress) {
        int attempts = attemptsByIp.getOrDefault(ipAddress, 0) + 1;
        attemptsByIp.put(ipAddress, attempts);
        if (attempts >= SecurityConstants.MAX_ATTEMPTS) {
            blockUntilByIp.put(ipAddress, System.currentTimeMillis() + SecurityConstants.BLOCK_DURATION);
            attemptsByIp.remove(ipAddress);
        }
    }

    /**
     * Resets the failed login attempts for a specific IP address.
     *
     * @param ipAddress the IP address whose attempts should be reset
     */
    public void resetAttemptsByIp(String ipAddress) {
        attemptsByIp.remove(ipAddress);
    }
}
