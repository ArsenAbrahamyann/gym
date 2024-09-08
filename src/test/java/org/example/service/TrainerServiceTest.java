package org.example.service;

import java.util.ArrayList;
import java.util.List;
import org.example.entity.TrainerEntity;
import org.example.repository.TrainerDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {
    @Mock
    private TrainerDao trainerDAO;

    @InjectMocks
    private TrainerService trainerService;

    private TrainerEntity trainerEntity;

    @BeforeEach
    void setUp() {
        trainerEntity = new TrainerEntity();
        trainerEntity.setUserId("trainer1");
        trainerEntity.setSpecialization("Yoga");
    }

    @Test
    void testCreateTrainer() {
        trainerService.createTrainer(trainerEntity);

        verify(trainerDAO, times(1)).createTrainer(trainerEntity);
    }

    @Test
    void testUpdateTrainer() {
        trainerService.updateTrainer("trainer1", trainerEntity);

        verify(trainerDAO, times(1)).updateTrainer(eq("trainer1"), any(TrainerEntity.class));
    }

    @Test
    void testGetTrainer_Found() {
        when(trainerDAO.getTrainer("trainer1")).thenReturn(trainerEntity);

        TrainerEntity result = trainerService.getTrainer("trainer1");

        assertNotNull(result);
        assertEquals("trainer1", result.getUserId());
        assertEquals("Yoga", result.getSpecialization());

        verify(trainerDAO, times(1)).getTrainer("trainer1");
    }

    @Test
    void testGetTrainer_NotFound() {
        when(trainerDAO.getTrainer("trainer1")).thenReturn(null);

        TrainerEntity result = trainerService.getTrainer("trainer1");

        assertNull(result);
        verify(trainerDAO, times(1)).getTrainer("trainer1");
    }

    @Test
    void testGetAllTrainers() {
        List<TrainerEntity> trainers = new ArrayList<>();
        trainers.add(trainerEntity);
        when(trainerDAO.getAllTrainers()).thenReturn(trainers);

        List<TrainerEntity> result = trainerService.getAllTrainers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("trainer1", result.get(0).getUserId());

        verify(trainerDAO, times(1)).getAllTrainers();
    }

    @Test
    void testReflectionForScannerField() throws NoSuchFieldException, IllegalAccessException {
        java.lang.reflect.Field field = trainerService.getClass().getDeclaredField("trainerDAO");
        field.setAccessible(true);

        TrainerDao injectedTrainerDao = (TrainerDao) field.get(trainerService);
        assertNotNull(injectedTrainerDao);
        assertSame(trainerDAO, injectedTrainerDao);
    }
}
