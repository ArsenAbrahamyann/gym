package org.example;

import org.example.config.AppConfig;

import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;



public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TrainingService trainingService = context.getBean(TrainingService.class);

        System.out.println("Beans initialized and ready to use");

        ConsoleApp consoleApp = new ConsoleApp(traineeService, trainerService, trainingService);
        consoleApp.run();

        ((AnnotationConfigApplicationContext) context).close();
    }
}

