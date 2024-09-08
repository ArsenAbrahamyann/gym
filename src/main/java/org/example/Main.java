package org.example;

import org.example.config.AppConfig;
import org.example.console.ConsoleApp;
import org.example.console.TraineeConsoleImpl;
import org.example.console.TrainerConsoleImpl;
import org.example.console.TrainingConsoleImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * The main entry point of the application.
 * <p>
 * This class initializes the Spring application context using {@link AnnotationConfigApplicationContext},
 * retrieves and sets up the necessary console components from the context, and then starts the console application.
 * </p>
 */
public class Main {

    /**
     * The main method which serves as the entry point for the application.
     * <p>
     * It performs the following steps:
     * <ol>
     *     <li>Initializes the Spring application context using {@link AnnotationConfigApplicationContext} with the
     *     configuration provided by {@link AppConfig}.</li>
     *     <li>Retrieves instances of {@link TraineeConsoleImpl}, {@link TrainerConsoleImpl}, and {@link TrainingConsoleImpl}
     *     from the application context.</li>
     *     <li>Prints a message indicating that the beans are initialized and ready to use.</li>
     *     <li>Creates an instance of {@link ConsoleApp} using the retrieved console components and starts it by calling
     *     the {@link ConsoleApp#run()} method.</li>
     *     <li>Closes the application context to release resources.</li>
     * </ol>
     * </p>
     *
     * @param args command-line arguments passed to the application. This implementation does not use them.
     */
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

