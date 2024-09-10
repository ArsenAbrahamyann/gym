package org.example.console;

import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The {@code ConsoleApp} class represents the main application console for managing
 * trainees, trainers, and trainings in the Gym CRM System. It provides a menu-driven
 * interface to the user and handles various operations like creation, update, deletion,
 * and viewing of entities.
 *
 * <p>This class uses {@link TraineeConsoleImpl}, {@link TrainerConsoleImpl}, and
 * {@link TrainingConsoleImpl} to delegate the specific operations to their respective
 * modules.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ConsoleApp {
    private final TraineeConsoleImpl traineeConsole;
    private final TrainerConsoleImpl trainerConsole;
    private final TrainingConsoleImpl trainingConsole;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Runs the main loop of the console application. Displays the main menu
     * and handles user input for managing trainees, trainers, and trainings.
     */
    public void run() {
        while (true) {
            printMenu();
            try {
                Integer choice = Integer.parseInt(scanner.nextLine().trim());
                log.info("UserEntity selected option: {}", choice);
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
                        log.info("Exiting the application.");
                        System.out.println("Exiting...");
                        return;
                    default:
                        log.warn("Invalid choice selected: {}", choice);
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                log.error("Invalid input, expected a number.");
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                log.error("An unexpected error occurred.", e);
                System.out.println("An unexpected error occurred. Please try again.");
            }
        }
    }

    /**
     * Displays the menu for managing trainees and handles user input for trainee operations.
     */
    private void manageTrainees() {
        while (true) {
            traineeConsole.printMenu();
            try {
                Integer choice = Integer.parseInt(scanner.nextLine().trim());
                log.info("UserEntity selected trainee option: {}", choice);
                switch (choice) {
                    case 1:
                        traineeConsole.createTrainee();
                        break;
                    case 2:
                        traineeConsole.updateTrainee();
                        break;
                    case 3:
                        traineeConsole.deleteTrainee();
                        break;
                    case 4:
                        traineeConsole.viewTrainee();
                        break;
                    case 5:
                        traineeConsole.viewAllTrainee();
                        break;
                    case 6:
                        return;
                    default:
                        log.warn("Invalid trainee choice selected: {}", choice);
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                log.error("Invalid input for trainee option, expected a number.", e);
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                log.error("An unexpected error occurred while managing trainees.", e);
                System.out.println("An unexpected error occurred. Please try again.");
            }
        }
    }

    /**
     * Displays the menu for managing trainers and handles user input for trainer operations.
     */
    private void manageTrainers() {
        while (true) {
            trainerConsole.printMenu();
            try {
                Integer choice = Integer.parseInt(scanner.nextLine().trim());
                log.info("UserEntity selected trainer option: {}", choice);
                switch (choice) {
                    case 1:
                        trainerConsole.createTrainer();
                        break;
                    case 2:
                        trainerConsole.updateTrainer();
                        break;
                    case 3:
                        trainerConsole.viewTrainer();
                        break;
                    case 4:
                        trainerConsole.viewAllTrainer();
                        break;
                    case 5:
                        return;
                    default:
                        log.warn("Invalid trainer choice selected: {}", choice);
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                log.error("Invalid input for trainer option, expected a number.", e);
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                log.error("An unexpected error occurred while managing trainers.", e);
                System.out.println("An unexpected error occurred. Please try again.");
            }
        }
    }

    /**
     * Displays the menu for managing trainings and handles user input for training operations.
     */
    private void manageTrainings() {
        while (true) {
            trainingConsole.printMenu();
            try {
                Integer choice = Integer.parseInt(scanner.nextLine().trim());
                log.info("UserEntity selected training option: {}", choice);
                switch (choice) {
                    case 1:
                        trainingConsole.createTraining();
                        break;
                    case 2:
                        trainingConsole.viewTraining();
                        break;
                    case 3:
                        trainingConsole.viewAllTrainings();
                        break;
                    case 4:
                        return;
                    default:
                        log.warn("Invalid training choice selected: {}", choice);
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                log.error("Invalid input for training option, expected a number.", e);
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                log.error("An unexpected error occurred while managing trainings.", e);
                System.out.println("An unexpected error occurred. Please try again.");
            }
        }
    }

    /**
     * Prints the main menu of the application to the console.
     * The menu provides options to manage trainees, trainers, and trainings.
     */
    public void printMenu() {
        log.info("Displaying main menu.");
        String s = """
                Welcome to the Gym CRM System
                1. Manage Trainees
                2. Manage Trainers
                3. Manage Trainings
                4. Exit
                5. Choose an option:
                """;

        System.out.println(s);
    }
}


