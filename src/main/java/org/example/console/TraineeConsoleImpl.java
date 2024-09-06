package org.example.console;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.entity.UserEntity;
import org.example.entity.UserUtils;
import org.example.entity.dto.TraineeDto;
import org.example.entity.dto.UserDto;
import org.example.service.TraineeService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import org.example.service.UserService;
import org.modelmapper.ModelMapper;

/**
 * Implementation of console-based operations for managing trainees.
 * Provides functionality for creating, updating, deleting, and viewing trainees.
 */
@Slf4j
public class TraineeConsoleImpl {
    private final TraineeService traineeService;
    private final UserConsoleImpl userConsole;
    private final UserService userService;
    private final ModelMapper modelMapper = new ModelMapper();
    private Scanner scanner = new Scanner(System.in);

    public TraineeConsoleImpl(TraineeService traineeService, UserConsoleImpl userConsole, UserService userService) {
        this.traineeService = traineeService;
        this.userConsole = userConsole;
        this.userService = userService;
    }

    /**
     * Sets the Scanner instance used for user input.
     *
     * @param scanner the Scanner instance to be set
     * @throws IllegalArgumentException if the scanner is null
     */
    public void setScanner(Scanner scanner) {
        log.info("Entering setScanner method.");
        if (scanner == null) {
            log.error("Attempted to set a null scanner.");
            throw new IllegalArgumentException("Scanner cannot be null.");
        }
        this.scanner = scanner;
        log.info("Scanner has been successfully set.");
    }

    /**
     * Updates an existing trainee based on the username.
     * Handles possible errors during the update process.
     */
    public void updateTrainee() {
        log.info("Starting to update trainee.");
        try {
            viewAllTrainee();
            System.out.print("Enter username of the trainee to update: ");
            String username = scanner.nextLine();

            TraineeEntity traineeEntity = traineeService.getTrainee(username);
            if (traineeEntity != null) {
                log.info("Trainee found with username: {}", username);
                Optional<UserEntity> userOptional = userConsole.getUser(traineeEntity.getUserId());
                if (userOptional.isPresent()) {
                    UserEntity userEntity = userOptional.get();
                    UserDto userDto = modelMapper.map(userEntity, UserDto.class);

                    System.out.println("Enter new firstName: ");
                    String newFirstName = scanner.nextLine();
                    System.out.println("Enter new lastName: ");
                    String newLastName = scanner.nextLine();
                    String newUsername = UserUtils.generateUsername(newFirstName, newLastName);
                    userDto.setFirstName(newFirstName);
                    userDto.setLastName(newLastName);
                    userDto.setUserName(newUsername);
                    userService.updateUser(
                        modelMapper.map(userDto, UserEntity.class));

                    System.out.print("Enter new date of birth (YYYY-MM-DD): ");
                    String dateOfBirth = scanner.nextLine();
                    LocalDate traineeDateOfBirth;
                    try {
                        traineeDateOfBirth = LocalDate.parse(dateOfBirth);
                    } catch (DateTimeParseException e) {
                        log.warn("Invalid date format: {}", dateOfBirth);
                        System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                        return;
                    }
                    System.out.print("Enter new address: ");
                    String address = scanner.nextLine();

                    traineeEntity.setLocalDateTime(dateOfBirth);
                    traineeEntity.setAddress(address);
                    traineeEntity.setUserId(newUsername);

                    traineeService.updateTrainee(traineeEntity);
                    log.info("Trainee updated successfully.");
                }
            } else {
                log.warn("Trainee not found with username: {}", username);
                System.out.println("Trainee not found.");
            }
        } catch (Exception e) {
            log.error("Error occurred while updating trainee: ", e);
            System.out.println("An error occurred while updating the trainee. Please try again.");
        }
    }

