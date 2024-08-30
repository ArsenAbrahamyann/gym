package org.exemple;

import org.exemple.service.TraineeService;
import org.exemple.service.TrainerService;
import org.exemple.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ConsoleApp {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleApp.class);
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final Scanner scanner;

    public ConsoleApp(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.scanner = new Scanner(System.in);
    }


    public void run() {
        while (true) {
            printMenu();
            try {
                Integer choice = Integer.parseInt(scanner.nextLine().trim());
                logger.info("User selected option: {}", choice);
                switch (choice) {
                    case 1:
                        manageTrainees();
                        break;
                    case 2:
                        manageTrainers();
                        break;
                    case 3:
                        manageTrainings();
                        break;
                    case 4:
                        logger.info("Exiting the application.");
                        System.out.println("Exiting...");
                        return;
                    default:
                        logger.warn("Invalid choice selected: {}", choice);
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid input, expected a number.");
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                logger.error("An unexpected error occurred.", e);
                System.out.println("An unexpected error occurred. Please try again.");
            }
        }
    }

    private void manageTrainees() {
        while (true) {
            traineeService.printMenu();
            try {
                Integer choice = Integer.parseInt(scanner.nextLine().trim());
                logger.info("User selected trainee option: {}", choice);
                switch (choice) {
                    case 1:
                        traineeService.createTrainee();
                        break;
                    case 2:
                        traineeService.updateTrainee();
                        break;
                    case 3:
                        traineeService.deleteTrainee();
                        break;
                    case 4:
                        traineeService.viewTrainee();
                        break;
                    case 5:
                        traineeService.viewAllTrainee();
                        break;
                    case 6:
                        return;
                    default:
                        logger.warn("Invalid trainee choice selected: {}", choice);
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid input for trainee option, expected a number.", e);
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                logger.error("An unexpected error occurred while managing trainees.", e);
                System.out.println("An unexpected error occurred. Please try again.");
            }
        }
    }

    private void manageTrainers() {
        while (true) {
            trainerService.printMenu();
            try {
                Integer choice = Integer.parseInt(scanner.nextLine().trim());
                logger.info("User selected trainer option: {}", choice);
                switch (choice) {
                    case 1:
                        trainerService.createTrainer();
                        break;
                    case 2:
                        trainerService.updateTrainer();
                        break;
                    case 3:
                        trainerService.deleteTrainer();
                        break;
                    case 4:
                        trainerService.viewTrainer();
                        break;
                    case 5:
                        trainerService.viewAllTrainer();
                        break;
                    case 6:
                        return;
                    default:
                        logger.warn("Invalid trainer choice selected: {}", choice);
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid input for trainer option, expected a number.", e);
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                logger.error("An unexpected error occurred while managing trainers.", e);
                System.out.println("An unexpected error occurred. Please try again.");
            }
        }
    }

    private void manageTrainings() {
        while (true) {
            trainingService.printMenu();
            try {
                Integer choice = Integer.parseInt(scanner.nextLine().trim());
                logger.info("User selected training option: {}", choice);
                switch (choice) {
                    case 1:
                        trainingService.createTraining();
                        break;
                    case 2:
                        trainingService.viewTraining();
                        break;
                    case 3:
                        trainingService.viewAllTrainings();
                        break;
                    case 4:
                        trainingService.updateTraining();
                        break;
                    case 5:
                        trainingService.deleteTraining();
                        break;
                    case 6:
                        return;
                    default:
                        logger.warn("Invalid training choice selected: {}", choice);
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid input for training option, expected a number.", e);
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                logger.error("An unexpected error occurred while managing trainings.", e);
                System.out.println("An unexpected error occurred. Please try again.");
            }
        }
    }

    public void printMenu() {
        logger.info("Displaying main menu.");
        StringBuilder sb = new StringBuilder();
        sb.append("\nWelcome to the Gym CRM System " +
                  "\n1. Manage Trainees " +
                  "\n2. Manage Trainers " +
                  "\n3. Manage Trainings " +
                  "\n4. Exit " +
                  "\n5. Choose an option: ");
        System.out.println(sb);
    }


}


