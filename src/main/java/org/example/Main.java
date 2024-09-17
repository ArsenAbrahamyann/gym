package org.example;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.example.config.AppConfig;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
import org.example.dto.TrainingTypeDto;
import org.example.dto.UserDto;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
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

        List<String> allUsername = userService.findAllUsernames();
        String generatedUsername = userUtils.generateUsername("Arsen",
                "Abrahamyan", allUsername);
        String generatedPassword = userUtils.generatePassword();
        UserDto userDto = new UserDto("Arsen", "Abrahamyan", true, generatedUsername, generatedPassword);

        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setUser(userDto);
        traineeDto.setAddress("asas");

        TraineeService traineeService = context.getBean(TraineeService.class);
        traineeService.createTraineeProfile(traineeDto);
        traineeService.changeTraineePassword("Arsen.Abrahamyan", "111111as");
        traineeService.toggleTraineeStatus("Arsen.Abrahamyan");
        traineeDto.setAddress("111");
        traineeService.updateTraineeProfile(userDto.getUsername(),traineeDto);
        Set<TraineeDto> traineeDtos = new HashSet<>();
        traineeDtos.add(traineeDto);
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setTrainees(traineeDtos);
        TrainingTypeDto trainingTypeDto = new TrainingTypeDto("Java");
        trainerDto.setSpecialization(trainingTypeDto);
        trainerDto.setUser(userDto);
        TrainerService trainerService = context.getBean(TrainerService.class);
        trainerService.createTrainerProfile(trainerDto);

        Set<TrainerDto> trainerDtos = new HashSet<>();
        trainerDtos.add(trainerDto);
        traineeDto.setTrainers(trainerDtos);
        traineeService.getUnassignedTrainers("Arsen.Abrahamyan");
        trainerService.changeTrainerPassword("Arsen.Abrahamyan1", "222222");
        trainerService.toggleTrainerStatus("Arsen.Abrahamyan1");
        traineeDtos.add(traineeDto);
        trainerDto.setTrainees(traineeDtos);
        trainerService.updateTrainerProfile("Arsen.Abrahamyan1",trainerDto);

        TrainingDto trainingDto = new TrainingDto(traineeDto,trainerDto,"techer",trainingTypeDto,12);
        TrainingService trainingService = context.getBean(TrainingService.class);
        trainingService.addTraining(trainingDto);
        ((AnnotationConfigApplicationContext) context).close();
    }
}

