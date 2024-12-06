package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoginAttemptServiceTest {

    @InjectMocks
    private LoginAttemptService loginAttemptService;


    @Test
    void testRegisterFailedAttempt_BelowMaxAttempts() {
        String username = "testUser";

        loginAttemptService.registerFailedAttempt(username);
        loginAttemptService.registerFailedAttempt(username);

        assertFalse(loginAttemptService.isBlocked(username));
    }

    @Test
    void testRegisterFailedAttempt_ReachesMaxAttempts() {
        String username = "testUser";

        loginAttemptService.registerFailedAttempt(username);
        loginAttemptService.registerFailedAttempt(username);
        loginAttemptService.registerFailedAttempt(username);

        assertTrue(loginAttemptService.isBlocked(username));
    }

    @Test
    void testIsBlocked_UserBlockedAndBlockTimeNotExpired() {
        String username = "testUser";

        loginAttemptService.registerFailedAttempt(username);
        loginAttemptService.registerFailedAttempt(username);
        loginAttemptService.registerFailedAttempt(username);

        assertTrue(loginAttemptService.isBlocked(username));
    }



    @Test
    void testResetAttempts() {
        String username = "testUser";

        loginAttemptService.registerFailedAttempt(username);
        loginAttemptService.registerFailedAttempt(username);
        loginAttemptService.registerFailedAttempt(username);

        assertTrue(loginAttemptService.isBlocked(username));

        loginAttemptService.resetAttempts(username);

        assertFalse(loginAttemptService.isBlocked(username));
    }

    @Test
    void testIsBlocked_NoFailedAttempts() {
        String username = "testUser";

        assertFalse(loginAttemptService.isBlocked(username));
    }
}
