package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
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

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("testPassword");
    }

    @Test
    void testAuthenticateUserSuccess() {
        // Given
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

        // When
        boolean result = userService.authenticateUser("testUser", "testPassword");

        // Then
        assertTrue(result, "User authentication should succeed with correct password.");
        verify(userRepository).findByUsername("testUser");
    }

    @Test
    void testAuthenticateUserFailureDueToIncorrectPassword() {
        // Given
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

        // When
        boolean result = userService.authenticateUser("testUser", "wrongPassword");

        // Then
        assertFalse(result, "User authentication should fail with incorrect password.");
        verify(userRepository).findByUsername("testUser");
    }

    @Test
    void testAuthenticateUserThrowsEntityNotFoundException() {
        // Given
        when(userRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

        // When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.authenticateUser("nonExistingUser", "somePassword");
        });

        // Then
        assertEquals("User not found", exception.getMessage(), "Exception message should match expected output.");
        verify(userRepository).findByUsername("nonExistingUser");
    }

    @Test
    void testAuthenticateUserRepositoryInteraction() {
        // Given
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

        // When
        userService.authenticateUser("testUser", "testPassword");

        // Then
        verify(userRepository, times(1)).findByUsername("testUser");
    }
}
