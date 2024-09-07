package org.example;

import org.example.config.AppConfig;
import org.example.console.ConsoleApp;
import org.example.console.TraineeConsoleImpl;
import org.example.console.TrainerConsoleImpl;
import org.example.console.TrainingConsoleImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        TraineeConsoleImpl traineeConsole = context.getBean(TraineeConsoleImpl.class);
        TrainerConsoleImpl trainerConsole = context.getBean(TrainerConsoleImpl.class);
        TrainingConsoleImpl trainingConsole = context.getBean(TrainingConsoleImpl.class);

        System.out.println("Beans initialized and ready to use");

        ConsoleApp consoleApp = new ConsoleApp(traineeConsole, trainerConsole, trainingConsole);
        consoleApp.run();

        ((AnnotationConfigApplicationContext) context).close();
    }
}

