package org.example.console;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Console implementation for managing user operations.
 * <p>
 * This class provides functionality for viewing user details, including listing all usernames.
 * It interacts with the {@link UserService} to fetch and display user-related information.
 * </p>
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserConsoleImpl {

    private final UserService userService;

    /**
     * Prints all usernames to the console.
     * <p>
     * This method retrieves the list of all usernames from the {@link UserService} and prints
     * each username to the console. It is used for displaying a list of all users.
     * </p>
     */
    public void printAllUsername() {
        List<String> allUsernames = userService.getAllUsernames();
        for (String s : allUsernames) {
            System.out.println(s);
        }
    }
}
