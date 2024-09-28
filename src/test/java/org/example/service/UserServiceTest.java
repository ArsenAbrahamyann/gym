package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

        boolean result = userService.authenticateUser("testUser", "testPassword");

        assertTrue(result, "User authentication should succeed with correct password.");
        verify(userRepository).findByUsername("testUser");
    }

    @Test
    void testAuthenticateUserFailureDueToIncorrectPassword() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

        boolean result = userService.authenticateUser("testUser", "wrongPassword");

        assertFalse(result, "User authentication should fail with incorrect password.");
        verify(userRepository).findByUsername("testUser");
    }

    @Test
    void testAuthenticateUserFailureDueToUserNotFound() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        boolean result = userService.authenticateUser("nonExistentUser", "anyPassword");

        assertFalse(result, "User authentication should fail for non-existent user.");
        verify(userRepository).findByUsername("nonExistentUser");
    }

    @Test
    void testAuthenticateUserHandlesUnexpectedError() {
        when(userRepository.findByUsername("testUser")).thenThrow(new RuntimeException("Database error"));

        boolean result = userService.authenticateUser("testUser", "testPassword");

        assertFalse(result, "User authentication should fail on unexpected error.");
        verify(userRepository).findByUsername("testUser");
    }

    @Test
    void testAuthenticateUserRepositoryInteraction() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

        userService.authenticateUser("testUser", "testPassword");

        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void testFindAllUsernamesReturnsList() {
        when(userRepository.findAllUsername()).thenReturn(List.of("testUser", "anotherUser"));

        List<String> usernames = userService.findAllUsernames();

        assertEquals(2, usernames.size(), "Should return 2 usernames.");
        assertTrue(usernames.contains("testUser"), "Usernames should contain 'testUser'.");
        assertTrue(usernames.contains("anotherUser"), "Usernames should contain 'anotherUser'.");
        verify(userRepository).findAllUsername();
    }

    @Test
    void testFindAllUsernamesReturnsEmptyList() {
        when(userRepository.findAllUsername()).thenReturn(Collections.EMPTY_LIST);

        List<String> usernames = userService.findAllUsernames();

        assertTrue(usernames.isEmpty(), "Should return an empty list when no usernames are found.");
        verify(userRepository).findAllUsername();
    }

    @Test
    void testFindByUsernameFound() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> result = userService.findByUsername("testUser");

        assertTrue(result.isPresent(), "Should find the user.");
        assertEquals("testUser", result.get().getUsername(),
                "The found user's username should be 'testUser'.");
        verify(userRepository).findByUsername("testUser");
    }

    @Test
    void testFindByUsernameNotFound() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        Optional<UserEntity> result = userService.findByUsername("nonExistentUser");

        assertFalse(result.isPresent(), "Should not find a user for a non-existent username.");
        verify(userRepository).findByUsername("nonExistentUser");
    }

    @Test
    void testSaveUser() {
        userService.save(userEntity);

        verify(userRepository).save(userEntity);
    }

    @Test
    void testUpdateUser() {
        userService.update(userEntity);

        verify(userRepository).save(userEntity);
    }
}
