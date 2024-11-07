package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.gym.dto.request.ChangeLoginRequestDto;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.UnauthorizedException;
import org.example.gym.exeption.UserNotFoundException;
import org.example.gym.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private MetricsService metricsService;

    @InjectMocks
    private UserService userService;

    private UserEntity mockUser;

    /**
     * Sets up a mock user entity before each test.
     * Initializes a {@link UserEntity} with a username and password to
     * be used in various test cases.
     */
    @BeforeEach
    public void setUp() {
        mockUser = new UserEntity();
        mockUser.setUsername("testUser");
        mockUser.setPassword("correctPassword");
    }

    @Test
    public void testFindByUsername_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));

        UserEntity foundUser = userService.findByUsername("testUser");

        verify(userRepository).findByUsername("testUser");
    }

    @Test
    public void testFindByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByUsername("nonExistentUser"));
        verify(userRepository).findByUsername("nonExistentUser");
    }

    @Test
    public void testAuthenticateUser_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));

        boolean isAuthenticated = userService.authenticateUser("testUser", "correctPassword");

        assertTrue(isAuthenticated);
        verify(metricsService).recordLoginSuccess();
        verify(metricsService, never()).recordLoginFailure();
    }

    @Test
    public void testAuthenticateUser_UserNotFound() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> userService.authenticateUser("nonExistentUser", "password"));
        verify(metricsService).recordLoginFailure();
        verify(metricsService, never()).recordLoginSuccess();
    }

    @Test
    public void testAuthenticateUser_IncorrectPassword() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));

        assertThrows(UnauthorizedException.class, () -> userService.authenticateUser("testUser", "wrongPassword"));
        verify(metricsService).recordLoginFailure();
        verify(metricsService, never()).recordLoginSuccess();
    }

    @Test
    public void testChangePassword_Success() {
        ChangeLoginRequestDto dto = new ChangeLoginRequestDto("testUser", "correctPassword", "newPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));

        userService.changePassword(dto);

        verify(userRepository).save(mockUser);
        verify(metricsService).recordPasswordChange();
        assertTrue(mockUser.getPassword().equals("newPassword"));
    }

    @Test
    public void testChangePassword_UserNotFound() {
        ChangeLoginRequestDto dto = new ChangeLoginRequestDto("nonExistentUser", "oldPassword", "newPassword");

        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.changePassword(dto));
    }

    @Test
    public void testExistsByUsername_UserExists() {
        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        boolean exists = userService.existsByUsername("existingUser");

        assertTrue(exists);
        verify(userRepository).existsByUsername("existingUser");
    }

    @Test
    public void testExistsByUsername_UserDoesNotExist() {
        when(userRepository.existsByUsername("nonExistentUser")).thenReturn(false);

        boolean exists = userService.existsByUsername("nonExistentUser");

        assertTrue(!exists);
        verify(userRepository).existsByUsername("nonExistentUser");
    }

}
