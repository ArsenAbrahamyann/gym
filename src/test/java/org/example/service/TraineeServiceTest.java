package org.example.service;

import org.example.entity.TraineeEntity;
import org.example.repository.TraineeDAO;
import org.example.repository.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {
    @Mock
    private TraineeDAO traineeDao;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private TraineeService traineeService;

    private TraineeEntity trainee;

    @BeforeEach
    void setUp() {
        trainee = new TraineeEntity("2024-09-03T10:00:00", "123 Main St", "1");
    }

    @Test
    void testCreateTrainee() {
        traineeService.createTrainee(trainee);

        verify(traineeDao, times(1)).createTrainee(trainee);
    }

    @Test
    void testUpdateTrainee() {
        traineeService.updateTrainee(trainee);

        verify(traineeDao, times(1)).updateTrainee(trainee.getUserId(), trainee);
    }

    @Test
    void testDeleteTrainee() {
        String userId = "1";

        traineeService.deleteTrainee(userId);

        verify(userDAO, times(1)).deleteByUsername(userId);
        verify(traineeDao, times(1)).deleteTrainee(userId);
    }

    @Test
    void testGetTrainee() {
        String userId = "1";
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

        assertThat(result).hasSize(2).containsExactlyElementsOf(trainees);
        verify(traineeDao, times(1)).getAllTrainees();
    }

    @Test
    void testDeleteTrainee_UserDaoFails() {
        String userId = "1";
        doThrow(new RuntimeException("User deletion failed")).when(userDAO).deleteByUsername(userId);

        try {
            traineeService.deleteTrainee(userId);
        } catch (RuntimeException e) {
            assertThat(e).hasMessageContaining("User deletion failed");
        }

        verify(userDAO, times(1)).deleteByUsername(userId);
        verify(traineeDao, never()).deleteTrainee(userId);
    }
}
