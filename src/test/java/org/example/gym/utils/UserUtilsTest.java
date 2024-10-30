package org.example.gym.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.example.gym.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserUtilsTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserUtils userUtils;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGenerateUsername_WhenUnique_ShouldReturnBaseUsername() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String expectedUsername = "John.Doe";

        when(userService.existsByUsername(expectedUsername)).thenReturn(false);

        // Act
        String actualUsername = userUtils.generateUsername(firstName, lastName);

        // Assert
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    void testGenerateUsername_WhenNotUnique_ShouldReturnUniqueUsername() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String baseUsername = "John.Doe";
        String expectedUsername = "John.Doe1";

        when(userService.existsByUsername(baseUsername)).thenReturn(true);
        when(userService.existsByUsername(expectedUsername)).thenReturn(false);

        // Act
        String actualUsername = userUtils.generateUsername(firstName, lastName);

        // Assert
        assertEquals(expectedUsername, actualUsername);
    }

}
