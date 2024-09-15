package org.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEntity user;

    /**
     * Sets up the test environment by initializing a {@link UserEntity} instance with predefined sample data.
     * This method ensures that a {@code UserEntity} object with specific attributes is available for use in test methods,
     * providing a consistent starting state for each test.
     */
    @BeforeEach
    public void setUp() {
        user = new UserEntity();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setIsActive(true);
    }

    @Test
    public void testFindByUsername() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // Act
        Optional<UserEntity> result = userRepository.findByUsername("testuser");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user.getUsername(), result.get().getUsername());
        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    public void testFindAllUsername() {
        // Arrange
        List<String> usernames = new ArrayList<>();
        usernames.add("testuser");
        when(userRepository.findAllUsername()).thenReturn(Optional.of(usernames));

        // Act
        Optional<List<String>> result = userRepository.findAllUsername();

        // Assert
        assertTrue(result.isPresent());
        assertEquals(usernames.size(), result.get().size());
        assertEquals("testuser", result.get().get(0));
        verify(userRepository, times(1)).findAllUsername();
    }

    @Test
    public void testSave() {
        // Arrange
        doNothing().when(userRepository).save(any(UserEntity.class));

        // Act
        userRepository.save(user);

        // Assert
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testUpdate() {
        // Arrange
        doNothing().when(userRepository).update(any(UserEntity.class));

        // Act
        userRepository.update(user);

        // Assert
        verify(userRepository, times(1)).update(any(UserEntity.class));
    }

    @Test
    public void testDeleteByUsername() {
        // Arrange
        doNothing().when(userRepository).deleteByUsername(anyString());

        // Act
        userRepository.deleteByUsername("testuser");

        // Assert
        verify(userRepository, times(1)).deleteByUsername(anyString());
    }
}
