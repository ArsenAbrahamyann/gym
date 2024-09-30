package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.example.config.AppConfig;
import org.example.controller.TraineeController;
import org.example.controller.TrainerController;
import org.example.controller.TrainingController;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
import org.example.dto.TrainingTypeDto;
import org.example.dto.UserDto;
import org.example.service.UserService;
import org.example.utils.UserUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * The Main class serves as the entry point for the Spring application.
 * It sets up the Spring application context, initializes services,
 * and demonstrates creating and saving trainer and trainee profiles.
 */
@Slf4j
public class Main {

    /**
     * The main method initializes the Spring application context, retrieves
     * services from the context, and performs operations related to creating
     * and saving trainer and trainee profiles.
     *
     * @param args Command-line arguments (not used in this implementation).
     */
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserService userService = context.getBean(UserService.class);
        UserUtils userUtils = context.getBean(UserUtils.class);

        // I think constructing the trainee object in the main method is not a good idea.
        // Because in the main method we should directly call the create method of the controller with only input dto values.
        // I would move the username and password generation method calls, userDto object creation and setting the user of the trainee
        // to the trainee service methods.

        List<String> allUsername = userService.findAllUsernames();

        String generatedUsername = userUtils.generateUsername("Arsen", "Abrahamyan", allUsername);

        String generatedPassword = userUtils.generatePassword();

        UserDto userDto = new UserDto("Arsen", "Abrahamyan", true, generatedUsername, generatedPassword);

        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setUser(userDto);
        traineeDto.setAddress("address");

        TraineeController traineeController = context.getBean(TraineeController.class);
        traineeController.createTrainee(traineeDto);

        traineeController.changeTraineePassword("Arsen.Abrahamyan", "NewPassword");

        traineeController.toggleTraineeStatus("Arsen.Abrahamyan");

        traineeDto.setAddress("UpdateAddres");
        traineeController.updateTraineeProfile(userDto.getUsername(), traineeDto);

        Set<TraineeDto> traineeDtos = new HashSet<>();
        traineeDtos.add(traineeDto);

        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setTrainees(traineeDtos);
        TrainingTypeDto trainingTypeDto = new TrainingTypeDto("Java");
        trainerDto.setSpecialization(trainingTypeDto);
        trainerDto.setUser(userDto);

        TrainerController trainerController = context.getBean(TrainerController.class);
        trainerController.createTrainer(trainerDto);

        Set<TrainerDto> trainerDtos = new HashSet<>();
        trainerDtos.add(trainerDto);
        traineeDto.setTrainers(trainerDtos);

        traineeController.getUnassignedTrainers("Arsen.Abrahamyan");

        trainerController.changeTrainerPassword("Arsen.Abrahamyan", "changePassword");
        trainerController.toggleTrainerStatus("Arsen.Abrahamyan");

        traineeDtos.add(traineeDto);
        trainerDto.setTrainees(traineeDtos);
        trainerController.updateTrainerProfile("Arsen.Abrahamyan", trainerDto);

        TrainingDto trainingDto = new TrainingDto(1L, 1L, "teacher",
                1L, 12);

        TrainingController trainingController = context.getBean(TrainingController.class);
        trainingController.addTraining(trainingDto);

        trainingController.getTrainingsForTrainee("Arsen.Abrahamyan",
                LocalDateTime.parse("2022-12-12T10:15:30"), LocalDateTime.now(),
                "Arsen.Abrahamyan1", "Java");
        trainingController.getTrainingsForTrainer("Arsen.Abrahamyan",
                LocalDateTime.parse("2022-12-12T10:15:30"), LocalDateTime.now(),
                "Arsen.Abrahamyan");

        List<Long> trainersId = new ArrayList<>();
        trainersId.add(1L);

        traineeController.assignTrainersToTrainee("Arsen.Abrahamyan", trainersId);

        traineeController.deleteTrainee("Arsen.Abrahamyan");

        ((AnnotationConfigApplicationContext) context).close();
    }
}

