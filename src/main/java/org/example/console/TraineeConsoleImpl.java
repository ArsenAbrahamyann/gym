package org.example.console;

import java.util.List;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.entity.dto.TraineeDto;
import org.example.service.TraineeService;
import org.example.service.UserService;
import org.example.utils.UserUtils;
import org.example.utils.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * Implementation of console-based operations for managing trainees.
 * Provides functionality for creating, updating, deleting, and viewing trainees.
 * This service interacts with the {@link TraineeService} and {@link UserService}
 * to perform operations on trainee entities and handles user input and output
 * through the console.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TraineeConsoleImpl {
    private final TraineeService traineeService;
    private final UserConsoleImpl userConsole;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ValidationUtils validationUtils;
    private Scanner scanner = new Scanner(System.in);

    /**
     * Creates a new trainee by taking input from the console.
     * Prompts the user for the trainee's first name, last name, date of birth,
     * address, and whether the trainee is active. Generates a username and
     * password for the trainee, then creates the trainee entity.
     * <p>
     * Handles possible errors during the creation process and logs relevant
     * information.
     * </p>
     */
    @SneakyThrows
    public void createTrainee() {
        log.info("Starting to create a new trainee.");

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

        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dateOfBirth = scanner.nextLine();
        validationUtils.validateBirthDate(dateOfBirth);

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setUserId(username);
        traineeDto.setDateOfBirth(dateOfBirth);
        traineeDto.setAddress(address);
        traineeDto.setUserName(username);
        traineeDto.setFirstName(firstName);
        traineeDto.setLastName(lastName);
        traineeDto.setPassword(password);
        traineeDto.setIsActive(isActive);

        TraineeEntity traineeEntity = modelMapper.map(traineeDto, TraineeEntity.class);

        traineeService.createTrainee(traineeEntity);
        log.info("Trainee created successfully with username: {}", username);
    }

    /**
     * Updates an existing trainee based on the username.
     * Prompts the user for new values for the trainee's first name, last name,
     * date of birth, and address. Validates the date of birth before updating
     * the trainee entity.
     * <p>
     * Handles possible errors during the update process and logs relevant
     * information.
     * </p>
     */
    @SneakyThrows
    public void updateTrainee() {
        log.info("Starting to update trainee.");
        userConsole.printAllUsername();
        System.out.print("Enter username of the trainee to update: ");
        String updateUsername = scanner.nextLine();
        log.info("Trainee found with username: {}", updateUsername);

        System.out.println("Enter new firstName: ");
        String newFirstName = scanner.nextLine();

        System.out.println("Enter new lastName: ");
        String newLastName = scanner.nextLine();

        System.out.print("Enter new date of birth (YYYY-MM-DD): ");
        String dateOfBirth = scanner.nextLine();
        validationUtils.validateBirthDate(dateOfBirth);

        System.out.print("Enter new address: ");
        String address = scanner.nextLine();

        TraineeEntity trainee = traineeService.getTrainee(updateUsername);
        TraineeEntity traineeDto = modelMapper.map(trainee, TraineeEntity.class);

        List<String> allUsernames = userService.getAllUsernames();
        String newUsername = UserUtils.generateUsername(newFirstName, newLastName, allUsernames);

        traineeDto.setDateOfBirth(dateOfBirth);
        traineeDto.setAddress(address);
        traineeDto.setFirstName(newFirstName);
        traineeDto.setLastName(newLastName);
        traineeDto.setUserId(newUsername);

        TraineeEntity traineeEntity = modelMapper.map(traineeDto, TraineeEntity.class);

        traineeService.deleteTrainee(updateUsername);
        traineeService.updateTrainee(traineeEntity);
        log.info("Trainee updated successfully.");

    }

    /**
     * Deletes an existing trainee based on the username.
     * Prompts the user to enter the username of the trainee to be deleted.
     * <p>
     * Handles possible errors during the deletion process and logs relevant
     * information.
     * </p>
     */
    @SneakyThrows
    public void deleteTrainee() {
        log.info("Starting to delete trainee.");
        userConsole.printAllUsername();

        System.out.print("Enter username of the trainee to delete: ");
        String username = scanner.nextLine();

        traineeService.deleteTrainee(username);
        log.info("Trainee with username: {} deleted successfully.", username);
    }

    /**
     * Displays details of a trainee based on the username.
     * Prompts the user to enter the username of the trainee whose details
     * are to be viewed. If the trainee exists, displays their username, date
     * of birth, and address.
     * <p>
     * Handles possible errors during the retrieval process and logs relevant
     * information.
     * </p>
     */
    @SneakyThrows
    public void viewTrainee() {
        log.info("Starting to view trainee details.");
        userConsole.printAllUsername();
        System.out.print("Enter username of the trainee to view: ");
        String username = scanner.nextLine();

        TraineeEntity traineeEntity = traineeService.getTrainee(username);
        TraineeDto traineeDto = modelMapper.map(traineeEntity, TraineeDto.class);
        if (traineeDto
                != null) {
            log.info("Displaying trainee details for username: {}", username);
            System.out.println("Username: "
                    +
                    traineeDto.getUserId());
            System.out.println("Date of Birth: "
                    +
                    traineeDto.getDateOfBirth());
            System.out.println("Address: "
                    +
                    traineeDto.getAddress());
        } else {
            log.warn("Trainee not found with username: {}", username);
            System.out.println("Trainee not found.");
        }

    }

    /**
     * Displays details of all trainees.
     * Retrieves and displays details of all trainees. For each trainee, shows
     * their username, date of birth, and address.
     * <p>
     * Handles possible errors during the retrieval process and logs relevant
     * information.
     * </p>
     */
    @SneakyThrows
    public void viewAllTrainee() {
        log.info("Starting to view all trainees.");
        List<TraineeEntity> trainees = traineeService.getAllTrainees();
        if (! trainees.isEmpty()) {
            log.info("Displaying all trainees.");
            for (TraineeEntity traineeEntity : trainees) {
                TraineeDto traineeDto = modelMapper.map(traineeEntity,
                        TraineeDto.class);
                System.out.println("Username: "
                        +
                        traineeDto.getUserId()
                        +
                        ", Date of Birth: "
                        +
                        traineeDto.getDateOfBirth()
                        +
                        ", Address: "
                        +
                        traineeDto.getAddress());
            }
        } else {
            log.warn("No trainees found.");
        }
    }

    /**
     * Prints the menu for managing trainees.
     * Displays the available options for managing trainees, including creating,
     * updating, deleting, viewing a specific trainee, viewing all trainees,
     * and returning to the main menu.
     */
    public void printMenu() {
        log.info("Displaying menu options for managing trainees.");
        StringBuilder sb = new StringBuilder();
        sb.append("\nManage Trainee "
                +
                "\n1. Create Trainee "
                +
                "\n2. Update Trainee "
                +
                "\n3. Delete Trainee "
                +
                "\n4. View Trainee "
                +
                "\n5. View All Trainees "
                +
                "\n6. Back to Main Menu"
                +
                "\nEnter your choice: ");
        System.out.println(sb);
    }

}
