package org.example.gym.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {
    private final int MAX_ATTEMPTS = 3;
    private final long LOCK_TIME_DURATION = 300;
    private Map<String, Integer> attemptsCache = new HashMap<>();
    private Map<String, LocalDateTime> lockTimeCache = new HashMap<>();

    public void loginSucceeded(String username) {
        attemptsCache.remove(username);
        lockTimeCache.remove(username);
    }

    public void loginFailed(String username) {
        int attempts = attemptsCache.getOrDefault(username, 0);
        attempts++;
        attemptsCache.put(username, attempts);

        if (attempts >= MAX_ATTEMPTS) {
            lockTimeCache.put(username, LocalDateTime.now());
        }
    }

    public boolean isBlocked(String username) {
        if (lockTimeCache.containsKey(username)) {
            LocalDateTime lockTime = lockTimeCache.get(username);
            long secondsElapsed = java.time.Duration.between(lockTime, LocalDateTime.now()).toSeconds();
            if (secondsElapsed < LOCK_TIME_DURATION) {
                return true;
            } else {
                lockTimeCache.remove(username);
                attemptsCache.remove(username);
            }
        }
        return false;
    }
}
