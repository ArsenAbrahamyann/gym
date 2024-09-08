package org.example.console;

import java.util.List;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingEntity;
import org.example.entity.dto.TrainingDto;
import org.example.entity.dto.TrainingTypeDto;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.example.utils.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * Console implementation for managing trainings.
 * <p>
 * This class provides methods for creating, viewing, and listing training entities.
 * It interacts with various services to handle user input and perform operations via the console.
 * </p>
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TrainingConsoleImpl {
    private final TrainingService trainingService;
    private final TraineeConsoleImpl traineeConsole;
    private final TrainerConsoleImpl trainerConsole;
    private final UserConsoleImpl userConsole;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final ModelMapper modelMapper;
    private final ValidationUtils validationUtils;
    private Scanner scanner = new Scanner(System.in);

    /**
     * Creates a new training by gathering input from the console.
     * <p>
     * This method collects details such as trainee username, trainer username, training name,
     * training type, date, and duration. It validates the input and then creates and saves a
     * {@link TrainingEntity} using the provided details.
     * </p>
     *
     * @throws Exception if an error occurs during input validation or training creation.
     */
    @SneakyThrows
    public void createTraining() {
        log.info("Starting to create a new training.");


        TrainingDto trainingDto = new TrainingDto();
        traineeConsole.viewAllTrainee();
        System.out.print("Enter trainee username: ");
        String traineeUsername = scanner.nextLine();
        validationUtils.validateTraineeExists(traineeUsername);
        trainingDto.setTraineeId(traineeUsername);

        trainerConsole.viewAllTrainer();
        System.out.print("Enter trainer username: ");
        String trainerUsername = scanner.nextLine();
        validationUtils.validateTrainerExists(trainerUsername);
        trainingDto.setTrainerId(trainerUsername);

        System.out.print("Enter training name: ");
        String trainingName = scanner.nextLine();
        trainingDto.setTrainingName(trainingName);

        System.out.print("Enter training type: ");
        String trainingTypeName = scanner.nextLine();
        TrainingTypeDto trainingTypeDto = new TrainingTypeDto(trainingTypeName);
        trainingDto.setTrainingTypeDto(trainingTypeDto);

        System.out.print("Enter training date (YYYY-MM-DD): ");
        String trainingDateInput = scanner.nextLine();
        validationUtils.validateBirthDate(trainingDateInput);
        trainingDto.setTrainingDate(trainingDateInput);

        System.out.print("Enter training duration (HH:MM): ");
        String trainingDurationInput = scanner.nextLine();
        validationUtils.validateTrainingDuration(trainingDurationInput);
        trainingDto.setTrainingDuration(trainingDurationInput);



        TrainingEntity trainingEntity = modelMapper.map(trainingDto, TrainingEntity.class);
        trainingService.createTraining(trainingEntity);
        log.info("TrainingEntity '{}' created successfully.", trainingName);
        System.out.println("TrainingEntity created successfully.");

    }

    /**
     * Displays details of a specific training based on its name.
     * <p>
     * Prompts the user to enter the training name and then retrieves and displays the details of the
     * corresponding {@link TrainingEntity}. Handles any errors that may occur during this process.
     * </p>
     */
    public void viewTraining() {
        log.info("Starting to view training details.");
        try {
            System.out.print("Enter training name to view: ");
            String trainingName = scanner.nextLine();

            TrainingEntity trainingEntity = trainingService.getTraining(trainingName);
            if (trainingEntity
                    != null) {
                TrainingDto trainingDto = modelMapper.map(trainingEntity, TrainingDto.class);
                System.out.println(trainingDto);
            } else {
                System.out.println("Training not found.");
            }
        } catch (Exception e) {
            log.error("Error occurred while viewing training: ", e);
            System.out.println("An error occurred while viewing the training. Please try again.");
        }
    }

    /**
     * Displays details of all existing trainings.
     * <p>
     * Retrieves and displays a list of all training entities from the service.
     * Handles cases where no trainings are found.
     * </p>
     */
    @SneakyThrows
    public void viewAllTrainings() {
        log.info("Fetching all trainings.");
        List<TrainingEntity> trainingEntities = trainingService.getAllTrainings();
        if (trainingEntities.isEmpty()) {
            System.out.println("No trainings found.");
        } else {
            trainingEntities.forEach(trainingEntity -> {
                TrainingDto trainingDto = modelMapper.map(trainingEntity, TrainingDto.class);
                System.out.println(trainingDto);
            });
        }

    }

    /**
     * Prints the menu options for managing trainings.
     * <p>
     * Displays a menu that allows users to choose from options such as creating, viewing, and listing
     * trainings or returning to the main menu.
     * </p>
     */
    public void printMenu() {
        log.info("Displaying menu options for managing trainings.");
        StringBuilder sb = new StringBuilder();
        sb.append("\nManage Trainings "
                +
                "\n1. Create TrainingEntity "
                +
                "\n2. View TrainingEntity "
                +
                "\n3. View All Trainings "
                +
                "\n4. Back to Main Menu"
                +
                "\nEnter your choice: ");
        System.out.println(sb);
    }
}
