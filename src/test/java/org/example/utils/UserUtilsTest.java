package org.example.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserUtilsTest {

    @Test
    public void testGenerateUsernameUnique() {
        // Given
        List<String> existingUsernames = new ArrayList<>();
        existingUsernames.add("john.doe");
        existingUsernames.add("john.doe1");

        // When
        String username = UserUtils.generateUsername("john", "doe", existingUsernames);

        // Then
        assertThat(username).isEqualTo("john.doe2");
    }

    @Test
    public void testGenerateUsernameNonUnique() {
        // Given
        List<String> existingUsernames = new ArrayList<>();
        existingUsernames.add("john.doe");

        // When
        String username = UserUtils.generateUsername("john", "doe", existingUsernames);

        // Then
        assertThat(username).isEqualTo("john.doe1");
    }

    @Test
    public void testGenerateUsernameEmptyList() {
        // Given
        List<String> existingUsernames = new ArrayList<>();

        // When
        String username = UserUtils.generateUsername("john", "doe", existingUsernames);

        // Then
        assertThat(username).isEqualTo("john.doe");
    }

    @Test
    public void testGeneratePasswordLength() {
        // When
        String password = UserUtils.generatePassword();

        // Then
        assertThat(password).hasSize(10);
    }

    @Test
    public void testGeneratePasswordCharacters() {
        // When
        String password = UserUtils.generatePassword();

        // Then
        assertThat(password).matches("[A-Za-z0-9]{10}"); // Ensure it only contains alphanumeric characters
    }
}
