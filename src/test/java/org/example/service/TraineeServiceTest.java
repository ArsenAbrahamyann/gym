package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.example.entity.TraineeEntity;
import org.example.repository.TraineeDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {
    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private TraineeService traineeService;

    private TraineeEntity traineeEntity;

    @BeforeEach
    void setUp() {
        traineeEntity = new TraineeEntity();
        traineeEntity.setUserId("trainee123");
        traineeEntity.setFirstName("John");
        traineeEntity.setLastName("Doe");
    }

    @Test
    void testCreateTrainee() {
        traineeService.createTrainee(traineeEntity);

        verify(traineeDao, times(1)).createTrainee(traineeEntity);
    }

    @Test
    void testUpdateTrainee() {
        traineeService.updateTrainee(traineeEntity);

        verify(traineeDao, times(1)).updateTrainee(traineeEntity.getUserId(), traineeEntity);
    }

    @Test
    void testDeleteTrainee() {
        String username = "trainee123";

        traineeService.deleteTrainee(username);

        verify(traineeDao, times(1)).deleteTrainee(username);
    }

    @Test
    void testGetTrainee() {
        String userId = "trainee123";
        when(traineeDao.getTrainee(userId)).thenReturn(traineeEntity);

        TraineeEntity result = traineeService.getTrainee(userId);

        assertNotNull(result);
        assertEquals(traineeEntity, result);
        verify(traineeDao, times(1)).getTrainee(userId);
    }

    @Test
    void testGetAllTrainees() {
        List<TraineeEntity> traineeList = Arrays.asList(traineeEntity, new TraineeEntity());
        when(traineeDao.getAllTrainees()).thenReturn(traineeList);

        List<TraineeEntity> result = traineeService.getAllTrainees();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(traineeDao, times(1)).getAllTrainees();
    }

    @Test
    void testGetTraineeNotFound() {
        String userId = "nonExistingTrainee";
        when(traineeDao.getTrainee(userId)).thenReturn(null);

        TraineeEntity result = traineeService.getTrainee(userId);

        assertNull(result);
        verify(traineeDao, times(1)).getTrainee(userId);
    }
}
