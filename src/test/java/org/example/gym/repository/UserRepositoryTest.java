package org.example.gym.repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.example.gym.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the findByUsername method of UserRepository.
     * This test verifies that when a username is found, the correct UserEntity is returned.
     */
    @Test
    public void testFindByUsername_ExistingUser() {
        String username = "testUser";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setId(1L);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> result = userRepository.findByUsername(username);

        assertTrue(result.isPresent(), "User should be found");
        assertEquals(username, result.get().getUsername(), "Username should match");
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testFindByUsername_NonExistingUser() {
        String username = "nonExistingUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<UserEntity> result = userRepository.findByUsername(username);

        assertFalse(result.isPresent(), "User should not be found");
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testFindAllUsername() {
        List<String> usernames = Arrays.asList("user1", "user2", "user3");

        when(userRepository.findAllUsername()).thenReturn(usernames);

        List<String> result = userRepository.findAllUsername();

        assertNotNull(result, "Username list should not be null");
        assertEquals(3, result.size(), "Should return 3 usernames");
        assertTrue(result.contains("user1"), "Username list should contain 'user1'");
        verify(userRepository, times(1)).findAllUsername();
    }
}
