package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.repository.TrainingDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Service class for managing trainings.
 */
@Service
@Slf4j
public class TrainingService {
    private final TrainingDAO trainingDao;
    Scanner scanner = new Scanner(System.in);

    @Autowired
    public TrainingService(TrainingDAO trainingDao) {
        this.trainingDao = trainingDao;
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
     * Creates a new training by taking input from the console.
     * Handles possible errors during the creation process.
     */
    public void createTraining() {
        log.info("Starting to create a new training.");
        try {
            System.out.print("Enter trainee username: ");
            String traineeId = scanner.nextLine();

            System.out.print("Enter trainer username: ");
            String trainerId = scanner.nextLine();

            System.out.print("Enter trainingEntity name: ");
            String trainingName = scanner.nextLine();

            System.out.print("Enter trainingEntity type: ");
            String trainingTypeName = scanner.nextLine();
            TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity(trainingTypeName);

            System.out.print("Enter trainingEntity date (YYYY-MM-DD): ");
            String trainingDateInput = scanner.nextLine();
            LocalDate trainingDate;
            try {
                trainingDate = LocalDate.parse(trainingDateInput);
            } catch (DateTimeParseException e) {
                log.warn("Invalid trainingEntity date format: {}", trainingDateInput);
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                return;
            }

            System.out.print("Enter trainingEntity duration (HH:MM): ");
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
                log.warn("Invalid trainingEntity duration format: {}", trainingDurationInput);
                System.out.println("Invalid duration format. Please use HH:MM.");
                return;
            }

            TrainingEntity trainingEntity = new TrainingEntity();
            trainingEntity.setTraineeId(traineeId);
            trainingEntity.setTrainerId(trainerId);
            trainingEntity.setTrainingName(trainingName);
            trainingEntity.setTrainingTypeEntity(trainingTypeEntity);
            trainingEntity.setTrainingDate(trainingDate.toString());
            trainingEntity.setTrainingDuration(trainingDuration);

            trainingDao.createTraining(trainingEntity);
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
            System.out.print("Enter the name of the training to update: ");
            String trainingName = scanner.nextLine();

            TrainingEntity existingTrainingEntity = getTraining(trainingName);
            if (existingTrainingEntity != null) {
                log.info("TrainingEntity '{}' found. Proceeding with update.", trainingName);

                System.out.print("Enter new trainee username: ");
                String traineeId = scanner.nextLine();

                System.out.print("Enter new trainer username: ");
                String trainerId = scanner.nextLine();

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

                existingTrainingEntity.setTraineeId(traineeId);
                existingTrainingEntity.setTrainerId(trainerId);
                existingTrainingEntity.setTrainingName(newTrainingName);
                existingTrainingEntity.setTrainingTypeEntity(trainingTypeEntity);
                existingTrainingEntity.setTrainingDate(trainingDate.toString());
                existingTrainingEntity.setTrainingDuration(trainingDuration);

                trainingDao.updateTraining(trainingName, existingTrainingEntity);
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
            System.out.print("Enter the name of the training to delete: ");
            String trainingName = scanner.nextLine();

            TrainingEntity existingTrainingEntity = getTraining(trainingName);
            if (existingTrainingEntity != null) {
                trainingDao.deleteTraining(trainingName);
                log.info("TrainingEntity '{}' deleted successfully.", trainingName);
                System.out.println("TrainingEntity deleted successfully.");
            } else {
                log.warn("TrainingEntity '{}' not found.", trainingName);
                System.out.println("TrainingEntity not found.");
            }
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
            System.out.print("Enter trainingEntity name to view: ");
            String trainingName = scanner.nextLine();

            TrainingEntity trainingEntity = getTraining(trainingName);
            if (trainingEntity != null) {
                log.info("Displaying details for trainingEntity '{}'.", trainingName);
                System.out.println("TrainingEntity Details:");
                System.out.println("TraineeEntity Username: " + trainingEntity.getTraineeId());
                System.out.println("TrainerEntity Username: " + trainingEntity.getTrainerId());
                System.out.println("TrainingEntity Name: " + trainingEntity.getTrainingName());
                System.out.println("TrainingEntity Type: " + trainingEntity.getTrainingTypeEntity().getTrainingTypeName());
                System.out.println("TrainingEntity Date: " + trainingEntity.getTrainingDate());
                System.out.println("TrainingEntity Duration: " + formatDuration(trainingEntity.getTrainingDuration()));
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
                        System.out.println("TrainingEntity Type: " + trainingEntity.getTrainingTypeEntity().getTrainingTypeName());
                    } else {
                        System.out.println("TrainingEntity Type: Not specified");
                    }

                    System.out.println("TrainingEntity Date: " + trainingEntity.getTrainingDate());
                    System.out.println("TrainingEntity Duration: " + formatDuration(trainingEntity.getTrainingDuration()));
                    System.out.println("--------");
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while viewing all trainings: ", e);
            System.out.println("An error occurred while retrieving the trainings. Please try again.");
        }
    }

    /**
     * Prints the menu for managing trainings.
     */
    public void printMenu() {
        log.info("Displaying menu options for managing trainings.");
        StringBuilder sb = new StringBuilder();
        sb.append("\nManage Trainings " +
                  "\n1. Create TrainingEntity " +
                  "\n2. View TrainingEntity " +
                  "\n3. View All Trainings " +
                  "\n4. Update TrainingEntity " +
                  "\n5. Delete TrainingEntity " +
                  "\n6. Back to Main Menu" +
                  "\nEnter your choice: ");
        System.out.println(sb);
    }

    /**
     * Retrieves a training based on the training name.
     * Handles possible errors during the retrieval process.
     *
     * @param trainingName the name of the training
     * @return the TrainingEntity object if found, null otherwise
     */
    public TrainingEntity getTraining(String trainingName) {
        log.info("Retrieving training with name: {}", trainingName);
        try {
            return trainingDao.getTraining(trainingName);
        } catch (Exception e) {
            log.error("Error occurred while retrieving training '{}': ", trainingName, e);
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
            return trainingDao.getAllTrainings();
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
}
