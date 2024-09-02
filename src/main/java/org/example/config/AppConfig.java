package org.example.config;

import org.example.storage.InMemoryStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackages = "org.example")
//@PropertySource("classpath:application.properties")
public class AppConfig {
    @Bean(initMethod = "loadFromFile", destroyMethod = "saveToFile")
    public InMemoryStorage inMemoryStorage() {
        return new InMemoryStorage();
    }
}
