package org.example.gym.utils;

import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import org.example.gym.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class for user-related operations.
 * <p>
 * This class provides utility methods for generating usernames and passwords. It ensures that usernames
 * are unique among existing ones and generates secure random passwords.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class UserUtils {
    private final UserService userService;
    @Value("${user.password.characters}")
    private  String characters;
    @Value("${user.password.length:10}")
    private  Integer passwordLength;
    private final SecureRandom random;

    /**
     * Generates a unique username based on the provided first name and last name.
     * <p>
     * This method creates a base username by combining the first name and last name. It then appends a serial
     * number to the username if it already exists in the list of existing usernames to ensure uniqueness.
     * </p>
     *
     * @param firstName         the first name of the user
     * @param lastName          the last name of the user
     * @return a unique username
     */
    public  String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int serialNumber = 1;

        // TODO why while? And why is it very very bad idea? You should use correct SQL
        while (userService.existsByUsername(username)) {
            username = baseUsername + serialNumber;
            serialNumber++;
        }

        return username;
    }

    /**
     * Generates a random password of a fixed length.
     * <p>
     * This method creates a password by selecting random characters from a predefined set of characters.
     * The password length is specified by the {@link #passwordLength} constant.
     * </p>
     *
     * @return a randomly generated password
     */
    public  String generatePassword() {
        StringBuilder password = new StringBuilder(passwordLength);
        for (int i = 0; i < passwordLength; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }
}
