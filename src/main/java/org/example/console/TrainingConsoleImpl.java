package org.example.console;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.entity.UserEntity;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import org.example.service.UserService;

@Slf4j
public class TrainingConsoleImpl {
    private final TrainingService trainingService;
    private final TraineeConsoleImpl traineeConsole;
    private final TrainerConsoleImpl trainerConsole;
    private final UserConsoleImpl userConsole;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private Scanner scanner = new Scanner(System.in);

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
     * Creates a new training by taking input from the console.
     * Handles possible errors during the creation process.
     */
    public void createTraining() {
        log.info("Starting to create a new training.");
        try {
            TrainingEntity trainingEntity = new TrainingEntity();
            traineeConsole.viewAllTrainee();
            System.out.print("Enter trainee username: ");
            String traineeUsername = scanner.nextLine();
            TraineeEntity trainee = traineeService.getTrainee(traineeUsername);
            if (trainee == null) {
                System.out.println("Trainee not found.");
                return;
            }
            trainingEntity.setTraineeId(traineeUsername);
            trainerConsole.viewAllTrainer();
            System.out.println("Enter trainer username");
            String trainerUsername = scanner.nextLine();
            TrainerEntity trainer = trainerService.getTrainer(trainerUsername);
            if (trainer == null) {
                System.out.println("Trainer not found.");
                return;
            }
            trainingEntity.setTrainerId(trainerUsername);
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
                if (parts.length != 2) {
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
            trainingEntity.setTrainingName(trainingName);
            trainingEntity.setTrainingTypeEntity(trainingTypeEntity);
            trainingEntity.setTrainingDate(trainingDate.toString());
            trainingEntity.setTrainingDuration(trainingDuration);

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
     * Handles possible errors during the update process.
     */
    public void updateTraining() {
        log.info("Starting to update training.");
        try {
            viewAllTrainings();
            System.out.print("Enter the name of the training to update: ");
            String trainingName = scanner.nextLine();
            TrainingEntity existingTrainingEntity = getTraining(trainingName);
            if (existingTrainingEntity != null) {
                log.info("TrainingEntity '{}' found. Proceeding with update.", trainingName);
                traineeConsole.viewAllTrainee();
                System.out.print("Enter new trainee username: ");
                String traineeUsername = scanner.nextLine();
                TraineeEntity trainee = traineeService.getTrainee(traineeUsername);
                if (trainee == null) {
                    System.out.println("Trainee not found.");
                    return;
                }
                trainerConsole.viewAllTrainer();
                System.out.print("Enter new trainer username: ");
                String trainerUsername = scanner.nextLine();
                TrainerEntity trainer = trainerService.getTrainer(trainerUsername);
                if (trainer == null) {
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
                    if (parts.length != 2) {
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

                existingTrainingEntity.setTraineeId(traineeUsername);
                existingTrainingEntity.setTrainerId(trainerUsername);
                existingTrainingEntity.setTrainingName(newTrainingName);
                existingTrainingEntity.setTrainingTypeEntity(trainingTypeEntity);
                existingTrainingEntity.setTrainingDate(trainingDate.toString());
                existingTrainingEntity.setTrainingDuration(trainingDuration);

                trainingService.updateTraining(trainingName, existingTrainingEntity);
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
     * Handles possible errors during the deletion process.
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
            if (trainingEntity != null) {
                log.info("Displaying details for trainingEntity '{}'.", trainingName);
                System.out.println("TrainingEntity Details:");
                System.out.println("Trainee Username: " + trainingEntity.getTraineeId());
                System.out.println("Trainer Username: " + trainingEntity.getTrainerId());
                System.out.println("Training Name: " + trainingEntity.getTrainingName());
                System.out.println("Training Type: " + trainingEntity.getTrainingTypeEntity().getTrainingTypeName());
                System.out.println("Training Date: " + trainingEntity.getTrainingDate());
                System.out.println("Training Duration: " + trainingEntity.getTrainingDuration().toHours() + " hours " +
                                   trainingEntity.getTrainingDuration().toMinutesPart() + " minutes");
            } else {
                log.warn("TrainingEntity '{}' not found.", trainingName);
                System.out.println("TrainingEntity not found.");
            }
        } catch (Exception e) {
            log.error("Error occurred while viewing training details: ", e);
            System.out.println("An error occurred while retrieving the training details. Please try again.");
        }
    }

    /**
     * Displays details of all trainings.
     * Handles possible errors during the retrieval process.
     */
    public void viewAllTrainings() {
        log.info("Starting to view all trainings.");
        try {
            List<TrainingEntity> trainingEntities = getAllTrainings();
            if (trainingEntities == null || trainingEntities.isEmpty()) {
                log.info("No trainingEntities found.");
                System.out.println("No trainingEntities found.");
            } else {
                log.info("Displaying details of all trainingEntities.");
                System.out.println("All Trainings:");
                for (TrainingEntity trainingEntity : trainingEntities) {
                    System.out.println("TraineeEntity Username: " + trainingEntity.getTraineeId());
                    System.out.println("TrainerEntity Username: " + trainingEntity.getTrainerId());
                    System.out.println("TrainingEntity Name: " + trainingEntity.getTrainingName());

                    if (trainingEntity.getTrainingTypeEntity() != null) {
                        System.out.println(
                            "TrainingEntity Type: " + trainingEntity.getTrainingTypeEntity().getTrainingTypeName());
                    } else {
                        System.out.println("TrainingEntity Type: Not specified");
                    }

                    System.out.println("TrainingEntity Date: " + trainingEntity.getTrainingDate());
                    System.out.println(
                        "TrainingEntity Duration: " + formatDuration(trainingEntity.getTrainingDuration()));
                    System.out.println("--------");
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while viewing all trainings: ", e);
            System.out.println("An error occurred while retrieving the trainings. Please try again.");
        }
    }

    /**
     * Retrieves a training entity based on the training name.
     * Handles possible errors during the retrieval process.
     *
     * @param trainingName the name of the training to retrieve
     * @return the training entity if found, null otherwise
     */
    private TrainingEntity getTraining(String trainingName) {
        try {
            TrainingEntity trainingEntity = trainingService.getTraining(trainingName);
            if (trainingEntity != null) {
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
     * Retrieves all trainings.
     * Handles possible errors during the retrieval process.
     *
     * @return a list of all trainings
     */
    public List<TrainingEntity> getAllTrainings() {
        log.info("Retrieving all trainings.");
        try {
            return trainingService.getAllTrainings();
        } catch (Exception e) {
            log.error("Error occurred while retrieving all trainings: ", e);
            return null;
        }
    }

    /**
     * Formats the Duration object into HH:MM format for display.
     *
     * @param duration the Duration object
     * @return formatted duration string
     */
    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return String.format("%02d:%02d", hours, minutes);
    }

    /**
     * Prints the menu for managing trainings.
     */
    public void printMenu() {
        log.info("Displaying menu options for managing trainings.");
        StringBuilder sb = new StringBuilder();
        sb.append("\nManage Trainings " +
                  "\n1. Create TrainingEntity " +
                  "\n2. Update TrainingEntity" +
                  "\n3. Delete TrainingEntity " +
                  "\n4. View TrainingEntity " +
                  "\n5. View All Trainings " +
                  "\n6. Back to Main Menu" +
                  "\nEnter your choice: ");
        System.out.println(sb);
    }
}
