package org.example.console;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.entity.UserUtils;
import org.example.service.UserService;

@Slf4j
public class UserConsoleImpl {
    private final UserService userService;
    private Scanner scanner = new Scanner(System.in);

    public UserConsoleImpl(UserService userService) {
        this.userService = userService;
    }

    public UserEntity createUser() {
        UserEntity userEntity = new UserEntity();
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

            userEntity.setFirstName(firstName);
            userEntity.setLastName(lastName);
            userEntity.setUserName(username);
            userEntity.setPassword(password);
            userEntity.setActive(isActive);

            userService.saveUser(userEntity);
            log.info("UserEntity created with username: {}", username);
            System.out.println("UserEntity created with username: " + username);


        } catch (Exception e) {
            log.error("Error occurred while creating user: ", e);
            System.out.println("An error occurred while creating the user. Please try again.");
        }
        return userEntity;
    }

    public void updateUser(String username) {
        log.info("Starting to update user.");
        try {
            UserEntity userEntity = userService.findUserByUsername(username)
                .orElse(null);

            if (userEntity != null) {
                log.info("UserEntity found with username: {}", username);
                System.out.println();
                System.out.print("Enter new first name (or press Enter to keep current): ");
                String firstName = scanner.nextLine();
                System.out.print("Enter new last name (or press Enter to keep current): ");
                String lastName = scanner.nextLine();
                System.out.print("Is active (true/false, or press Enter to keep current): ");
                String isActiveInput = scanner.nextLine();

                if (!firstName.isEmpty()) {
                    userEntity.setFirstName(firstName);
                }
                if (!lastName.isEmpty()) {
                    userEntity.setLastName(lastName);
                }
                if (!isActiveInput.isEmpty()) {
                    userEntity.setActive(Boolean.parseBoolean(isActiveInput));
                }

                userService.updateUser(username,userEntity);
                log.info("UserEntity updated successfully.");
                System.out.println("UserEntity updated.");
            } else {
                log.warn("UserEntity not found with username: {}", username);
                System.out.println("UserEntity not found.");
            }
        } catch (Exception e) {
            log.error("Error occurred while updating user: ", e);
            System.out.println("An error occurred while updating the user. Please try again.");
        }
    }

    public void deleteUser(String username) {
        log.info("Starting to delete user.");
        try {


            UserEntity userEntity = userService.findUserByUsername(username)
                .orElse(null);

            if (userEntity != null) {
                userService.deleteUserByUsername(username);
                log.info("UserEntity with username: {} deleted successfully.", username);
                System.out.println("UserEntity deleted.");
            } else {
                log.warn("UserEntity not found with username: {}", username);
                System.out.println("UserEntity not found.");
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting user: ", e);
            System.out.println("An error occurred while deleting the user. Please try again.");
        }
    }

    public void viewUser() {
        log.info("Starting to view user details.");
        try {
            System.out.print("Enter username of the user to view: ");
            String username = scanner.nextLine();

            UserEntity userEntity = userService.findUserByUsername(username)
                .orElse(null);

            if (userEntity != null) {
                log.info("Displaying user details for username: {}", username);
                System.out.println("UserEntity Details:");
                System.out.println("Username: " + userEntity.getUserName());
                System.out.println("First Name: " + userEntity.getFirstName());
                System.out.println("Last Name: " + userEntity.getLastName());
                System.out.println("Active: " + userEntity.isActive());
            } else {
                log.warn("UserEntity not found with username: {}", username);
                System.out.println("UserEntity not found.");
            }
        } catch (Exception e) {
            log.error("Error occurred while viewing user details: ", e);
            System.out.println("An error occurred while retrieving the user details. Please try again.");
        }
    }

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
                    System.out.println("Username: " + userEntity.getUserName());
                    System.out.println("First Name: " + userEntity.getFirstName());
                    System.out.println("Last Name: " + userEntity.getLastName());
                    System.out.println("Active: " + userEntity.isActive());
                    System.out.println("--------");
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while viewing all users: ", e);
            System.out.println("An error occurred while retrieving the users. Please try again.");
        }
    }

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
