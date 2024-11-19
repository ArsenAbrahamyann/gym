package org.example.gym.service;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {
    private final Map<String, Integer> failedAttempts = new ConcurrentHashMap<>();
    private final Map<String, Long> blockTime = new ConcurrentHashMap<>();

    private static final int MAX_ATTEMPTS = 3;
    private static final long BLOCK_DURATION = 5 * 60 * 1000;

    public void registerFailedAttempt(String username) {
        int attempts = failedAttempts.getOrDefault(username, 0) + 1;
        failedAttempts.put(username, attempts);

        if (attempts >= MAX_ATTEMPTS) {
            blockTime.put(username, System.currentTimeMillis());
        }
    }

    public boolean isBlocked(String username) {
        Long blockedTime = blockTime.get(username);
        if (blockedTime == null) {
            return false;
        }

        if (System.currentTimeMillis() - blockedTime > BLOCK_DURATION) {
            blockTime.remove(username);
            failedAttempts.remove(username);
            return false;
        }
        return true;
    }


}
