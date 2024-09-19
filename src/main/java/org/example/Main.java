package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.config.AppConfig;
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


        ((AnnotationConfigApplicationContext) context).close();
    }
}

