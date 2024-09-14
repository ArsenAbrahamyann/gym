package org.example.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.example.entity.TraineeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class TraineeDaoTest {
    @Mock
    private TraineeDao traineeDao;

    private TraineeEntity trainee1;
    private TraineeEntity trainee2;

    @BeforeEach
    void setUp() {
        trainee1 = new TraineeEntity("2024-09-01T10:00", "123 Main St", "userId1");
        trainee2 = new TraineeEntity("2024-09-02T14:00", "456 Oak St", "userId2");
    }

    @Test
    void testCreateTrainee() {
        doNothing().when(traineeDao).createTrainee(any(TraineeEntity.class));

        traineeDao.createTrainee(trainee1);
        verify(traineeDao, times(1)).createTrainee(trainee1);
    }

    @Test
    void testUpdateTrainee() {
        doNothing().when(traineeDao).updateTrainee(anyString(), any(TraineeEntity.class));

        traineeDao.updateTrainee("userId1", trainee1);
        verify(traineeDao, times(1)).updateTrainee("userId1", trainee1);
    }

    @Test
    void testDeleteTrainee() {
        doNothing().when(traineeDao).deleteTrainee(anyString());

        traineeDao.deleteTrainee("userId1");
        verify(traineeDao, times(1)).deleteTrainee("userId1");
    }

    @Test
    void testGetTrainee() {
        when(traineeDao.getTrainee(anyString())).thenReturn(trainee1);

        TraineeEntity result = traineeDao.getTrainee("userId1");
        assertThat(result).isEqualTo(trainee1);
        verify(traineeDao, times(1)).getTrainee("userId1");
    }

    @Test
    void testGetAllTrainees() {
        when(traineeDao.getAllTrainees()).thenReturn(Arrays.asList(trainee1, trainee2));

        List<TraineeEntity> result = traineeDao.getAllTrainees();
        assertThat(result).hasSize(2).containsExactly(trainee1, trainee2);
        verify(traineeDao, times(1)).getAllTrainees();
    }
}
