package org.exemple.config;

import org.exemple.repository.TraineeDAO;
import org.exemple.repository.TrainerDAO;
import org.exemple.repository.TrainingDAO;
import org.exemple.service.TraineeService;
import org.exemple.service.TrainerService;
import org.exemple.service.TrainingService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
    @Bean
    public TraineeDAO traineeDao() {
        return Mockito.mock(TraineeDAO.class);
    }

    @Bean
    public TraineeService traineeService() {
        return new TraineeService(traineeDao());
    }
    @Bean
    public TrainerDAO trainerDAO() {
        return Mockito.mock(TrainerDAO.class);
    }

    @Bean
    public TrainerService trainerService() {
        return new TrainerService(trainerDAO());
    }

    @Bean
    public TrainingDAO trainingDAO() {
        return Mockito.mock(TrainingDAO.class);
    }

    @Bean
    public TrainingService trainingService() {
        return new TrainingService(trainingDAO());
    }
}
