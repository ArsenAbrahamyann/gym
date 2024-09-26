package org.example.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import org.example.dto.TrainerDto;
import org.example.entity.TrainingEntity;
import org.example.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit test class for TrainerController.
 */
@ExtendWith(MockitoExtension.class)
public class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerController trainerController;

    private TrainerDto trainerDto;
    private TrainingEntity trainingEntity;

    /**
     * Initializes the necessary objects before each test case is executed.
     * This method is annotated with {@code @BeforeEach}, which ensures that
     * it runs before each individual test in the class. The method sets up
     * a new {@link TrainerDto} and a {@link TrainingEntity} to be used in
     * the test cases. This ensures that each test has fresh instances of
     * these objects and prevents shared state between tests.
     * This setup is essential for maintaining test isolation and ensuring
     * that changes made in one test do not affect the others.
     */
    @BeforeEach
    void setUp() {
        trainerDto = new TrainerDto();
        trainingEntity = new TrainingEntity();
    }

    @Test
    void testCreateTrainer() {
        when(trainerService.createTrainerProfile(any(TrainerDto.class)))
                .thenReturn(trainerDto);

        TrainerDto createdTrainer = trainerController.createTrainer(trainerDto);

        assertNotNull(createdTrainer);
        verify(trainerService, times(1)).createTrainerProfile(trainerDto);
    }

    @Test
    void testUpdateTrainerProfile() {
        doNothing().when(trainerService).updateTrainerProfile(anyString(), any(TrainerDto.class));

        trainerController.updateTrainerProfile("username", trainerDto);

        verify(trainerService, times(1)).updateTrainerProfile(eq("username"),
                eq(trainerDto));
    }

    @Test
    void testChangeTrainerPassword() {
        doNothing().when(trainerService).changeTrainerPassword(anyString(), anyString());

        trainerController.changeTrainerPassword("username", "newPassword");

        verify(trainerService, times(1)).changeTrainerPassword(eq("username"),
                eq("newPassword"));
    }

    @Test
    void testToggleTrainerStatus() {
        doNothing().when(trainerService).toggleTrainerStatus(anyString());

        trainerController.toggleTrainerStatus("username");

        verify(trainerService, times(1)).toggleTrainerStatus(eq("username"));
    }

    @Test
    void testGetTrainerTrainings() {
        LocalDateTime fromDate = LocalDateTime.now().minusDays(1);
        LocalDateTime toDate = LocalDateTime.now();
        List<TrainingEntity> trainingEntities = List.of(trainingEntity);

        when(trainerService.getTrainerTrainings(anyString(), any(LocalDateTime.class),
                any(LocalDateTime.class), anyString()))
                .thenReturn(trainingEntities);

        List<TrainingEntity> result = trainerController.getTrainerTrainings("trainerUsername",
                fromDate, toDate, "traineeName");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(trainerService, times(1)).getTrainerTrainings(eq("trainerUsername"),
                eq(fromDate), eq(toDate), eq("traineeName"));
    }
}
