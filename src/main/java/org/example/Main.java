package org.example;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.AppConfig;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
import org.example.dto.TrainingTypeDto;
import org.example.dto.UserDto;
import org.example.entity.TrainingTypeEntity;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.UserRepository;
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
@RequiredArgsConstructor
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
        UserDto userDto = new UserDto("Arsen", "Abrahamyan", true,generatedUsername,generatedPassword);

        Set<TrainerDto> trainerDtos = new HashSet<>();
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setUser(userDto);
        trainerDto.setSpecialization(new TrainingTypeDto("Java"));
        trainerDtos.add(trainerDto);

        TraineeService traineeService = context.getBean(TraineeService.class);
        List<String> allUsername1 = userService.findAllUsernames();
        String generatedUsername1 = userUtils.generateUsername("Afssrsen",
                "Abrahamyan", allUsername);
        String generatedPassword1 = userUtils.generatePassword();
        UserDto userDto1 = new UserDto("Arsen", "Abrahamyan", true, generatedUsername1, generatedPassword1);
        TraineeDto traineeDto = new TraineeDto(Date.valueOf("2023-09-15"), "test", userDto1, trainerDtos);
        TrainerService trainerService = context.getBean(TrainerService.class);
        trainerService.createTrainerProfile(trainerDto); // Assuming a method to save trainer exists
        traineeService.createTraineeProfile(traineeDto);
        userService.authenticateUser("Arsen.Abrhamayan","chgitem");
        TrainingTypeDto trainingTypeDto = new TrainingTypeDto("Java");
        TrainingDto trainingDto = new TrainingDto(traineeDto,trainerDto,"Java",
                trainingTypeDto,Date.valueOf("1111-11-11"),11);
        TrainingService trainingService = context.getBean(TrainingService.class);
        trainingService.addTraining(trainingDto);

        ((AnnotationConfigApplicationContext) context).close();
    }
}

