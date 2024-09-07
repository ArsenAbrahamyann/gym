package org.example.utils;

import java.security.SecureRandom;
import java.util.List;

/**
 * Utility class for user-related operations.
 * <p>
 * This class provides utility methods for generating usernames and passwords. It ensures that usernames
 * are unique among existing ones and generates secure random passwords.
 * </p>
 */
public class UserUtils {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 10;
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates a unique username based on the provided first name and last name.
     * <p>
     * This method creates a base username by combining the first name and last name. It then appends a serial
     * number to the username if it already exists in the list of existing usernames to ensure uniqueness.
     * </p>
     *
     * @param firstName         the first name of the user
     * @param lastName          the last name of the user
     * @param existingUsernames a list of usernames that already exist to check against
     * @return a unique username
     */
    public static String generateUsername(String firstName, String lastName, List<String> existingUsernames) {
        String baseUsername = firstName
                + "."
                + lastName;
        String username = baseUsername;
        int serialNumber = 1;

        while (existingUsernames.contains(username)) {
            username = baseUsername
                    + serialNumber;
            serialNumber++;
        }

        return username;
    }

    /**
     * Generates a random password of a fixed length.
     * <p>
     * This method creates a password by selecting random characters from a predefined set of characters.
     * The password length is specified by the {@link #PASSWORD_LENGTH} constant.
     * </p>
     *
     * @return a randomly generated password
     */
    public static String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }
}
