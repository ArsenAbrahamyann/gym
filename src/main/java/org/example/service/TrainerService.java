package org.example.service;

import org.example.entity.Trainer;
import org.example.entity.UserUtils;
import org.example.repository.TrainerDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

/**
 * Service class for managing trainers.
 */
@Service
public class TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);
    private final TrainerDAO trainerDao;
    Scanner scanner = new Scanner(System.in);

    @Autowired
    public TrainerService(TrainerDAO trainerDao) {
        this.trainerDao = trainerDao;
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
     * Creates a new trainer by taking input from the console.
     * Handles possible errors during the creation process.
     */
    public void createTrainer() {
        logger.info("Starting to create a new trainer.");
        try {
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter specialization: ");
            String specialization = scanner.nextLine();

            String username = UserUtils.generateUsername(firstName, lastName);

            Trainer trainer = new Trainer();
            trainer.setSpecialization(specialization);
            trainer.setUserId(username);

            trainerDao.createTrainer(trainer);
            logger.info("Trainer created with username: {}", username);
            System.out.println("Trainer created with username: " + username);

        } catch (Exception e) {
            logger.error("Error occurred while creating trainer: ", e);
            System.out.println("An error occurred while creating the trainer. Please try again.");
        }
    }

    /**
     * Updates an existing trainer based on the username.
     * Handles possible errors during the update process.
     */
    public void updateTrainer() {
        logger.info("Starting to update trainer.");
        try {
            System.out.print("Enter username of the trainer to update: ");
            String username = scanner.nextLine();

            Trainer trainer = getTrainer(username);
            if (trainer != null) {
                logger.info("Trainer found with username: {}", username);
                System.out.println();
                System.out.print("Enter new specialization: ");
                String specialization = scanner.nextLine();

                trainer.setSpecialization(specialization);

                trainerDao.updateTrainer(trainer.getUserId(), trainer);
                logger.info("Trainer updated successfully.");
                System.out.println("Trainer updated.");
            } else {
                logger.warn("Trainer not found with username: {}", username);
                System.out.println("Trainer not found.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating trainer: ", e);
            System.out.println("An error occurred while updating the trainer. Please try again.");
        }
    }

    /**
     * Deletes an existing trainer based on the username.
     * Handles possible errors during the deletion process.
     */
    public void deleteTrainer() {
        logger.info("Starting to delete trainer.");
        try {
            System.out.print("Enter username of the trainer to delete: ");
            String username = scanner.nextLine();

            Trainer trainer = getTrainer(username);
            if (trainer != null) {
                trainerDao.deleteTrainer(trainer.getUserId());
                logger.info("Trainer with username: {} deleted successfully.", username);
                System.out.println("Trainer deleted.");
            } else {
                logger.warn("Trainer not found with username: {}", username);
                System.out.println("Trainer not found.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting trainer: ", e);
            System.out.println("An error occurred while deleting the trainer. Please try again.");
        }
    }

    /**
     * Displays details of a trainer based on the username.
     * Handles possible errors during the retrieval process.
     */
    public void viewTrainer() {
        logger.info("Starting to view trainer details.");
        try {
            System.out.print("Enter username of the trainer to view: ");
            String username = scanner.nextLine();

            Trainer trainer = getTrainer(username);
            if (trainer != null) {
                logger.info("Displaying trainer details for username: {}", username);
                System.out.println("Trainer Details:");
                System.out.println("Username: " + trainer.getUserId());
                System.out.println("Specialization: " + trainer.getSpecialization());
            } else {
                logger.warn("Trainer not found with username: {}", username);
                System.out.println("Trainer not found.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while viewing trainer details: ", e);
            System.out.println("An error occurred while retrieving the trainer details. Please try again.");
        }
    }

    /**
     * Displays details of all trainers.
     * Handles possible errors during the retrieval process.
     */
    public void viewAllTrainer() {
        logger.info("Starting to view all trainers.");
        try {
            List<Trainer> trainers = getAllTrainers();
            if (trainers.isEmpty()) {
                logger.info("No trainers found.");
                System.out.println("No trainer found.");
            } else {
                logger.info("Displaying details of all trainers.");
                System.out.println("All Trainers:");
                for (Trainer trainer : trainers) {
                    System.out.println("Username: " + trainer.getUserId());
                    System.out.println("Specialization: " + trainer.getSpecialization());
                    System.out.println("--------");
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred while viewing all trainers: ", e);
            System.out.println("An error occurred while retrieving the trainers. Please try again.");
        }
    }

    /**
     * Retrieves a trainer based on the username.
     * Handles possible errors during the retrieval process.
     *
     * @param userId the username of the trainer
     * @return the Trainer object if found, null otherwise
     */
    public Trainer getTrainer(String userId) {
        logger.info("Retrieving trainer with username: {}", userId);
        try {
            return trainerDao.getTrainer(userId);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving trainer with username: {}", userId, e);
            return null;
        }
    }

    /**
     * Retrieves all trainers.
     * Handles possible errors during the retrieval process.
     *
     * @return a list of all trainers
     */
    public List<Trainer> getAllTrainers() {
        logger.info("Retrieving all trainers.");
        try {
            return trainerDao.getAllTrainers();
        } catch (Exception e) {
            logger.error("Error occurred while retrieving all trainers: ", e);
            return null;
        }
    }

    /**
     * Prints the menu for managing trainers.
     */
    public void printMenu() {
        logger.info("Displaying menu options for managing trainers.");
        StringBuilder sb = new StringBuilder();
        sb.append("\nManage Trainer " +
                  "\n1. Create Trainer " +
                  "\n2. Update Trainer " +
                  "\n3. Delete Trainer " +
                  "\n4. View Trainer " +
                  "\n5. View All Trainers " +
                  "\n6. Back to Main Menu" +
                  "\nEnter your choice: ");
        System.out.println(sb);
    }
}
