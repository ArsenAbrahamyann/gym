package org.example.console;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.entity.dto.TraineeDto;
import org.example.entity.dto.TrainerDto;
import org.example.entity.dto.TrainingDto;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import org.modelmapper.ModelMapper;

/**
 * Console implementation for managing trainings.
 * <p>
 * This class provides methods for creating, updating, deleting, and viewing training entities.
 * It interacts with various services and handles user input via the console.
 * </p>
 */
@Slf4j
public class TrainingConsoleImpl {
    private final TrainingService trainingService;
    private final TraineeConsoleImpl traineeConsole;
    private final TrainerConsoleImpl trainerConsole;
    private final UserConsoleImpl userConsole;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final ModelMapper modelMapper = new ModelMapper();
    private Scanner scanner = new Scanner(System.in);

    /**
     * Constructs a new {@link TrainingConsoleImpl} instance with the provided services and consoles.
     *
     * @param trainingService the service for managing training entities
     * @param traineeConsole  the console for managing trainee entities
     * @param trainerConsole  the console for managing trainer entities
     * @param userConsole     the console for managing user entities
     * @param traineeService  the service for managing trainee entities
     * @param trainerService  the service for managing trainer entities
     */
    public TrainingConsoleImpl(TrainingService trainingService, TraineeConsoleImpl traineeConsole,
                               TrainerConsoleImpl trainerConsole, UserConsoleImpl userConsole,
                               TraineeService traineeService, TrainerService trainerService) {
        this.trainingService = trainingService;
        this.traineeConsole = traineeConsole;
        this.trainerConsole = trainerConsole;
        this.userConsole = userConsole;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    /**
     * Sets a new scanner instance for input operations.
     *
     * @param scanner the scanner to set
     * @throws IllegalArgumentException if the provided scanner is null
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
     * Creates a new training by taking input from the console.
     * <p>
     * Handles various input validations, including date and duration formats.
     * Creates the training entity using the provided details and saves it via the training service.
     * </p>
     */
    public void createTraining() {
        log.info("Starting to create a new training.");
        try {
            TrainingDto trainingDto = new TrainingDto();
            traineeConsole.viewAllTrainee();
            System.out.print("Enter trainee username: ");
            String traineeUsername = scanner.nextLine();
            TraineeEntity trainee = traineeService.getTrainee(traineeUsername);
            if (trainee
                    == null) {
                System.out.println("Trainee not found.");
                return;
            }
            trainingDto.setTraineeId(traineeUsername);
            trainerConsole.viewAllTrainer();
            System.out.print("Enter trainer username: ");
            String trainerUsername = scanner.nextLine();
            TrainerEntity trainer = trainerService.getTrainer(trainerUsername);
            if (trainer
                    == null) {
                System.out.println("Trainer not found.");
                return;
            }
            trainingDto.setTrainerId(trainerUsername);
            System.out.print("Enter training name: ");
            String trainingName = scanner.nextLine();

            System.out.print("Enter training type: ");
            String trainingTypeName = scanner.nextLine();
            TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity(trainingTypeName);

            System.out.print("Enter training date (YYYY-MM-DD): ");
            String trainingDateInput = scanner.nextLine();
            LocalDate trainingDate;
            try {
                trainingDate = LocalDate.parse(trainingDateInput);
            } catch (DateTimeParseException e) {
                log.warn("Invalid training date format: {}", trainingDateInput);
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                return;
            }

            System.out.print("Enter training duration (HH:MM): ");
            String trainingDurationInput = scanner.nextLine();
            Duration trainingDuration;
            try {
                String[] parts = trainingDurationInput.split(":");
                if (parts.length
                        != 2) {
                    throw new NumberFormatException("Duration must be in HH:MM format.");
                }
                int hours = Integer.parseInt(parts[0]);
                int minutes = Integer.parseInt(parts[1]);
                trainingDuration = Duration.ofHours(hours).plusMinutes(minutes);
            } catch (NumberFormatException e) {
                log.warn("Invalid training duration format: {}", trainingDurationInput);
                System.out.println("Invalid duration format. Please use HH:MM.");
                return;
            }
            trainingDto.setTrainingName(trainingName);
            trainingDto.setTrainingTypeEntity(trainingTypeEntity);
            trainingDto.setTrainingDate(trainingDate.toString());
            trainingDto.setTrainingDuration(trainingDuration);

            TrainingEntity trainingEntity = modelMapper.map(trainingDto, TrainingEntity.class);
            trainingService.createTraining(trainingEntity);
            log.info("TrainingEntity '{}' created successfully.", trainingName);
            System.out.println("TrainingEntity created successfully.");
        } catch (Exception e) {
            log.error("Error occurred while creating training: ", e);
            System.out.println("An error occurred while creating the training. Please try again.");
        }
    }

    /**
     * Updates an existing training based on the training name.
     * <p>
     * Handles input validation and updates the training entity with new details.
     * </p>
     */
    public void updateTraining() {
        log.info("Starting to update training.");
        try {
            viewAllTrainings();
            System.out.print("Enter the name of the training to update: ");
            String trainingName = scanner.nextLine();
            TrainingEntity existingTrainingEntity = getTraining(trainingName);
            if (existingTrainingEntity
                    != null) {
                log.info("TrainingEntity '{}' found. Proceeding with update.", trainingName);
                TraineeDto traineeDto;
                TrainerDto trainerDto;

                traineeConsole.viewAllTrainee();
                System.out.print("Enter new trainee username: ");
                String traineeUsername = scanner.nextLine();
                traineeDto = modelMapper.map(traineeService.getTrainee(traineeUsername), TraineeDto.class);
                if (traineeDto
                        == null) {
                    System.out.println("Trainee not found.");
                    return;
                }

                trainerConsole.viewAllTrainer();
                System.out.print("Enter new trainer username: ");
                String trainerUsername = scanner.nextLine();
                trainerDto = modelMapper.map(trainerService.getTrainer(trainerUsername), TrainerDto.class);
                if (trainerDto
                        == null) {
                    System.out.println("Trainer not found.");
                    return;
                }

                System.out.print("Enter new training name: ");
                String newTrainingName = scanner.nextLine();
                System.out.print("Enter new training type: ");
                String trainingTypeName = scanner.nextLine();
                TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity(trainingTypeName);
                System.out.print("Enter new training date (YYYY-MM-DD): ");
                String trainingDateInput = scanner.nextLine();
                LocalDate trainingDate;
                try {
                    trainingDate = LocalDate.parse(trainingDateInput);
                } catch (DateTimeParseException e) {
                    log.warn("Invalid training date format: {}", trainingDateInput);
                    System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                    return;
                }
                System.out.print("Enter new training duration (HH:MM): ");
                String trainingDurationInput = scanner.nextLine();
                Duration trainingDuration;
                try {
                    String[] parts = trainingDurationInput.split(":");
                    if (parts.length
                            != 2) {
                        throw new NumberFormatException("Duration must be in HH:MM format.");
                    }
                    int hours = Integer.parseInt(parts[0]);
                    int minutes = Integer.parseInt(parts[1]);
                    trainingDuration = Duration.ofHours(hours).plusMinutes(minutes);
                } catch (NumberFormatException e) {
                    log.warn("Invalid training duration format: {}", trainingDurationInput);
                    System.out.println("Invalid duration format. Please use HH:MM.");
                    return;
                }

                TrainingDto updatedTrainingDto = new TrainingDto();
                updatedTrainingDto.setTraineeId(traineeUsername);
                updatedTrainingDto.setTrainerId(trainerUsername);
                updatedTrainingDto.setTrainingName(newTrainingName);
                updatedTrainingDto.setTrainingTypeEntity(trainingTypeEntity);
                updatedTrainingDto.setTrainingDate(trainingDate.toString());
                updatedTrainingDto.setTrainingDuration(trainingDuration);

                TrainingEntity updatedTrainingEntity = modelMapper.map(updatedTrainingDto, TrainingEntity.class);
                trainingService.updateTraining(trainingName, updatedTrainingEntity);
                log.info("TrainingEntity '{}' updated successfully.", trainingName);
                System.out.println("TrainingEntity updated successfully.");
            } else {
                log.warn("TrainingEntity '{}' not found.", trainingName);
                System.out.println("TrainingEntity not found.");
            }

        } catch (Exception e) {
            log.error("Error occurred while updating training: ", e);
            System.out.println("An error occurred while updating the training. Please try again.");
        }
    }

    /**
     * Deletes an existing training based on the training name.
     * <p>
     * This method will attempt to delete the specified training entity and provide feedback to the user.
     * </p>
     */
    public void deleteTraining() {
        log.info("Starting to delete training.");
        try {
            viewAllTrainings();
            System.out.print("Enter the name of the training to delete: ");
            String trainingName = scanner.nextLine();
            trainingService.deleteTraining(trainingName);
            log.info("TrainingEntity '{}' deleted successfully.", trainingName);
            System.out.println("TrainingEntity deleted successfully.");
        } catch (Exception e) {
            log.error("Error occurred while deleting training: ", e);
            System.out.println("An error occurred while deleting the training. Please try again.");
        }
    }

    /**
     * Displays details of a training based on the training name.
     * Handles possible errors during the retrieval process.
     */
    public void viewTraining() {
        log.info("Starting to view training details.");
        try {
            System.out.print("Enter training name to view: ");
            String trainingName = scanner.nextLine();

            TrainingEntity trainingEntity = getTraining(trainingName);
            if (trainingEntity
                    != null) {
                TrainingDto trainingDto = modelMapper.map(trainingEntity, TrainingDto.class);
                System.out.println(trainingDto);
            } else {
                System.out.println("Training not found.");
            }
        } catch (Exception e) {
            log.error("Error occurred while viewing training: ", e);
            System.out.println("An error occurred while viewing the training. Please try again.");
        }
    }

    /**
     * Displays all existing trainings.
     * <p>
     * This method fetches and displays the list of all training entities.
     * </p>
     */
    public void viewAllTrainings() {
        log.info("Fetching all trainings.");
        try {
            List<TrainingEntity> trainingEntities = trainingService.getAllTrainings();
            if (trainingEntities.isEmpty()) {
                System.out.println("No trainings found.");
            } else {
                trainingEntities.forEach(trainingEntity -> {
                    TrainingDto trainingDto = modelMapper.map(trainingEntity, TrainingDto.class);
                    System.out.println(trainingDto);
                });
            }
        } catch (Exception e) {
            log.error("Error occurred while viewing all trainings: ", e);
            System.out.println("An error occurred while viewing all trainings. Please try again.");
        }
    }

    /**
     * Fetches a training entity by its name.
     *
     * @param trainingName the name of the training to fetch
     * @return the TrainingEntity if found, or null if not found
     */
    private TrainingEntity getTraining(String trainingName) {
        try {
            TrainingEntity trainingEntity = trainingService.getTraining(trainingName);
            if (trainingEntity
                    != null) {
                log.info("TrainingEntity '{}' retrieved successfully.", trainingName);
            } else {
                log.warn("TrainingEntity '{}' not found.", trainingName);
            }
            return trainingEntity;
        } catch (Exception e) {
            log.error("Error occurred while retrieving trainingEntity '{}': ", trainingName, e);
            System.out.println("An error occurred while retrieving the training. Please try again.");
            return null;
        }
    }

    /**
     * Prints the menu for managing trainings.
     */
    public void printMenu() {
        log.info("Displaying menu options for managing trainings.");
        StringBuilder sb = new StringBuilder();
        sb.append("\nManage Trainings "
                +
                "\n1. Create TrainingEntity "
                +
                "\n2. Update TrainingEntity"
                +
                "\n3. Delete TrainingEntity "
                +
                "\n4. View TrainingEntity "
                +
                "\n5. View All Trainings "
                +
                "\n6. Back to Main Menu"
                +
                "\nEnter your choice: ");
        System.out.println(sb);
    }
}
