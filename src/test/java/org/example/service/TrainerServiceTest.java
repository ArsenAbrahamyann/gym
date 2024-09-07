package org.example.service;
import org.example.entity.TrainerEntity;
import org.example.repository.TrainerDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {
    @Mock
    private TrainerDAO trainerDAO;  // Mocked dependency

    @InjectMocks
    private TrainerService trainerService;  // Inject mocks into this service

    private TrainerEntity trainerEntity;

    @BeforeEach
    void setUp() {
        // Set up a sample TrainerEntity object for testing
        trainerEntity = new TrainerEntity();
        trainerEntity.setUserId("trainer1");
        trainerEntity.setSpecialization("Yoga");
    }

    @Test
    void testCreateTrainer() {
        // Call the method under test
        trainerService.createTrainer(trainerEntity);

        // Verify if trainerDAO's createTrainer() was called with the correct argument
        verify(trainerDAO, times(1)).createTrainer(trainerEntity);
    }

    @Test
    void testUpdateTrainer() {
        // Call the method under test
        trainerService.updateTrainer("trainer1", trainerEntity);

        // Verify if trainerDAO's updateTrainer() was called with the correct arguments
        verify(trainerDAO, times(1)).updateTrainer(eq("trainer1"), any(TrainerEntity.class));
    }

    @Test
    void testGetTrainer_Found() {
        // Mock the behavior of trainerDAO
        when(trainerDAO.getTrainer("trainer1")).thenReturn(trainerEntity);

        // Call the method under test
        TrainerEntity result = trainerService.getTrainer("trainer1");

        // Verify the result and mock interaction
        assertNotNull(result);
        assertEquals("trainer1", result.getUserId());
        assertEquals("Yoga", result.getSpecialization());

        verify(trainerDAO, times(1)).getTrainer("trainer1");
    }

    @Test
    void testGetTrainer_NotFound() {
        // Mock the behavior of trainerDAO to return null
        when(trainerDAO.getTrainer("trainer1")).thenReturn(null);

        // Call the method under test
        TrainerEntity result = trainerService.getTrainer("trainer1");

        // Verify that no trainer is returned and interaction is correct
        assertNull(result);
        verify(trainerDAO, times(1)).getTrainer("trainer1");
    }

    @Test
    void testGetAllTrainers() {
        // Create a list of trainers to return
        List<TrainerEntity> trainers = new ArrayList<>();
        trainers.add(trainerEntity);
        when(trainerDAO.getAllTrainers()).thenReturn(trainers);

        // Call the method under test
        List<TrainerEntity> result = trainerService.getAllTrainers();

        // Verify the result and interaction with the mock
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("trainer1", result.get(0).getUserId());

        verify(trainerDAO, times(1)).getAllTrainers();
    }

    @Test
    void testReflectionForScannerField() throws NoSuchFieldException, IllegalAccessException {
        // Using reflection to access private final field trainerDAO in TrainerService class
        java.lang.reflect.Field field = trainerService.getClass().getDeclaredField("trainerDAO");
        field.setAccessible(true);

        // Check if the injected mock is correctly set
        TrainerDAO injectedTrainerDAO = (TrainerDAO) field.get(trainerService);
        assertNotNull(injectedTrainerDAO);
        assertSame(trainerDAO, injectedTrainerDAO);
    }
}
