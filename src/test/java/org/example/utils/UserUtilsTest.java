package org.example.utils;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserUtilsTest {

    @Test
    public void testGenerateUsernameUnique() {
        List<String> existingUsernames = new ArrayList<>();
        existingUsernames.add("john.doe");
        existingUsernames.add("john.doe1");

        String username = UserUtils.generateUsername("john", "doe", existingUsernames);

        assertThat(username).isEqualTo("john.doe2");
    }

    @Test
    public void testGenerateUsernameNonUnique() {
        List<String> existingUsernames = new ArrayList<>();
        existingUsernames.add("john.doe");

        String username = UserUtils.generateUsername("john", "doe", existingUsernames);

        assertThat(username).isEqualTo("john.doe1");
    }

    @Test
    public void testGenerateUsernameEmptyList() {
        List<String> existingUsernames = new ArrayList<>();

        String username = UserUtils.generateUsername("john", "doe", existingUsernames);

        assertThat(username).isEqualTo("john.doe");
    }

    @Test
    public void testGeneratePasswordLength() {
        String password = UserUtils.generatePassword();

        assertThat(password).hasSize(10);
    }

    @Test
    public void testGeneratePasswordCharacters() {
        String password = UserUtils.generatePassword();

        assertThat(password).matches("[A-Za-z0-9]{10}");
    }
}
