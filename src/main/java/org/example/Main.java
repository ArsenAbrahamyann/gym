package org.example;

import org.example.config.AppConfig;

import org.example.console.ConsoleApp;
import org.example.console.TraineeConsoleImpl;
import org.example.console.TrainerConsoleImpl;
import org.example.console.TrainingConsoleImpl;
import org.example.console.UserConsoleImpl;
import org.example.entity.UserUtils;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.example.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;



public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TrainingService trainingService = context.getBean(TrainingService.class);
        UserService userService = context.getBean(UserService.class);

        System.out.println("Beans initialized and ready to use");

        UserConsoleImpl userConsole = new UserConsoleImpl(userService);
        TraineeConsoleImpl traineeConsole = new TraineeConsoleImpl(traineeService,userConsole,userService);
        TrainerConsoleImpl trainerConsole = new TrainerConsoleImpl(trainerService,userConsole,userService);
        TrainingConsoleImpl trainingConsole = new TrainingConsoleImpl(trainingService,traineeConsole,trainerConsole,userConsole,traineeService,trainerService);

        ConsoleApp consoleApp = new ConsoleApp(traineeConsole, trainerConsole, trainingConsole);
        consoleApp.run();

        ((AnnotationConfigApplicationContext) context).close();
    }
}

