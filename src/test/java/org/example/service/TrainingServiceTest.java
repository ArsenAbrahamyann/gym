package org.example.service;

import org.example.entity.TrainingEntity;
import org.example.repository.TrainingDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
    @Mock
    private TrainingDAO trainingDao;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void testCreateTraining() {
        TrainingEntity training = new TrainingEntity("1", "1", "Yoga", null, "2024-09-01", Duration.ofHours(2));

        trainingService.createTraining(training);

        verify(trainingDao, times(1)).createTraining(training);
    }

    @Test
    void testUpdateTraining() {
        TrainingEntity existingTraining = new TrainingEntity("1", "1", "Yoga", null, "2024-09-01", Duration.ofHours(2));
        String trainingName = "Yoga";

        trainingService.updateTraining(trainingName, existingTraining);

        verify(trainingDao, times(1)).updateTraining(trainingName, existingTraining);
    }

    @Test
    void testDeleteTraining() {
        String trainingName = "Yoga";

        trainingService.deleteTraining(trainingName);

        verify(trainingDao, times(1)).deleteTraining(trainingName);
    }

    @Test
    void testGetTraining() {
        String trainingName = "Yoga";
        TrainingEntity training = new TrainingEntity("1", "1", "Yoga", null, "2024-09-01", Duration.ofHours(2));

        when(trainingDao.getTraining(trainingName)).thenReturn(training);

        TrainingEntity result = trainingService.getTraining(trainingName);

        assertThat(result).isEqualTo(training);
        verify(trainingDao, times(1)).getTraining(trainingName);
    }

    @Test
    void testGetAllTrainings() {
        List<TrainingEntity> trainings = Arrays.asList(
                new TrainingEntity("1", "1", "Yoga", null, "2024-09-01", Duration.ofHours(2)),
                new TrainingEntity("2", "2", "Pilates", null, "2024-09-02", Duration.ofHours(1))
        );

        when(trainingDao.getAllTrainings()).thenReturn(trainings);

        List<TrainingEntity> result = trainingService.getAllTrainings();

        assertThat(result).isEqualTo(trainings);
        verify(trainingDao, times(1)).getAllTrainings();
    }
}
