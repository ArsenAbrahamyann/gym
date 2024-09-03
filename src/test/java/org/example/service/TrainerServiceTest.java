package org.example.service;
import org.example.entity.TrainerEntity;
import org.example.repository.TrainerDAO;
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
public class TrainerServiceTest {
    @Mock
    private TrainerDAO trainerDAO;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void testCreateTrainer() {
        TrainerEntity trainer = new TrainerEntity("1", "Java Specialist");

        trainerService.createTrainer(trainer);

        verify(trainerDAO, times(1)).createTrainer(trainer);
    }

    @Test
    void testUpdateTrainer() {
        TrainerEntity trainer = new TrainerEntity("1", "Java Specialist");

        trainerService.updateTrainer(trainer.getUserId(), trainer);

        verify(trainerDAO, times(1)).updateTrainer(trainer.getUserId(), trainer);
    }

    @Test
    void testDeleteTrainer() {
        String userId = "1";

        trainerService.deleteTrainer(userId);

        verify(trainerDAO, times(1)).deleteTrainer(userId);
    }

    @Test
    void testGetTrainer() {
        String userId = "1";
        TrainerEntity trainer = new TrainerEntity("1", "Java Specialist");

        when(trainerDAO.getTrainer(userId)).thenReturn(trainer);

        TrainerEntity result = trainerService.getTrainer(userId);

        assertThat(result).isEqualTo(trainer);
        verify(trainerDAO, times(1)).getTrainer(userId);
    }

    @Test
    void testGetAllTrainers() {
        List<TrainerEntity> trainers = Arrays.asList(
                new TrainerEntity("1", "Java Specialist"),
                new TrainerEntity("2", "Python Specialist")
        );

        when(trainerDAO.getAllTrainers()).thenReturn(trainers);

        List<TrainerEntity> result = trainerService.getAllTrainers();

        assertThat(result).isEqualTo(trainers);
        verify(trainerDAO, times(1)).getAllTrainers();
    }
}
