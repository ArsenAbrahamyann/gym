package org.example.service;

import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.repository.TrainingDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
    @Mock
    private TrainingDAO trainingDAO;

    @InjectMocks
    private TrainingService trainingService;

    private TrainingEntity training;
    private TrainingTypeEntity trainingType;

    @BeforeEach
    void setUp() {
        trainingType = new TrainingTypeEntity("Yoga");
        training = new TrainingEntity(
            "1",
            "2",
            "Morning Yoga",
            trainingType,
            "2024-09-10",
            Duration.ofHours(1)
        );
    }

    @Test
    void testCreateTraining() {
        trainingService.createTraining(training);

        verify(trainingDAO, times(1)).createTraining(training);
    }

    @Test
    void testUpdateTraining() {
        trainingService.updateTraining(training.getTrainingName(), training);

        verify(trainingDAO, times(1)).updateTraining(training.getTrainingName(), training);
    }

    @Test
    void testDeleteTraining() {
        String trainingName = "Morning Yoga";

        trainingService.deleteTraining(trainingName);

        verify(trainingDAO, times(1)).deleteTraining(trainingName);
    }

    @Test
    void testGetTraining() {
        String trainingName = "Morning Yoga";
        when(trainingDAO.getTraining(trainingName)).thenReturn(training);

        TrainingEntity result = trainingService.getTraining(trainingName);

        assertThat(result).isEqualTo(training);
        verify(trainingDAO, times(1)).getTraining(trainingName);
    }

    @Test
    void testGetAllTrainings() {
        List<TrainingEntity> trainings = Arrays.asList(
            new TrainingEntity("1", "2", "Morning Yoga", trainingType, "2024-09-10", Duration.ofHours(1)),
            new TrainingEntity("3", "4", "Evening Yoga", trainingType, "2024-09-11", Duration.ofHours(2))
        );
        when(trainingDAO.getAllTrainings()).thenReturn(trainings);

        List<TrainingEntity> result = trainingService.getAllTrainings();

        assertThat(result).hasSize(2).containsExactlyElementsOf(trainings);
        verify(trainingDAO, times(1)).getAllTrainings();
    }
}