    /**
     * Creates a new trainee by taking input from the console.
     * Handles possible errors during the creation process.
     */
    public void createTrainee() {
        log.info("Starting to create a new trainee.");
        try {
            UserDto userDto = userConsole.createUser();
            UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
            userService.saveUser(userEntity);

            System.out.print("Enter date of birth (YYYY-MM-DD): ");
            String dateOfBirth = scanner.nextLine();
            LocalDate traineeDateOfBirth;
            try {
                traineeDateOfBirth = LocalDate.parse(dateOfBirth);
            } catch (DateTimeParseException e) {
                log.warn("Invalid date format: {}", dateOfBirth);
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                return;
            }
            System.out.print("Enter address: ");
            String address = scanner.nextLine();

            TraineeDto traineeDto = new TraineeDto();
            traineeDto.setUserId(userDto.getUserName());
            traineeDto.setLocalDateTime(dateOfBirth.toString());
            traineeDto.setAddress(address);

            traineeService.createTrainee(
                modelMapper.map(traineeDto, TraineeEntity.class));
            log.info("Trainee created successfully with username: {}", userDto.getUserName());
        } catch (Exception e) {
            log.error("Error occurred while creating trainee: ", e);
            System.out.println("An error occurred while creating the trainee. Please try again.");
        }
    }

        /**
         * Deletes an existing trainee based on the username.
         * Handles possible errors during the deletion process.
         */
        public void deleteTrainee () {
            log.info("Starting to delete trainee.");
            try {
                viewAllTrainee();
                System.out.print("Enter username of the trainee to delete: ");
                String username = scanner.nextLine();
                userService.deleteUserByUsername(username);
                traineeService.deleteTrainee(username);
                log.info("Trainee with username: {} deleted successfully.", username);
                System.out.println("Trainee deleted.");
            } catch (Exception e) {
                log.error("Error occurred while deleting trainee: ", e);
                System.out.println("An error occurred while deleting the trainee. Please try again.");
            }
        }

        /**
         * Displays details of a trainee based on the username.
         * Handles possible errors during the retrieval process.
         */
        public void viewTrainee () {
            log.info("Starting to view trainee details.");
            try {
                System.out.print("Enter username of the trainee to view: ");
                String username = scanner.nextLine();

                TraineeEntity traineeEntity = traineeService.getTrainee(username);
                if (traineeEntity != null) {
                    TraineeDto traineeDto = modelMapper.map(traineeEntity, TraineeDto.class);
                    log.info("Displaying trainee details for username: {}", username);
                    System.out.println("Trainee Details:");
                    System.out.println("Username: " + traineeDto.getUserId());
                    System.out.println("Date of Birth: " + traineeDto.getLocalDateTime());
                    System.out.println("Address: " + traineeDto.getAddress());
                } else {
                    log.warn("Trainee not found with username: {}", username);
                    System.out.println("Trainee not found.");
                }
            } catch (Exception e) {
                log.error("Error occurred while viewing trainee details: ", e);
                System.out.println("An error occurred while retrieving the trainee details. Please try again.");
            }
        }
        /**
         * Displays details of all trainees.
         * Handles possible errors during the retrieval process.
         */
        public void viewAllTrainee () {
            log.info("Starting to view all trainees.");
            try {
                List<TraineeEntity> trainees = traineeService.getAllTrainees();
                if (!trainees.isEmpty()) {
                    log.info("Displaying all trainees.");
                    System.out.println("All Trainees:");
                    for (TraineeEntity traineeEntity : trainees) {
                        TraineeDto traineeDto = modelMapper.map(traineeEntity, TraineeDto.class);  // Using ModelMapper to map entity to DTO
                        System.out.println("Username: " + traineeDto.getUserId() + ", Date of Birth: " +
                                           traineeDto.getLocalDateTime() + ", Address: " + traineeDto.getAddress());
                    }
                } else {
                    log.warn("No trainees found.");
                    System.out.println("No trainees found.");
                }
            } catch (Exception e) {
                log.error("Error occurred while viewing all trainees: ", e);
                System.out.println("An error occurred while retrieving the list of trainees. Please try again.");
            }
        }

        /**
         * Prints the menu for managing trainees.
         */
        public void printMenu () {
            log.info("Displaying menu options for managing trainees.");
            StringBuilder sb = new StringBuilder();
            sb.append("\nManage Trainee " +
                      "\n1. Create Trainee " +
                      "\n2. Update Trainee " +
                      "\n3. Delete Trainee " +
                      "\n4. View Trainee " +
                      "\n5. View All Trainees " +
                      "\n6. Back to Main Menu" +
                      "\nEnter your choice: ");
            System.out.println(sb);
        }

}
