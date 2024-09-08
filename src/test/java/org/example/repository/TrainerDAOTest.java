package org.example.repository;

import java.util.Arrays;
import java.util.List;
import org.example.entity.TrainerEntity;
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
public class TrainerDAOTest {
    @Mock
    private TrainerDAO trainerDAO;

    private TrainerEntity trainer1;
    private TrainerEntity trainer2;

    @BeforeEach
    void setUp() {
        trainer1 = new TrainerEntity("userId1", "Fitness");
        trainer2 = new TrainerEntity("userId2", "Yoga");
    }

    @Test
    void testCreateTrainer() {
        doNothing().when(trainerDAO).createTrainer(any(TrainerEntity.class));

        trainerDAO.createTrainer(trainer1);
        verify(trainerDAO, times(1)).createTrainer(trainer1);
    }

    @Test
    void testUpdateTrainer() {
        doNothing().when(trainerDAO).updateTrainer(anyString(), any(TrainerEntity.class));

        trainerDAO.updateTrainer("userId1", trainer1);
        verify(trainerDAO, times(1)).updateTrainer("userId1", trainer1);
    }

    @Test
    void testGetTrainer() {
        when(trainerDAO.getTrainer(anyString())).thenReturn(trainer1);

        TrainerEntity result = trainerDAO.getTrainer("userId1");
        assertThat(result).isEqualTo(trainer1);
        verify(trainerDAO, times(1)).getTrainer("userId1");
    }

    @Test
    void testGetAllTrainers() {
        when(trainerDAO.getAllTrainers()).thenReturn(Arrays.asList(trainer1, trainer2));

        List<TrainerEntity> result = trainerDAO.getAllTrainers();
        assertThat(result).hasSize(2).containsExactly(trainer1, trainer2);
        verify(trainerDAO, times(1)).getAllTrainers();
    }
}

