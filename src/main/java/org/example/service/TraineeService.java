package org.example.service;

import org.example.entity.TraineeEntity;
import org.example.entity.UserUtils;
import org.example.repository.TraineeDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

/**
 * Service class for managing trainees.
 */
@Service
public class TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);
    private final TraineeDAO traineeDao;
    Scanner scanner = new Scanner(System.in);


    @Autowired
    public TraineeService(TraineeDAO traineeDao) {
        this.traineeDao = traineeDao;
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
    public  void setScanner(Scanner scanner) {
        logger.info("Entering setScanner method.");
        if (scanner == null) {
            logger.error("Attempted to set a null scanner.");
            throw new IllegalArgumentException("Scanner cannot be null.");
        }
        this.scanner = scanner;
        logger.info("Scanner has been successfully set.");
    }

    /**
     * Updates an existing trainee based on the username.
     * Handles possible errors during the update process.
     */
    public void updateTrainee() {
        logger.info("Starting to update trainee.");
        try {
            System.out.print("Enter username of the traineeEntity to update: ");
            String username = scanner.nextLine();

            TraineeEntity traineeEntity = getTrainee(username);
            if (traineeEntity != null) {
                logger.info("TraineeEntity found with username: {}", username);
                System.out.println();
                System.out.print("Enter new date of birth (YYYY-MM-DD): ");
                String dateOfBirth = scanner.nextLine();
                System.out.print("Enter new address: ");
                String address = scanner.nextLine();

                traineeEntity.setLocalDateTime(dateOfBirth);
                traineeEntity.setAddress(address);

                traineeDao.updateTrainee(traineeEntity.getUserId(), traineeEntity);
                logger.info("TraineeEntity updated successfully.");
            } else {
                logger.warn("TraineeEntity not found with username: {}", username);
                System.out.println("TraineeEntity not found.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating trainee: ", e);
            System.out.println("An error occurred while updating the trainee. Please try again.");
        }
    }

    /**
     * Creates a new trainee by taking input from the console.
     * Handles possible errors during the creation process.
     */
    public void createTrainee() {
        logger.info("Starting to create a new trainee.");
        try {
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter date of birth (YYYY-MM-DD): ");
            String dateOfBirth = scanner.nextLine();
            System.out.print("Enter address: ");
            String address = scanner.nextLine();

            String username = UserUtils.generateUsername(firstName, lastName);
            String password = UserUtils.generatePassword();

            TraineeEntity traineeEntity = new TraineeEntity();
            traineeEntity.setUserId(username);
            traineeEntity.setLocalDateTime(dateOfBirth);
            traineeEntity.setAddress(address);

            traineeDao.createTrainee(traineeEntity);
            logger.info("TraineeEntity created with username: {} and password: {}", username, password);
        } catch (Exception e) {
            logger.error("Error occurred while creating trainee: ", e);
            System.out.println("An error occurred while creating the trainee. Please try again.");
        }
    }

    /**
     * Deletes an existing trainee based on the username.
     * Handles possible errors during the deletion process.
     */
    public void deleteTrainee() {
        logger.info("Starting to delete trainee.");
        try {
            System.out.print("Enter username of the traineeEntity to delete: ");
            String username = scanner.nextLine();
            TraineeEntity traineeEntity = getTrainee(username);
            if (traineeEntity != null) {
                traineeDao.deleteTrainee(traineeEntity.getUserId());
                logger.info("TraineeEntity with username: {} deleted successfully.", username);
                System.out.println("TraineeEntity deleted.");
            } else {
                logger.warn("TraineeEntity not found with username: {}", username);
                System.out.println("TraineeEntity not found.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting trainee: ", e);
            System.out.println("An error occurred while deleting the trainee. Please try again.");
        }
    }

    /**
     * Displays details of a trainee based on the username.
     * Handles possible errors during the retrieval process.
     */
    public void viewTrainee() {
        logger.info("Starting to view trainee details.");
        try {
            System.out.print("Enter username of the traineeEntity to view: ");
            String username = scanner.nextLine();

            TraineeEntity traineeEntity = getTrainee(username);
            if (traineeEntity != null) {
                logger.info("Displaying traineeEntity details for username: {}", username);
                System.out.println("TraineeEntity Details:");
                System.out.println("Username: " + traineeEntity.getUserId());
                System.out.println("Date of Birth: " + traineeEntity.getLocalDateTime().toString());
                System.out.println("Address: " + traineeEntity.getAddress());
            } else {
                logger.warn("TraineeEntity not found with username: {}", username);
                System.out.println("TraineeEntity not found.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while viewing trainee details: ", e);
            System.out.println("An error occurred while retrieving the trainee details. Please try again.");
        }
    }

    /**
     * Displays details of all trainees.
     * Handles possible errors during the retrieval process.
     */
    public void viewAllTrainee() {
        logger.info("Starting to view all trainees.");
        try {
            List<TraineeEntity> traineeEntities = getAllTrainees();
            if (traineeEntities.isEmpty()) {
                logger.info("No traineeEntities found.");
                System.out.println("No trainee found.");
            } else {
                logger.info("Displaying details of all traineeEntities.");
                System.out.println("All Trainees:");
                for (TraineeEntity traineeEntity : traineeEntities) {
                    System.out.println("TraineeEntity: " + traineeEntity.getUserId());
                    System.out.println("TraineeEntity: " + traineeEntity.getAddress());
                    System.out.println("TraineeEntity: " + traineeEntity.getLocalDateTime());
                    System.out.println("--------");
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred while viewing all trainees: ", e);
            System.out.println("An error occurred while retrieving the trainees. Please try again.");
        }
    }

    /**
     * Retrieves a trainee based on the username.
     * Handles possible errors during the retrieval process.
     *
     * @param userId the username of the trainee
     * @return the TraineeEntity object if found, null otherwise
     */
    public TraineeEntity getTrainee(String userId) {
        logger.info("Retrieving trainee with username: {}", userId);
        try {
            return traineeDao.getTrainee(userId);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving trainee with username: {}", userId, e);
            return null;
        }
    }

    /**
     * Retrieves all trainees.
     * Handles possible errors during the retrieval process.
     *
     * @return a list of all trainees
     */
    public List<TraineeEntity> getAllTrainees() {
        logger.info("Retrieving all trainees.");
        try {
            return traineeDao.getAllTrainees();
        } catch (Exception e) {
            logger.error("Error occurred while retrieving all trainees: ", e);
            return null;
        }
    }

    /**
     * Prints the menu for managing trainees.
     */
    public void printMenu() {
        logger.info("Displaying menu options for managing trainees.");
        StringBuilder sb = new StringBuilder();
        sb.append("\nManage TraineeEntity " +
                  "\n1. Create TraineeEntity " +
                  "\n2. Update TraineeEntity " +
                  "\n3. Delete TraineeEntity " +
                  "\n4. View TraineeEntity " +
                  "\n5. View All Trainees " +
                  "\n6. Back to Main Menu" +
                  "\n Enter your choice: ");
        System.out.println(sb);
    }
}
