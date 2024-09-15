package org.example;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.example.config.AppConfig;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.dto.UserDto;
import org.example.entity.TrainerEntity;
import org.example.service.TraineeService;
import org.example.service.UserService;
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
        UserService bean = context.getBean(UserService.class);
        UserDto userDto = new UserDto("Arsen", "Abrahamyan", true);
        Set<TrainerDto> trainerDtos = new HashSet<>();
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setUser(userDto);
        trainerDtos.add(trainerDto);
        TraineeService traineeService = context.getBean(TraineeService.class);
        TraineeDto traineeDto = new TraineeDto(Date.valueOf("2023-09-15"), "test", userDto, trainerDtos);
        traineeService.createTraineeProfile(traineeDto);


        ((AnnotationConfigApplicationContext) context).close();
    }
}

