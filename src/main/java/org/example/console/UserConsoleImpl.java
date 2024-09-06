package org.example.console;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.entity.UserUtils;
import org.example.entity.dto.UserDto;
import org.example.service.UserService;
import org.modelmapper.ModelMapper;

/**
 * Implementation of user console operations for managing user data.
 * This class provides methods for creating, updating, deleting, and viewing user details.
 */
@Slf4j
public class UserConsoleImpl {
    private final UserService userService;
    private final ModelMapper modelMapper = new ModelMapper();
    private Scanner scanner = new Scanner(System.in);

    /**
     * Constructs a UserConsoleImpl instance with the specified UserService.
     *
     * @param userService the UserService instance used for user operations
     */
    public UserConsoleImpl(UserService userService) {
        this.userService = userService;
    }

    /**
     * Sets a custom Scanner instance for user input.
     *
     * @param scanner the Scanner instance to be set
     * @throws IllegalArgumentException if the provided Scanner is null
     */
    public void setScanner(Scanner scanner) {
        log.info("Entering setScanner method.");
        if (scanner
                == null) {
            log.error("Attempted to set a null scanner.");
            throw new IllegalArgumentException("Scanner cannot be null.");
        }
        this.scanner = scanner;
        log.info("Scanner has been successfully set.");
    }

    /**
     * Creates a new user based on input from the console and saves it using UserService.
     *
     * @return the created UserDto object
     */
    public UserDto createUser() {
        UserDto userDto = new UserDto();
        log.info("Starting to create a new user.");
        try {
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();
            String username = UserUtils.generateUsername(firstName, lastName);
            String password = UserUtils.generatePassword();
            System.out.print("Is active (true/false): ");
            boolean isActive = Boolean.parseBoolean(scanner.nextLine());

            userDto.setFirstName(firstName);
            userDto.setLastName(lastName);
            userDto.setUserName(username);
            userDto.setPassword(password);
            userDto.setActive(isActive);

            UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
            userService.saveUser(userEntity);

            log.info("User created with username: {}", username);
            System.out.println("User created with username: "
                    + username);
        } catch (Exception e) {
            log.error("Error occurred while creating user: ", e);
            System.out.println("An error occurred while creating the user. Please try again.");
        }
        return userDto;
    }

    /**
     * Views the details of all users.
     */
    public void viewAllUsers() {
        log.info("Starting to view all users.");
        try {
            List<UserEntity> userEntities = userService.findAllUsers();
            if (userEntities.isEmpty()) {
                log.info("No users found.");
                System.out.println("No users found.");
            } else {
                log.info("Displaying details of all users.");
                System.out.println("All Users:");
                for (UserEntity userEntity : userEntities) {
                    UserDto userDto = modelMapper.map(userEntity, UserDto.class);  // Convert to DTO
                    System.out.println("Username: "
                            + userDto.getUserName());
                    System.out.println("First Name: "
                            + userDto.getFirstName());
                    System.out.println("Last Name: "
                            + userDto.getLastName());
                    System.out.println("Active: "
                            + userDto.isActive());
                    System.out.println("--------");
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while viewing all users: ", e);
            System.out.println("An error occurred while retrieving the users. Please try again.");
        }
    }

    /**
     * Retrieves a user entity based on the specified username.
     *
     * @param username the username of the user to retrieve
     * @return an Optional containing the UserEntity if found, or an empty Optional if not found
     */
    public Optional<UserEntity> getUser(String username) {
        log.info("Retrieving User with username: {}", username);
        try {
            return userService.findUserByUsername(username);
        } catch (Exception e) {
            log.error("Error occurred while retrieving trainer with username: {}", username, e);
            return null;
        }
    }
}
