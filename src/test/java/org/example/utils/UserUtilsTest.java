package org.example.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserUtilsTest {
    private final UserUtils userUtils = new UserUtils();

    @Test
    public void testGenerateUsernameUnique() {
        List<String> existingUsernames = Arrays.asList("john.doe", "john.doe1", "john.doe2");
        String username = userUtils.generateUsername("john", "doe", existingUsernames);
        assertEquals("john.doe3", username, "Username should be unique and not exist in the list.");
    }

    @Test
    public void testGenerateUsernameBaseCase() {
        List<String> existingUsernames = Arrays.asList("john.doe");
        String username = userUtils.generateUsername("john", "doe", existingUsernames);
        assertEquals("john.doe1", username, "Username should be generated "
                + "with a serial number if the base username exists.");
    }


    @Test
    public void testGeneratePasswordLength() {
        String password = userUtils.generatePassword();
        assertEquals(10, password.length(), "Password length should be 10.");
    }

    @Test
    public void testGeneratePasswordCharacters() {
        String password = userUtils.generatePassword();
        assertTrue(password.chars().allMatch(ch -> Character.isLetterOrDigit(ch)),
                "Password should only contain letters and digits.");
    }
}
