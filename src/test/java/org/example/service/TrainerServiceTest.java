package org.example.service;
import org.example.entity.TrainerEntity;
import org.example.repository.TrainerDAO;
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
public class TrainerServiceTest {
    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private TrainerService trainerService;

    private TrainerEntity trainer;

    @BeforeEach
    void setUp() {
        trainer = new TrainerEntity("1", "Yoga Instructor");
    }

    @Test
    void testCreateTrainer() {
        trainerService.createTrainer(trainer);

        verify(trainerDAO, times(1)).createTrainer(trainer);
    }

    @Test
    void testUpdateTrainer() {
        trainerService.updateTrainer(trainer.getUserId(), trainer);

        verify(trainerDAO, times(1)).updateTrainer(trainer.getUserId(), trainer);
    }

    @Test
    void testDeleteTrainer() {
        String trainerId = "1";

        trainerService.deleteTrainer(trainerId);

        verify(userDAO, times(1)).deleteByUsername(trainerId);
        verify(trainerDAO, times(1)).deleteTrainer(trainerId);
    }

    @Test
    void testGetTrainer() {
        String userId = "1";
        when(trainerDAO.getTrainer(userId)).thenReturn(trainer);

        TrainerEntity result = trainerService.getTrainer(userId);

        assertThat(result).isEqualTo(trainer);
        verify(trainerDAO, times(1)).getTrainer(userId);
    }

    @Test
    void testGetAllTrainers() {
        List<TrainerEntity> trainers = Arrays.asList(
            new TrainerEntity("1", "Yoga Instructor"),
            new TrainerEntity("2", "Pilates Instructor")
        );
        when(trainerDAO.getAllTrainers()).thenReturn(trainers);

        List<TrainerEntity> result = trainerService.getAllTrainers();

        assertThat(result).hasSize(2).containsExactlyElementsOf(trainers);
        verify(trainerDAO, times(1)).getAllTrainers();
    }

    @Test
    void testDeleteTrainer_UserDaoFails() {
        String trainerId = "1";
        doThrow(new RuntimeException("User deletion failed")).when(userDAO).deleteByUsername(trainerId);

        try {
            trainerService.deleteTrainer(trainerId);
        } catch (RuntimeException e) {
            assertThat(e).hasMessageContaining("User deletion failed");
        }

        verify(userDAO, times(1)).deleteByUsername(trainerId);
        verify(trainerDAO, never()).deleteTrainer(trainerId);
    }
}
