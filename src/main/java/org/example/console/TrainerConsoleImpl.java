package org.example.console;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
import org.example.entity.UserUtils;
import org.example.entity.dto.TrainerDto;
import org.example.entity.dto.UserDto;
import org.example.service.TrainerService;

import java.util.List;
import java.util.Scanner;
import org.example.service.UserService;
import org.modelmapper.ModelMapper;

@Slf4j
public class TrainerConsoleImpl {
    private final TrainerService trainerService;
    private final UserConsoleImpl userConsole;
    private final UserService userService;
    private final ModelMapper modelMapper = new ModelMapper();
    Scanner scanner = new Scanner(System.in);

    public TrainerConsoleImpl(TrainerService trainerService, UserConsoleImpl userConsole, UserService userService) {
        this.trainerService = trainerService;
        this.userConsole = userConsole;
        this.userService = userService;
    }

    /**
     * Sets the Scanner instance used for user input.
     *
     * <p> This method allows setting a custom {@link Scanner} for reading user input.
     * If the provided scanner is null, an {@link IllegalArgumentException} will be thrown.</p>
     *
     * @param scanner the {@link Scanner} instance to be set
     * @throws IllegalArgumentException if the {@code scanner} is null
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
     * Creates a new trainer by taking input from the console.
     * Handles possible errors during the creation process.
     */
    public void createTrainer() {
        log.info("Starting to create a new trainer.");
        try {
            UserDto userDto = userConsole.createUser();
            System.out.print("Enter specialization: ");
            String specialization = scanner.nextLine();

            TrainerDto trainerDto = new TrainerDto();
            trainerDto.setUserId(userDto.getUserName());
            trainerDto.setSpecialization(specialization);

            TrainerEntity trainerEntity = modelMapper.map(trainerDto, TrainerEntity.class);
            trainerService.createTrainer(trainerEntity);
            log.info("TrainerEntity created with userId (username): {}", userDto.getUserName());
            System.out.println("TrainerEntity created with userId: " + userDto.getUserName());

        } catch (Exception e) {
            log.error("Error occurred while creating trainer: ", e);
            System.out.println("An error occurred while creating the trainer. Please try again.");
        }
    }

    /**
     * Updates an existing trainer based on the username.
     * Handles possible errors during the update process.
     */
    public void updateTrainer() {
        log.info("Starting to update trainer.");
        try {
            viewAllTrainer();
            System.out.print("Enter username of the trainer to update: ");
            String username = scanner.nextLine();
            TrainerEntity trainerEntity = getTrainer(username);
            TrainerDto updatedTrainerDto = modelMapper.map(trainerEntity, TrainerDto.class);
            if (updatedTrainerDto != null) {
                log.info("TrainerEntity found with username: {}", updatedTrainerDto.getUserId());
                UserEntity user = userConsole.getUser(updatedTrainerDto.getUserId()).orElse(null);
                UserDto userDto = modelMapper.map(user, UserDto.class);
                System.out.println("Enter new firstName: ");
                String newFirstName = scanner.nextLine();
                System.out.println("Enter new lastName: ");
                String newLastName = scanner.nextLine();
                String newUsername = UserUtils.generateUsername(newFirstName, newLastName);
                userDto.setFirstName(newFirstName);
                userDto.setLastName(newLastName);
                userDto.setUserName(newUsername);
                UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
                userService.updateUser(userEntity);
                userService.deleteUserByUsername(userDto.getUserName());
                System.out.print("Enter new specialization: ");
                String specialization = scanner.nextLine();

                updatedTrainerDto.setSpecialization(specialization);
                updatedTrainerDto.setUserId(newUsername);

                TrainerEntity trainer = modelMapper.map(updatedTrainerDto, TrainerEntity.class);
                trainerService.updateTrainer(newUsername, trainer);
                trainerService.deleteTrainer(trainer.getUserId());
                log.info("TrainerEntity updated successfully.");
                System.out.println("TrainerEntity updated.");
            } else {
                log.warn("TrainerEntity not found with username: {}", username);
                System.out.println("TrainerEntity not found.");
            }
        } catch (Exception e) {
            log.error("Error occurred while updating trainer: ", e);
            System.out.println("An error occurred while updating the trainer. Please try again.");
        }
    }

