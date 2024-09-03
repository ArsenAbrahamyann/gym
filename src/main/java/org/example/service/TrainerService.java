package org.example.service;

import org.example.entity.TrainerEntity;
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

            TrainerEntity trainerEntity = new TrainerEntity();
            trainerEntity.setSpecialization(specialization);
            trainerEntity.setUserId(username);

            trainerDao.createTrainer(trainerEntity);
            logger.info("TrainerEntity created with username: {}", username);
            System.out.println("TrainerEntity created with username: " + username);

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
            System.out.print("Enter username of the trainerEntity to update: ");
            String username = scanner.nextLine();

            TrainerEntity trainerEntity = getTrainer(username);
            if (trainerEntity != null) {
                logger.info("TrainerEntity found with username: {}", username);
                System.out.println();
                System.out.print("Enter new specialization: ");
                String specialization = scanner.nextLine();

                trainerEntity.setSpecialization(specialization);

                trainerDao.updateTrainer(trainerEntity.getUserId(), trainerEntity);
                logger.info("TrainerEntity updated successfully.");
                System.out.println("TrainerEntity updated.");
            } else {
                logger.warn("TrainerEntity not found with username: {}", username);
                System.out.println("TrainerEntity not found.");
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
            System.out.print("Enter username of the trainerEntity to delete: ");
            String username = scanner.nextLine();

            TrainerEntity trainerEntity = getTrainer(username);
            if (trainerEntity != null) {
                trainerDao.deleteTrainer(trainerEntity.getUserId());
                logger.info("TrainerEntity with username: {} deleted successfully.", username);
                System.out.println("TrainerEntity deleted.");
            } else {
                logger.warn("TrainerEntity not found with username: {}", username);
                System.out.println("TrainerEntity not found.");
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
            System.out.print("Enter username of the trainerEntity to view: ");
            String username = scanner.nextLine();

            TrainerEntity trainerEntity = getTrainer(username);
            if (trainerEntity != null) {
                logger.info("Displaying trainerEntity details for username: {}", username);
                System.out.println("TrainerEntity Details:");
                System.out.println("Username: " + trainerEntity.getUserId());
                System.out.println("Specialization: " + trainerEntity.getSpecialization());
            } else {
                logger.warn("TrainerEntity not found with username: {}", username);
                System.out.println("TrainerEntity not found.");
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
            List<TrainerEntity> trainerEntities = getAllTrainers();
            if (trainerEntities.isEmpty()) {
                logger.info("No trainerEntities found.");
                System.out.println("No trainer found.");
            } else {
                logger.info("Displaying details of all trainerEntities.");
                System.out.println("All Trainers:");
                for (TrainerEntity trainerEntity : trainerEntities) {
                    System.out.println("Username: " + trainerEntity.getUserId());
                    System.out.println("Specialization: " + trainerEntity.getSpecialization());
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
     * @return the TrainerEntity object if found, null otherwise
     */
    public TrainerEntity getTrainer(String userId) {
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
    public List<TrainerEntity> getAllTrainers() {
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
