package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.storage.InMemoryStorage;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration class that defines Spring beans and
 * performs component scanning for the specified base package.
 */
@Configuration
@ComponentScan(basePackages = "org.example")
public class AppConfig {

    /**
     * Defines an {@link InMemoryStorage} bean that will be managed by the Spring container.
     *
     * @return a new instance of {@link InMemoryStorage}.
     */
    @Bean
    public InMemoryStorage inMemoryStorage() {
        return new InMemoryStorage(objectMapper());
    }

    /**
     * Defines an {@link ModelMapper} bean that will be managed by the Spring container.
     *
     * @return a new instance of {@link ModelMapper}.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


}
