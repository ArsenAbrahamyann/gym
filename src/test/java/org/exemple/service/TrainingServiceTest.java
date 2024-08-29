package org.exemple.service;

import org.exemple.config.TestConfig;
import org.exemple.entity.Training;
import org.exemple.entity.TrainingType;
import org.exemple.repository.TrainerDAO;
import org.exemple.repository.TrainingDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.mockito.Mockito.*;

public class TrainingServiceTest {
    private TrainingDAO trainingDao;
    private TrainingService trainingService;
    @BeforeEach
    public void setUp() {
        trainingDao = mock(TrainingDAO.class);
        Scanner scanner = mock(Scanner.class);
        trainingService = new TrainingService(trainingDao);
        trainingService.setScanner(scanner);
    }

    @Test
    public void shouldCreateTrainingSuccessfully() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLine()).thenReturn("trainee1", "trainer1", "Training1", "Yoga", "2024-09-01", "01:30");
        trainingService.setScanner(scanner);

        doNothing().when(trainingDao).createTraining(any(Training.class));

        trainingService.createTraining();

        verify(trainingDao).createTraining(any(Training.class));
    }

    @Test
    public void shouldUpdateTrainingSuccessfully() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLine()).thenReturn("Training1", "trainee2", "trainer2", "UpdatedTraining", "Pilates", "2024-10-01", "02:00");
        trainingService.setScanner(scanner);

        Training existingTraining = new Training();
        existingTraining.setTrainingName("Training1");
        when(trainingDao.getTraining("Training1")).thenReturn(existingTraining);
        doNothing().when(trainingDao).updateTraining(anyString(), any(Training.class));

        trainingService.updateTraining();

        verify(trainingDao).updateTraining(anyString(), any(Training.class));
    }

    @Test
    public void shouldDeleteTrainingSuccessfully() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLine()).thenReturn("Training1");
        trainingService.setScanner(scanner);

        Training existingTraining = new Training();
        existingTraining.setTrainingName("Training1");
        when(trainingDao.getTraining("Training1")).thenReturn(existingTraining);
        doNothing().when(trainingDao).deleteTraining(anyString());

        trainingService.deleteTraining();

        verify(trainingDao).deleteTraining(anyString());
    }

    @Test
    public void shouldViewTrainingSuccessfully() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLine()).thenReturn("Training1");
        trainingService.setScanner(scanner);

        Training existingTraining = new Training();
        existingTraining.setTrainingName("Training1");
        existingTraining.setTraineeId("trainee1");
        existingTraining.setTrainerId("trainer1");
        existingTraining.setTrainingType(new TrainingType("Yoga"));
        existingTraining.setTrainingDate("2024-09-01");
        existingTraining.setTrainingDuration(Duration.ofHours(1).plusMinutes(30));
        when(trainingDao.getTraining("Training1")).thenReturn(existingTraining);

        trainingService.viewTraining();

        verify(trainingDao).getTraining(anyString());
    }

    @Test
    public void shouldViewAllTrainingsSuccessfully() {
        List<Training> trainings = new ArrayList<>();
        Training training = new Training();
        training.setTrainingName("Training1");
        trainings.add(training);
        when(trainingDao.getAllTrainings()).thenReturn(trainings);

        trainingService.viewAllTrainings();

        verify(trainingDao).getAllTrainings();
    }

    @Test
    public void shouldHandleInvalidDateFormatOnCreate() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLine()).thenReturn("trainee1", "trainer1", "Training1", "Yoga", "invalid-date", "01:30");
        trainingService.setScanner(scanner);

        doNothing().when(trainingDao).createTraining(any(Training.class));

        trainingService.createTraining();

        verify(trainingDao, never()).createTraining(any(Training.class));
    }

    @Test
    public void shouldHandleInvalidDurationFormatOnCreate() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLine()).thenReturn("trainee1", "trainer1", "Training1", "Yoga", "2024-09-01", "invalid-duration");
        trainingService.setScanner(scanner);

        doNothing().when(trainingDao).createTraining(any(Training.class));

        trainingService.createTraining();

        verify(trainingDao, never()).createTraining(any(Training.class));
    }
}
