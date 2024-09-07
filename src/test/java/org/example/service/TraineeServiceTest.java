package org.example.service;

import org.example.entity.TraineeEntity;
import org.example.repository.TraineeDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {
    @Mock
    private TraineeDAO traineeDao;

    @InjectMocks
    private TraineeService traineeService;

    private TraineeEntity traineeEntity;

    @BeforeEach
    void setUp() {
        // Setup a sample TraineeEntity
        traineeEntity = new TraineeEntity();
        traineeEntity.setUserId("trainee123");
        traineeEntity.setFirstName("John");
        traineeEntity.setLastName("Doe");
    }

    @Test
    void testCreateTrainee() {
        // Act
        traineeService.createTrainee(traineeEntity);

        // Assert
        verify(traineeDao, times(1)).createTrainee(traineeEntity);
    }

    @Test
    void testUpdateTrainee() {
        // Act
        traineeService.updateTrainee(traineeEntity);

        // Assert
        verify(traineeDao, times(1)).updateTrainee(traineeEntity.getUserId(), traineeEntity);
    }

    @Test
    void testDeleteTrainee() {
        String username = "trainee123";

        // Act
        traineeService.deleteTrainee(username);

        // Assert
        verify(traineeDao, times(1)).deleteTrainee(username);
    }

    @Test
    void testGetTrainee() {
        String userId = "trainee123";
        when(traineeDao.getTrainee(userId)).thenReturn(traineeEntity);

        // Act
        TraineeEntity result = traineeService.getTrainee(userId);

        // Assert
        assertNotNull(result);
        assertEquals(traineeEntity, result);
        verify(traineeDao, times(1)).getTrainee(userId);
    }

    @Test
    void testGetAllTrainees() {
        List<TraineeEntity> traineeList = Arrays.asList(traineeEntity, new TraineeEntity());
        when(traineeDao.getAllTrainees()).thenReturn(traineeList);

        // Act
        List<TraineeEntity> result = traineeService.getAllTrainees();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(traineeDao, times(1)).getAllTrainees();
    }

    @Test
    void testGetTraineeNotFound() {
        String userId = "nonExistingTrainee";
        when(traineeDao.getTrainee(userId)).thenReturn(null);

        // Act
        TraineeEntity result = traineeService.getTrainee(userId);

        // Assert
        assertNull(result);
        verify(traineeDao, times(1)).getTrainee(userId);
    }
}
