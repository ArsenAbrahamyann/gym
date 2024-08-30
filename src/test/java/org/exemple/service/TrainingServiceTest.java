package org.exemple.service;

import org.exemple.entity.Training;
import org.exemple.entity.TrainingType;
import org.exemple.repository.TrainingDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.mockito.Mockito.*;

public class TrainingServiceTest {
    private final TrainingDAO trainingDao = mock(TrainingDAO.class);
    private final Scanner scanner = mock(Scanner.class);
    private TrainingService underTest;

    @BeforeEach
    public void setUp() {
        underTest = new TrainingService(trainingDao);
        underTest.setScanner(scanner);
    }

    @Test
    public void shouldCreateTrainingSuccessfully() {
        when(scanner.nextLine()).thenReturn("trainee1", "trainer1", "Training1", "Yoga", "2024-09-01", "01:30");
        underTest.setScanner(scanner);

        doNothing().when(trainingDao).createTraining(any(Training.class));

        underTest.createTraining();

        verify(trainingDao).createTraining(any(Training.class));
    }

    @Test
    public void shouldUpdateTrainingSuccessfully() {
        when(scanner.nextLine()).thenReturn("Training1", "trainee2", "trainer2", "UpdatedTraining", "Pilates", "2024-10-01", "02:00");
        underTest.setScanner(scanner);

        Training existingTraining = new Training();
        existingTraining.setTrainingName("Training1");
        when(trainingDao.getTraining("Training1")).thenReturn(existingTraining);
        doNothing().when(trainingDao).updateTraining(anyString(), any(Training.class));

        underTest.updateTraining();

        verify(trainingDao).updateTraining(anyString(), any(Training.class));
    }

    @Test
    public void shouldDeleteTrainingSuccessfully() {
        when(scanner.nextLine()).thenReturn("Training1");
        underTest.setScanner(scanner);

        Training existingTraining = new Training();
        existingTraining.setTrainingName("Training1");
        when(trainingDao.getTraining("Training1")).thenReturn(existingTraining);
        doNothing().when(trainingDao).deleteTraining(anyString());

        underTest.deleteTraining();

        verify(trainingDao).deleteTraining(anyString());
    }

    @Test
    public void shouldViewTrainingSuccessfully() {
        when(scanner.nextLine()).thenReturn("Training1");
        underTest.setScanner(scanner);

        Training existingTraining = new Training();
        existingTraining.setTrainingName("Training1");
        existingTraining.setTraineeId("trainee1");
        existingTraining.setTrainerId("trainer1");
        existingTraining.setTrainingType(new TrainingType("Yoga"));
        existingTraining.setTrainingDate("2024-09-01");
        existingTraining.setTrainingDuration(Duration.ofHours(1).plusMinutes(30));
        when(trainingDao.getTraining("Training1")).thenReturn(existingTraining);

        underTest.viewTraining();

        verify(trainingDao).getTraining(anyString());
    }

    @Test
    public void shouldViewAllTrainingsSuccessfully() {
        List<Training> trainings = new ArrayList<>();
        Training training = new Training();
        training.setTrainingName("Training1");
        trainings.add(training);
        when(trainingDao.getAllTrainings()).thenReturn(trainings);

        underTest.viewAllTrainings();

        verify(trainingDao).getAllTrainings();
    }

    @Test
    public void shouldHandleInvalidDateFormatOnCreate() {
        when(scanner.nextLine()).thenReturn("trainee1", "trainer1", "Training1", "Yoga", "invalid-date", "01:30");
        underTest.setScanner(scanner);

        doNothing().when(trainingDao).createTraining(any(Training.class));

        underTest.createTraining();

        verify(trainingDao, never()).createTraining(any(Training.class));
    }

    @Test
    public void shouldHandleInvalidDurationFormatOnCreate() {
        when(scanner.nextLine()).thenReturn("trainee1", "trainer1", "Training1", "Yoga", "2024-09-01", "invalid-duration");
        underTest.setScanner(scanner);

        doNothing().when(trainingDao).createTraining(any(Training.class));

        underTest.createTraining();

        verify(trainingDao, never()).createTraining(any(Training.class));
    }
}
