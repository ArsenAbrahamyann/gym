package org.example.console;

import java.util.List;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainerEntity;
import org.example.entity.dto.TrainerDto;
import org.example.service.TrainerService;
import org.example.service.UserService;
import org.example.utils.UserUtils;
import org.example.utils.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * Implementation of the console-based user interface for managing trainers.
 * <p>
 * This class provides functionality for creating, updating, deleting, and viewing trainers.
 * It interacts with the {@link TrainerService} and {@link UserService} to perform these operations.
 * </p>
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TrainerConsoleImpl {
    private final TrainerService trainerService;
    private final UserConsoleImpl userConsole;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ValidationUtils validationUtils;
    private Scanner scanner = new Scanner(System.in);

    /**
     * Creates a new trainer by taking input from the console.
     * <p>
     * This method prompts the user for the trainer's first name, last name, active status, and specialization.
     * It generates a unique username and password, creates a {@link TrainerDto} object, and then maps it to a
     * {@link TrainerEntity} before saving it using {@link TrainerService}.
     * </p>
     *
     * @throws Exception if an error occurs during input or creation process.
     */
    @SneakyThrows
    public void createTrainer() {
        log.info("Starting to create a new trainer.");

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        List<String> existingUsernames = userService.getAllUsernames();
        String username = UserUtils.generateUsername(firstName, lastName, existingUsernames);
        String password = UserUtils.generatePassword();

        System.out.print("Is active (true/false): ");
        String isActive = scanner.nextLine();
        validationUtils.isValidBoolean(isActive);

        System.out.print("Enter specialization: ");
        String specialization = scanner.nextLine();

        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setUserId(username);
        trainerDto.setSpecialization(specialization);
        trainerDto.setIsActive(isActive);
        trainerDto.setPassword(password);
        trainerDto.setLastName(lastName);
        trainerDto.setFirstName(firstName);
        trainerDto.setUserName(username);

        TrainerEntity trainerEntity = modelMapper.map(trainerDto, TrainerEntity.class);

        trainerService.createTrainer(trainerEntity);

        log.info("TrainerEntity created with userId (username): {}", username);

    }

    /**
     * Updates an existing trainer based on the username.
     * <p>
     * This method first displays all trainers and prompts the user to enter the username of the trainer to be updated.
     * It then updates the trainer's details including username and specialization.
     * </p>
     *
     * @throws Exception if an error occurs during the update process.
     */
    @SneakyThrows
    public void updateTrainer() {
        log.info("Starting to update trainer.");

        System.out.print("Enter username of the trainer to update: ");
        userConsole.printAllUsername();
        String updateUsername = scanner.nextLine();

        log.info("Trainee found with username: {}", updateUsername);

        System.out.println("Enter new firstName: ");
        String newFirstName = scanner.nextLine();

        System.out.println("Enter new lastName: ");
        String newLastName = scanner.nextLine();

        System.out.print("Enter new specialization: ");
        String newSpecialization = scanner.nextLine();

        TrainerEntity trainer = trainerService.getTrainer(updateUsername);
        TrainerEntity trainerDto = modelMapper.map(trainer, TrainerEntity.class);

        trainerDto.setSpecialization(newSpecialization);
        trainerDto.setFirstName(newFirstName);
        trainerDto.setLastName(newLastName);

        TrainerEntity trainerEntity = modelMapper.map(trainerDto, TrainerEntity.class);

        trainerService.updateTrainer(updateUsername, trainerEntity);
        log.info("TrainerEntity updated successfully.");

    }

    /**
     * Displays details of a trainer based on the username.
     * <p>
     * This method prompts the user to enter the username of the trainer to be viewed and then displays the trainer's details.
     * </p>
     *
     * @throws Exception if an error occurs during the retrieval process.
     */
    @SneakyThrows
    public void viewTrainer() {
        log.info("Starting to view trainer details.");
        userConsole.printAllUsername();

        System.out.print("Enter username of the trainer to view: ");
        String username = scanner.nextLine();

        TrainerEntity trainerEntity = trainerService.getTrainer(username);
        if (trainerEntity
                != null) {
            TrainerDto trainerDto = modelMapper.map(trainerEntity, TrainerDto.class);
            log.info("Displaying trainerEntity details for username: {}", username);
            System.out.println("Username: "
                    + trainerDto.getUserId());
            System.out.println("Specialization: "
                    + trainerDto.getSpecialization());
        } else {
            log.warn("TrainerEntity not found with username: {}", username);
            System.out.println("TrainerEntity not found.");
        }
    }

    /**
     * Displays details of all trainers.
     * <p>
     * This method retrieves and displays the details of all trainers.
     * </p>
     *
     * @throws Exception if an error occurs during the retrieval process.
     */
    @SneakyThrows
    public void viewAllTrainer() {
        log.info("Starting to view all trainers.");

        List<TrainerEntity> trainerEntities = trainerService.getAllTrainers();
        if (trainerEntities.isEmpty()) {
            log.info("No trainerEntities found.");
        } else {
            log.info("Displaying details of all trainerEntities.");
            for (TrainerEntity trainerEntity : trainerEntities) {
                TrainerDto trainerDto = modelMapper.map(trainerEntity, TrainerDto.class);
                System.out.println("Username: "
                        + trainerDto.getUserId());
                System.out.println("Specialization: "
                        + trainerDto.getSpecialization());
                System.out.println("--------");
            }
        }
    }

    /**
     * Prints the menu for managing trainers.
     * <p>
     * This method displays the options available for managing trainers.
     * </p>
     */
    public void printMenu() {
        log.info("Displaying menu options for managing trainers.");
        String s = """

                Manage TrainerEntity
                1. Create TrainerEntity
                2. Update TrainerEntity
                3. View TrainerEntity
                4. View All Trainers
                5. Back to Main Menu
                Enter your choice:
                """;
        System.out.println(s);
    }
}
