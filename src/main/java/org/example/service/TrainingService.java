package org.example.service;

import org.example.entity.Training;
import org.example.entity.TrainingType;
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
public class TrainingService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);
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
        logger.info("Entering setScanner method.");
        if (scanner == null) {
            logger.error("Attempted to set a null scanner.");
            throw new IllegalArgumentException("Scanner cannot be null.");
        }
        this.scanner = scanner;
        logger.info("Scanner has been successfully set.");
    }

    /**
     * Creates a new training by taking input from the console.
     * Handles possible errors during the creation process.
     */
    public void createTraining() {
        logger.info("Starting to create a new training.");
        try {
            System.out.print("Enter trainee username: ");
            String traineeId = scanner.nextLine();

            System.out.print("Enter trainer username: ");
            String trainerId = scanner.nextLine();

            System.out.print("Enter training name: ");
            String trainingName = scanner.nextLine();

            System.out.print("Enter training type: ");
            String trainingTypeName = scanner.nextLine();
            TrainingType trainingType = new TrainingType(trainingTypeName);

            System.out.print("Enter training date (YYYY-MM-DD): ");
            String trainingDateInput = scanner.nextLine();
            LocalDate trainingDate;
            try {
                trainingDate = LocalDate.parse(trainingDateInput);
            } catch (DateTimeParseException e) {
                logger.warn("Invalid training date format: {}", trainingDateInput);
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
                logger.warn("Invalid training duration format: {}", trainingDurationInput);
                System.out.println("Invalid duration format. Please use HH:MM.");
                return;
            }

            Training training = new Training();
            training.setTraineeId(traineeId);
            training.setTrainerId(trainerId);
            training.setTrainingName(trainingName);
            training.setTrainingType(trainingType);
            training.setTrainingDate(trainingDate.toString());
            training.setTrainingDuration(trainingDuration);

            trainingDao.createTraining(training);
            logger.info("Training '{}' created successfully.", trainingName);
            System.out.println("Training created successfully.");

        } catch (Exception e) {
            logger.error("Error occurred while creating training: ", e);
            System.out.println("An error occurred while creating the training. Please try again.");
        }
    }

    /**
     * Updates an existing training based on the training name.
     * Handles possible errors during the update process.
     */
    public void updateTraining() {
        logger.info("Starting to update training.");
        try {
            System.out.print("Enter the name of the training to update: ");
            String trainingName = scanner.nextLine();

            Training existingTraining = getTraining(trainingName);
            if (existingTraining != null) {
                logger.info("Training '{}' found. Proceeding with update.", trainingName);

                System.out.print("Enter new trainee username: ");
                String traineeId = scanner.nextLine();

                System.out.print("Enter new trainer username: ");
                String trainerId = scanner.nextLine();

                System.out.print("Enter new training name: ");
                String newTrainingName = scanner.nextLine();

                System.out.print("Enter new training type: ");
                String trainingTypeName = scanner.nextLine();
                TrainingType trainingType = new TrainingType(trainingTypeName);

                System.out.print("Enter new training date (YYYY-MM-DD): ");
                String trainingDateInput = scanner.nextLine();
                LocalDate trainingDate;
                try {
                    trainingDate = LocalDate.parse(trainingDateInput);
                } catch (DateTimeParseException e) {
                    logger.warn("Invalid training date format: {}", trainingDateInput);
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
                    logger.warn("Invalid training duration format: {}", trainingDurationInput);
                    System.out.println("Invalid duration format. Please use HH:MM.");
                    return;
                }

                existingTraining.setTraineeId(traineeId);
                existingTraining.setTrainerId(trainerId);
                existingTraining.setTrainingName(newTrainingName);
                existingTraining.setTrainingType(trainingType);
                existingTraining.setTrainingDate(trainingDate.toString());
                existingTraining.setTrainingDuration(trainingDuration);

                trainingDao.updateTraining(trainingName, existingTraining);
                logger.info("Training '{}' updated successfully.", trainingName);
                System.out.println("Training updated successfully.");
            } else {
                logger.warn("Training '{}' not found.", trainingName);
                System.out.println("Training not found.");
            }

        } catch (Exception e) {
            logger.error("Error occurred while updating training: ", e);
            System.out.println("An error occurred while updating the training. Please try again.");
        }
    }

    /**
     * Deletes an existing training based on the training name.
     * Handles possible errors during the deletion process.
     */
    public void deleteTraining() {
        logger.info("Starting to delete training.");
        try {
            System.out.print("Enter the name of the training to delete: ");
            String trainingName = scanner.nextLine();

            Training existingTraining = getTraining(trainingName);
            if (existingTraining != null) {
                trainingDao.deleteTraining(trainingName);
                logger.info("Training '{}' deleted successfully.", trainingName);
                System.out.println("Training deleted successfully.");
            } else {
                logger.warn("Training '{}' not found.", trainingName);
                System.out.println("Training not found.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting training: ", e);
            System.out.println("An error occurred while deleting the training. Please try again.");
        }
    }

    /**
     * Displays details of a training based on the training name.
     * Handles possible errors during the retrieval process.
     */
    public void viewTraining() {
        logger.info("Starting to view training details.");
        try {
            System.out.print("Enter training name to view: ");
            String trainingName = scanner.nextLine();

            Training training = getTraining(trainingName);
            if (training != null) {
                logger.info("Displaying details for training '{}'.", trainingName);
                System.out.println("Training Details:");
                System.out.println("Trainee Username: " + training.getTraineeId());
                System.out.println("Trainer Username: " + training.getTrainerId());
                System.out.println("Training Name: " + training.getTrainingName());
                System.out.println("Training Type: " + training.getTrainingType().getTrainingTypeName());
                System.out.println("Training Date: " + training.getTrainingDate());
                System.out.println("Training Duration: " + formatDuration(training.getTrainingDuration()));
            } else {
                logger.warn("Training '{}' not found.", trainingName);
                System.out.println("Training not found.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while viewing training details: ", e);
            System.out.println("An error occurred while retrieving the training details. Please try again.");
        }
    }

    /**
     * Displays details of all trainings.
     * Handles possible errors during the retrieval process.
     */
    public void viewAllTrainings() {
        logger.info("Starting to view all trainings.");
        try {
            List<Training> trainings = getAllTrainings();
            if (trainings == null || trainings.isEmpty()) {
                logger.info("No trainings found.");
                System.out.println("No trainings found.");
            } else {
                logger.info("Displaying details of all trainings.");
                System.out.println("All Trainings:");
                for (Training training : trainings) {
                    System.out.println("Trainee Username: " + training.getTraineeId());
                    System.out.println("Trainer Username: " + training.getTrainerId());
                    System.out.println("Training Name: " + training.getTrainingName());
                    System.out.println("Training Type: " + training.getTrainingType().getTrainingTypeName());
                    System.out.println("Training Date: " + training.getTrainingDate());
                    System.out.println("Training Duration: " + formatDuration(training.getTrainingDuration()));
                    System.out.println("--------");
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred while viewing all trainings: ", e);
            System.out.println("An error occurred while retrieving the trainings. Please try again.");
        }
    }

    /**
     * Prints the menu for managing trainings.
     */
    public void printMenu() {
        logger.info("Displaying menu options for managing trainings.");
        StringBuilder sb = new StringBuilder();
        sb.append("\nManage Trainings " +
                  "\n1. Create Training " +
                  "\n2. View Training " +
                  "\n3. View All Trainings " +
                  "\n4. Update Training " +
                  "\n5. Delete Training " +
                  "\n6. Back to Main Menu" +
                  "\nEnter your choice: ");
        System.out.println(sb);
    }

    /**
     * Retrieves a training based on the training name.
     * Handles possible errors during the retrieval process.
     *
     * @param trainingName the name of the training
     * @return the Training object if found, null otherwise
     */
    public Training getTraining(String trainingName) {
        logger.info("Retrieving training with name: {}", trainingName);
        try {
            return trainingDao.getTraining(trainingName);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving training '{}': ", trainingName, e);
            return null;
        }
    }

    /**
     * Retrieves all trainings.
     * Handles possible errors during the retrieval process.
     *
     * @return a list of all trainings
     */
    public List<Training> getAllTrainings() {
        logger.info("Retrieving all trainings.");
        try {
            return trainingDao.getAllTrainings();
        } catch (Exception e) {
            logger.error("Error occurred while retrieving all trainings: ", e);
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
