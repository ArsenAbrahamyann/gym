package org.exemple.service;

import org.exemple.entity.Trainee;
import org.exemple.entity.UserUtils;
import org.exemple.repository.TraineeDAO;
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

            Trainee trainee = new Trainee();
            trainee.setUserId(username);
            trainee.setLocalDateTime(dateOfBirth);
            trainee.setAddress(address);

            traineeDao.createTrainee(trainee);
            logger.info("Trainee created with username: {} and password: {}", username, password);
        } catch (Exception e) {
            logger.error("Error occurred while creating trainee: ", e);
            System.out.println("An error occurred while creating the trainee. Please try again.");
        }
    }

    /**
     * Updates an existing trainee based on the username.
     * Handles possible errors during the update process.
     */
    public void updateTrainee() {
        logger.info("Starting to update trainee.");
        try {
            System.out.print("Enter username of the trainee to update: ");
            String username = scanner.nextLine();

            Trainee trainee = getTrainee(username);
            if (trainee != null) {
                logger.info("Trainee found with username: {}", username);
                System.out.print("Enter new date of birth (YYYY-MM-DD): ");
                String dateOfBirth = scanner.nextLine();
                System.out.print("Enter new address: ");
                String address = scanner.nextLine();

                trainee.setLocalDateTime(dateOfBirth);
                trainee.setAddress(address);

                traineeDao.updateTrainee(trainee.getUserId(), trainee);
                logger.info("Trainee updated successfully.");
            } else {
                logger.warn("Trainee not found with username: {}", username);
                System.out.println("Trainee not found.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating trainee: ", e);
            System.out.println("An error occurred while updating the trainee. Please try again.");
        }
    }

    /**
     * Deletes an existing trainee based on the username.
     * Handles possible errors during the deletion process.
     */
    public void deleteTrainee() {
        logger.info("Starting to delete trainee.");
        try {
            System.out.print("Enter username of the trainee to delete: ");
            String username = scanner.nextLine();
            Trainee trainee = getTrainee(username);
            if (trainee != null) {
                traineeDao.deleteTrainee(trainee.getUserId());
                logger.info("Trainee with username: {} deleted successfully.", username);
                System.out.println("Trainee deleted.");
            } else {
                logger.warn("Trainee not found with username: {}", username);
                System.out.println("Trainee not found.");
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
            System.out.print("Enter username of the trainee to view: ");
            String username = scanner.nextLine();

            Trainee trainee = getTrainee(username);
            if (trainee != null) {
                logger.info("Displaying trainee details for username: {}", username);
                System.out.println("Trainee Details:");
                System.out.println("Username: " + trainee.getUserId());
                System.out.println("Date of Birth: " + trainee.getLocalDateTime().toString());
                System.out.println("Address: " + trainee.getAddress());
            } else {
                logger.warn("Trainee not found with username: {}", username);
                System.out.println("Trainee not found.");
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
            List<Trainee> trainees = getAllTrainees();
            if (trainees.isEmpty()) {
                logger.info("No trainees found.");
                System.out.println("No trainee found.");
            } else {
                logger.info("Displaying details of all trainees.");
                System.out.println("All Trainees:");
                for (Trainee trainee : trainees) {
                    System.out.println("Trainee: " + trainee.getUserId());
                    System.out.println("Trainee: " + trainee.getAddress());
                    System.out.println("Trainee: " + trainee.getLocalDateTime());
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
     * @return the Trainee object if found, null otherwise
     */
    public Trainee getTrainee(String userId) {
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
    public List<Trainee> getAllTrainees() {
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
        sb.append("\nManage Trainee " +
                  "\n1. Create Trainee " +
                  "\n2. Update Trainee " +
                  "\n3. Delete Trainee " +
                  "\n4. View Trainee " +
                  "\n5. View All Trainees " +
                  "\n6. Back to Main Menu" +
                  "\n Enter your choice: ");
        System.out.println(sb);
    }
}