    /**
     * Deletes an existing trainer based on the username.
     * Handles possible errors during the deletion process.
     */
    public void deleteTrainer() {
        log.info("Starting to delete trainer.");
        try {
            viewAllTrainer();
            System.out.print("Enter username of the trainer to delete: ");
            String username = scanner.nextLine();
            userService.deleteUserByUsername(username);
            trainerService.deleteTrainer(username);
            log.info("Trainer with username: {} deleted successfully.", username);
            System.out.println("Trainer deleted.");
        } catch (Exception e) {
            log.error("Error occurred while deleting trainer: ", e);
            System.out.println("An error occurred while deleting the trainer. Please try again.");
        }
    }

    /**
     * Displays details of a trainer based on the username.
     * Handles possible errors during the retrieval process.
     */
    public void viewTrainer() {
        log.info("Starting to view trainer details.");
        try {
            userConsole.viewAllUsers();
            System.out.print("Enter username of the trainer to view: ");
            String username = scanner.nextLine();

            TrainerEntity trainerEntity = getTrainer(username);
            if (trainerEntity != null) {
                TrainerDto trainerDto = modelMapper.map(trainerEntity, TrainerDto.class);
                log.info("Displaying trainerEntity details for username: {}", username);
                System.out.println("TrainerEntity Details:");
                System.out.println("Username: " + trainerDto.getUserId());
                System.out.println("Specialization: " + trainerDto.getSpecialization());
            } else {
                log.warn("TrainerEntity not found with username: {}", username);
                System.out.println("TrainerEntity not found.");
            }
        } catch (Exception e) {
            log.error("Error occurred while viewing trainer details: ", e);
            System.out.println("An error occurred while retrieving the trainer details. Please try again.");
        }
    }

    /**
     * Displays details of all trainers.
     * Handles possible errors during the retrieval process.
     */
    public void viewAllTrainer() {
        log.info("Starting to view all trainers.");
        try {
            List<TrainerEntity> trainerEntities = getAllTrainers();
            if (trainerEntities.isEmpty()) {
                log.info("No trainerEntities found.");
                System.out.println("No trainer found.");
            } else {
                log.info("Displaying details of all trainerEntities.");
                System.out.println("All Trainers:");
                for (TrainerEntity trainerEntity : trainerEntities) {
                    TrainerDto trainerDto = modelMapper.map(trainerEntity, TrainerDto.class);
                    System.out.println("Username: " + trainerDto.getUserId());
                    System.out.println("Specialization: " + trainerDto.getSpecialization());
                    System.out.println("--------");
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while viewing all trainers: ", e);
            System.out.println("An error occurred while retrieving the trainers. Please try again.");
        }
    }

    /**
     * Retrieves a trainer based on the username.
     * Handles possible errors during the retrieval process.
     *
     * @param userId the username of the trainer
     * @return the TrainerEntity object if found, null otherwise
     */
    public TrainerEntity getTrainer(String userId) {
        log.info("Retrieving trainer with username: {}", userId);
        try {
            return trainerService.getTrainer(userId);
        } catch (Exception e) {
            log.error("Error occurred while retrieving trainer with username: {}", userId, e);
            return null;
        }
    }

    /**
     * Retrieves all trainers.
     * Handles possible errors during the retrieval process.
     *
     * @return a list of all trainers
     */
    public List<TrainerEntity> getAllTrainers() {
        log.info("Retrieving all trainers.");
        try {
            return trainerService.getAllTrainers();
        } catch (Exception e) {
            log.error("Error occurred while retrieving all trainers: ", e);
            return null;
        }
    }

    /**
     * Prints the menu for managing trainers.
     */
    public void printMenu() {
        log.info("Displaying menu options for managing trainers.");
        StringBuilder sb = new StringBuilder();
        sb.append("\nManage TrainerEntity " +
                  "\n1. Create TrainerEntity " +
                  "\n2. Update TrainerEntity " +
                  "\n3. Delete TrainerEntity " +
                  "\n4. View TrainerEntity " +
                  "\n5. View All Trainers " +
                  "\n6. Back to Main Menu" +
                  "\nEnter your choice: ");
        System.out.println(sb);
    }
}
