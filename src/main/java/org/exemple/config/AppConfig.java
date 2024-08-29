package org.exemple.config;

import org.exemple.repository.TraineeDAO;
import org.exemple.repository.TrainerDAO;
import org.exemple.repository.TrainingDAO;
import org.exemple.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.io.IOException;


@Configuration
@ComponentScan(basePackages = "org.exemple")
//@PropertySource("classpath:application.properties")
public class AppConfig {
    @Bean(initMethod = "loadFromFile", destroyMethod = "saveToFile")
    public InMemoryStorage inMemoryStorage() {
        return new InMemoryStorage();
    }
}
