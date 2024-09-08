package org.example.repository;

import java.util.Arrays;
import java.util.List;
import org.example.entity.TraineeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TraineeDAOTest {
    @Mock
    private TraineeDAO traineeDAO;

    private TraineeEntity trainee1;
    private TraineeEntity trainee2;

    @BeforeEach
    void setUp() {
        trainee1 = new TraineeEntity("2024-09-01T10:00", "123 Main St", "userId1");
        trainee2 = new TraineeEntity("2024-09-02T14:00", "456 Oak St", "userId2");
    }

    @Test
    void testCreateTrainee() {
        doNothing().when(traineeDAO).createTrainee(any(TraineeEntity.class));

        traineeDAO.createTrainee(trainee1);
        verify(traineeDAO, times(1)).createTrainee(trainee1);
    }

    @Test
    void testUpdateTrainee() {
        doNothing().when(traineeDAO).updateTrainee(anyString(), any(TraineeEntity.class));

        traineeDAO.updateTrainee("userId1", trainee1);
        verify(traineeDAO, times(1)).updateTrainee("userId1", trainee1);
    }

    @Test
    void testDeleteTrainee() {
        doNothing().when(traineeDAO).deleteTrainee(anyString());

        traineeDAO.deleteTrainee("userId1");
        verify(traineeDAO, times(1)).deleteTrainee("userId1");
    }

    @Test
    void testGetTrainee() {
        when(traineeDAO.getTrainee(anyString())).thenReturn(trainee1);

        TraineeEntity result = traineeDAO.getTrainee("userId1");
        assertThat(result).isEqualTo(trainee1);
        verify(traineeDAO, times(1)).getTrainee("userId1");
    }

    @Test
    void testGetAllTrainees() {
        when(traineeDAO.getAllTrainees()).thenReturn(Arrays.asList(trainee1, trainee2));

        List<TraineeEntity> result = traineeDAO.getAllTrainees();
        assertThat(result).hasSize(2).containsExactly(trainee1, trainee2);
        verify(traineeDAO, times(1)).getAllTrainees();
    }
}
