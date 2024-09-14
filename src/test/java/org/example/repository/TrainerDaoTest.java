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
import org.example.entity.TrainerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainerDaoTest {
    @Mock
    private TrainerDao trainerDao;

    private TrainerEntity trainer1;
    private TrainerEntity trainer2;

    @BeforeEach
    void setUp() {
        trainer1 = new TrainerEntity("userId1", "Fitness");
        trainer2 = new TrainerEntity("userId2", "Yoga");
    }

    @Test
    void testCreateTrainer() {
        doNothing().when(trainerDao).createTrainer(any(TrainerEntity.class));

        trainerDao.createTrainer(trainer1);
        verify(trainerDao, times(1)).createTrainer(trainer1);
    }

    @Test
    void testUpdateTrainer() {
        doNothing().when(trainerDao).updateTrainer(anyString(), any(TrainerEntity.class));

        trainerDao.updateTrainer("userId1", trainer1);
        verify(trainerDao, times(1)).updateTrainer("userId1", trainer1);
    }

    @Test
    void testGetTrainer() {
        when(trainerDao.getTrainer(anyString())).thenReturn(trainer1);

        TrainerEntity result = trainerDao.getTrainer("userId1");
        assertThat(result).isEqualTo(trainer1);
        verify(trainerDao, times(1)).getTrainer("userId1");
    }

    @Test
    void testGetAllTrainers() {
        when(trainerDao.getAllTrainers()).thenReturn(Arrays.asList(trainer1, trainer2));

        List<TrainerEntity> result = trainerDao.getAllTrainers();
        assertThat(result).hasSize(2).containsExactly(trainer1, trainer2);
        verify(trainerDao, times(1)).getAllTrainers();
    }
}

