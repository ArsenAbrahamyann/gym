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

@Slf4j
public class UserConsoleImpl {
    private final UserService userService;
    private final ModelMapper modelMapper = new ModelMapper();
    private Scanner scanner = new Scanner(System.in);

    public UserConsoleImpl(UserService userService) {
        this.userService = userService;
    }

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
            System.out.println("User created with username: " + username);
        } catch (Exception e) {
            log.error("Error occurred while creating user: ", e);
            System.out.println("An error occurred while creating the user. Please try again.");
        }
        return userDto;
    }

    public void updateUser(String username) {
        log.info("Starting to update user.");
        try {
            Optional<UserEntity> userEntityOptional = userService.findUserByUsername(username);
            if (userEntityOptional.isPresent()) {
                UserEntity userEntity = userEntityOptional.get();
                UserDto userDto = modelMapper.map(userEntity, UserDto.class);

                System.out.print("Enter new first name (or press Enter to keep current): ");
                String firstName = scanner.nextLine();
                System.out.print("Enter new last name (or press Enter to keep current): ");
                String lastName = scanner.nextLine();
                System.out.print("Is active (true/false, or press Enter to keep current): ");
                String isActiveInput = scanner.nextLine();

                if (!firstName.isEmpty()) {
                    userDto.setFirstName(firstName);
                }
                if (!lastName.isEmpty()) {
                    userDto.setLastName(lastName);
                }
                if (!isActiveInput.isEmpty()) {
                    userDto.setActive(Boolean.parseBoolean(isActiveInput));
                }

                UserEntity updatedEntity = modelMapper.map(userDto, UserEntity.class);
                userService.updateUser(updatedEntity);

                log.info("User updated successfully.");
                System.out.println("User updated.");
            } else {
                log.warn("User not found with username: {}", username);
                System.out.println("User not found.");
            }
        } catch (Exception e) {
            log.error("Error occurred while updating user: ", e);
            System.out.println("An error occurred while updating the user. Please try again.");
        }
    }

    public void deleteUser(String username) {
        log.info("Starting to delete user.");
        try {
            Optional<UserEntity> userEntityOptional = userService.findUserByUsername(username);
            if (userEntityOptional.isPresent()) {
                userService.deleteUserByUsername(username);
                log.info("User deleted successfully.");
                System.out.println("User deleted.");
            } else {
                log.warn("User not found with username: {}", username);
                System.out.println("User not found.");
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

            Optional<UserEntity> userEntityOptional = userService.findUserByUsername(username);
            if (userEntityOptional.isPresent()) {
                UserEntity userEntity = userEntityOptional.get();
                UserDto userDto = modelMapper.map(userEntity, UserDto.class);  // Convert to DTO

                log.info("Displaying user details for username: {}", username);
                System.out.println("User Details:");
                System.out.println("Username: " + userDto.getUserName());
                System.out.println("First Name: " + userDto.getFirstName());
                System.out.println("Last Name: " + userDto.getLastName());
                System.out.println("Active: " + userDto.isActive());
            } else {
                log.warn("User not found with username: {}", username);
                System.out.println("User not found.");
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
                    UserDto userDto = modelMapper.map(userEntity, UserDto.class);  // Convert to DTO
                    System.out.println("Username: " + userDto.getUserName());
                    System.out.println("First Name: " + userDto.getFirstName());
                    System.out.println("Last Name: " + userDto.getLastName());
                    System.out.println("Active: " + userDto.isActive());
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
