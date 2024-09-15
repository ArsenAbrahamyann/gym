package org.example;

import java.sql.Date;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.example.config.AppConfig;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingTypeDto;
import org.example.dto.UserDto;
import org.example.entity.TraineeEntity;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.example.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


@Slf4j
public class Main {


    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserService userService = context.getBean(UserService.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainingService trainingService = context.getBean(TrainingService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);

        //creat testUser
        UserDto userDto = new UserDto();
        userDto.setFirstName("Arsen");
        userDto.setLastName("Abrahamyan");
        userDto.setIsActive(true);

        //creat testTrainee

        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setUser(userDto);
        traineeDto.setAddress("lvovyan");
        traineeDto.setDateOfBirth(Date.valueOf("1998-07-06"));

        //creat testTrainer

        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setUser(userDto);
        trainerDto.setTrainees((Set<TraineeEntity>) traineeDto);


        //creat testTrainingTypeName
        TrainingTypeDto trainingTypeDto = new TrainingTypeDto();
        trainingTypeDto.setTrainingTypeName("Java");

        trainerDto.setSpecialization(trainingTypeDto);
        trainerService.createTrainerProfile(trainerDto);
        traineeService.createTraineeProfile(traineeDto);
        // Use the context to get beans and run your application logic
        // For example, to get a UserService bean:
        // UserService userService = context.getBean(UserService.class);

        log.info("Spring application started.");

        // Close the context when done
        ((AnnotationConfigApplicationContext) context).close();
    }
}

