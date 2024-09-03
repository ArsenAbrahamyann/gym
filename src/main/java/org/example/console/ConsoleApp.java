package org.example.console;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.entity.UserUtils;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

@Slf4j
public class ConsoleApp {
    private final TraineeConsoleImpl traineeConsole;
    private final TrainerConsoleImpl trainerConsole;
    private final TrainingConsoleImpl trainingConsole;
    private final Scanner scanner;

    public ConsoleApp(TraineeConsoleImpl traineeConsole, TrainerConsoleImpl trainerConsole, TrainingConsoleImpl trainingConsole) {
        this.traineeConsole = traineeConsole;
        this.trainerConsole = trainerConsole;
        this.trainingConsole = trainingConsole;
        this.scanner = new Scanner(System.in);
    }


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
                        trainerConsole.deleteTrainer();
                        break;
                    case 4:
                        trainerConsole.viewTrainer();
                        break;
                    case 5:
                        trainerConsole.viewAllTrainer();
                        break;
                    case 6:
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
                        trainingConsole.updateTraining();
                        break;
                    case 5:
                        trainingConsole.deleteTraining();
                        break;
                    case 6:
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

    public void printMenu() {
        log.info("Displaying main menu.");
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


