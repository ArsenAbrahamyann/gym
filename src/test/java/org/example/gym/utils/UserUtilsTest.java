package org.example.gym.utils;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.example.gym.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class UserUtilsTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserUtils userUtils;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userUtils, "characters", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*");
        ReflectionTestUtils.setField(userUtils, "passwordLength", 10);
    }

    @Test
    void testGenerateUsername_WhenUnique_ShouldReturnBaseUsername() {
        String firstName = "John";
        String lastName = "Doe";
        String expectedUsername = "John.Doe";

        when(userService.existsByUsername(expectedUsername)).thenReturn(false);

        String actualUsername = userUtils.generateUsername(firstName, lastName);

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    void testGenerateUsername_WhenNotUnique_ShouldReturnUniqueUsername() {
        String firstName = "John";
        String lastName = "Doe";
        String baseUsername = "John.Doe";
        String expectedUsername = "John.Doe1";

        when(userService.existsByUsername(baseUsername)).thenReturn(true);
        when(userService.existsByUsername(expectedUsername)).thenReturn(false);

        String actualUsername = userUtils.generateUsername(firstName, lastName);

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    void testGenerateUsername_WhenMultipleAttemptsNeeded_ShouldReturnNextAvailableUsername() {
        String firstName = "John";
        String lastName = "Doe";
        String baseUsername = "John.Doe";
        String expectedUsername = "John.Doe3";

        when(userService.existsByUsername(baseUsername)).thenReturn(true);
        when(userService.existsByUsername("John.Doe1")).thenReturn(true);
        when(userService.existsByUsername("John.Doe2")).thenReturn(true);
        when(userService.existsByUsername(expectedUsername)).thenReturn(false);

        String actualUsername = userUtils.generateUsername(firstName, lastName);

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    void testGeneratePassword_ShouldReturnPasswordOfConfiguredLength() {
        int expectedLength = 10;

        String password = userUtils.generatePassword();

        assertNotNull(password);
        assertEquals(expectedLength, password.length());
    }

    @Test
    void testGeneratePassword_ShouldContainOnlyAllowedCharacters() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        ReflectionTestUtils.setField(userUtils, "characters", characters);

        String password = userUtils.generatePassword();

        assertNotNull(password);
        for (char c : password.toCharArray()) {
            assertTrue(characters.indexOf(c) >= 0, "Password contains invalid character: " + c);
        }
    }

}
