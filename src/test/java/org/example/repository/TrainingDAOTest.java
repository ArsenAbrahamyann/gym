package org.example.repository;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
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
public class TrainingDAOTest {
    @Mock
    private TrainingDAO trainingDAO;

    private TrainingEntity training1;
    private TrainingEntity training2;

    @BeforeEach
    void setUp() {
        TrainingTypeEntity type1 = new TrainingTypeEntity("Cardio");
        TrainingTypeEntity type2 = new TrainingTypeEntity("Strength");

        training1 = new TrainingEntity("traineeId1", "trainerId1", "Morning Cardio", type1, "2024-09-06",
                Duration.ofHours(1));
        training2 = new TrainingEntity("traineeId2", "trainerId2", "Evening Strength", type2, "2024-09-07",
                Duration.ofHours(
                        (long) 1.5));
    }

    @Test
    void testCreateTraining() {
        doNothing().when(trainingDAO).createTraining(any(TrainingEntity.class));

        trainingDAO.createTraining(training1);
        verify(trainingDAO, times(1)).createTraining(training1);
    }

    @Test
    void testGetTraining() {
        when(trainingDAO.getTraining(anyString())).thenReturn(training1);

        TrainingEntity result = trainingDAO.getTraining("Morning Cardio");
        assertThat(result).isEqualTo(training1);
        verify(trainingDAO, times(1)).getTraining("Morning Cardio");
    }

    @Test
    void testGetAllTrainings() {
        when(trainingDAO.getAllTrainings()).thenReturn(Arrays.asList(training1, training2));

        List<TrainingEntity> result = trainingDAO.getAllTrainings();
        assertThat(result).hasSize(2).containsExactly(training1, training2);
        verify(trainingDAO, times(1)).getAllTrainings();
    }
}
