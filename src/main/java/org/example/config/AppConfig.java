package org.example.config;

import org.example.storage.InMemoryStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackages = "org.example")
public class AppConfig {
    @Bean
    public InMemoryStorage inMemoryStorage() {
        return new InMemoryStorage();
    }
}
