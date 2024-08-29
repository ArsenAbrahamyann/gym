package org.exemple;

import org.exemple.config.AppConfig;

import org.exemple.service.TraineeService;
import org.exemple.service.TrainerService;
import org.exemple.service.TrainingService;
import org.exemple.storage.InMemoryStorage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;



public class Main {
    public static void main(String[] args) {
        InMemoryStorage storage = new InMemoryStorage();
        storage.loadFromFile();
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TrainingService trainingService = context.getBean(TrainingService.class);

        System.out.println("Beans initialized and ready to use");

        ConsoleApp consoleApp = new ConsoleApp(traineeService, trainerService, trainingService);
        consoleApp.run();
        storage.saveToFile();
    }
}

