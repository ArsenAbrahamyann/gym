package org.example.gym.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.gym.config.security.SecurityConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginAttemptServiceTest {

    private LoginAttemptService loginAttemptService;
    private final String ipAddress = "192.168.1.1";

    @BeforeEach
    void setUp() {
        loginAttemptService = new LoginAttemptService();
    }

    @Test
    void testIsBlocked_ShouldReturnFalseInitially() {
        assertThat(loginAttemptService.isBlocked(ipAddress)).isFalse();
    }

    @Test
    void testRegisterFailedAttemptByIp_ShouldNotBlockBeforeMaxAttempts() {
        for (int i = 1; i < SecurityConstants.MAX_ATTEMPTS; i++) {
            loginAttemptService.registerFailedAttemptByIp(ipAddress);
            assertThat(loginAttemptService.isBlocked(ipAddress)).isFalse();
        }
    }

    @Test
    void testRegisterFailedAttemptByIp_ShouldBlockAfterMaxAttempts() {
        for (int i = 0; i < SecurityConstants.MAX_ATTEMPTS; i++) {
            loginAttemptService.registerFailedAttemptByIp(ipAddress);
        }
        assertThat(loginAttemptService.isBlocked(ipAddress)).isTrue();
    }


}
