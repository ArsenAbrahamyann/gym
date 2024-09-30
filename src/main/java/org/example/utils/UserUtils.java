package org.example.utils;

import java.security.SecureRandom;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Utility class for user-related operations.
 * <p>
 * This class provides utility methods for generating usernames and passwords. It ensures that usernames
 * are unique among existing ones and generates secure random passwords.
 * </p>
 */
@Component
public class UserUtils {
    private final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final int passwordLength = 10;
    private final SecureRandom random = new SecureRandom();

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
    public  String generateUsername(String firstName, String lastName, List<String> existingUsernames) {
        String baseUsername = firstName
                + "."
                + lastName;
        String username = baseUsername;
        int serialNumber = 1;

        // I don't think passing all usernames to this method as an argument is not a good idea.
        // Because it is a very expensive operation to get all usernames.
        // I would use getUserByUsername method to check if a user with the given username exists.


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
