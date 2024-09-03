package org.example.service;

import org.example.entity.TraineeEntity;
import org.example.repository.TraineeDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {
    @Mock
    private TraineeDAO traineeDao;

    @InjectMocks
    private TraineeService traineeService;

    @Test
    void testCreateTrainee() {
        TraineeEntity trainee = new TraineeEntity("2024-09-03T10:00:00", "123 Main St", "1");

        traineeService.createTrainee(trainee);

        verify(traineeDao, times(1)).createTrainee(trainee);
    }

    @Test
    void testUpdateTrainee() {
        TraineeEntity trainee = new TraineeEntity("2024-09-03T10:00:00", "123 Main St", "1");

        traineeService.updateTrainee(trainee);

        verify(traineeDao, times(1)).updateTrainee(trainee.getUserId(), trainee);
    }

    @Test
    void testDeleteTrainee() {
        String userId = "1";

        traineeService.deleteTrainee(userId);

        verify(traineeDao, times(1)).deleteTrainee(userId);
    }

    @Test
    void testGetTrainee() {
        String userId = "1";
        TraineeEntity trainee = new TraineeEntity("2024-09-03T10:00:00", "123 Main St", "1");

        when(traineeDao.getTrainee(userId)).thenReturn(trainee);

        TraineeEntity result = traineeService.getTrainee(userId);

        assertThat(result).isEqualTo(trainee);
        verify(traineeDao, times(1)).getTrainee(userId);
    }

    @Test
    void testGetAllTrainees() {
        List<TraineeEntity> trainees = Arrays.asList(
                new TraineeEntity("2024-09-03T10:00:00", "123 Main St", "1"),
                new TraineeEntity("2024-09-03T11:00:00", "456 Elm St", "2")
        );

        when(traineeDao.getAllTrainees()).thenReturn(trainees);

        List<TraineeEntity> result = traineeService.getAllTrainees();

        assertThat(result).isEqualTo(trainees);
        verify(traineeDao, times(1)).getAllTrainees();
    }
}
