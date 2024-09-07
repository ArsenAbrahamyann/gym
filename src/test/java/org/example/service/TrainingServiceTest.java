package org.example.service;
import org.example.entity.TrainingEntity;
import org.example.repository.TrainingDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
    @Mock
    private TrainingDAO trainingDAO;  // Mocked DAO

    @InjectMocks
    private TrainingService trainingService;  // Service with the mock injected

    private TrainingEntity mockTraining;

    @BeforeEach
    void setUp() {
        // Initialize a mock TrainingEntity
        mockTraining = new TrainingEntity();
        mockTraining.setTrainingName("Java Basics");
        mockTraining.setTrainerId("1");
        mockTraining.setTraineeId("2");
        // ...set other necessary fields
    }

    @Test
    void testCreateTraining() throws NoSuchFieldException, IllegalAccessException {
        // Act: Call the method being tested
        trainingService.createTraining(mockTraining);

        // Assert: Verify that the DAO method was called
        verify(trainingDAO, times(1)).createTraining(mockTraining);

        // Using reflection to verify that the TrainingDAO instance in the service is properly injected
        Field daoField = TrainingService.class.getDeclaredField("trainingDao");
        daoField.setAccessible(true);
        TrainingDAO injectedDAO = (TrainingDAO) daoField.get(trainingService);

        assertNotNull(injectedDAO);  // Ensure that the DAO is injected
        assertEquals(trainingDAO, injectedDAO);  // The injected DAO should be the same as the mock
    }

    @Test
    void testGetTraining_TrainingExists() {
        // Arrange: Define behavior for the mock DAO
        when(trainingDAO.getTraining("Java Basics")).thenReturn(mockTraining);

        // Act: Call the method being tested
        TrainingEntity result = trainingService.getTraining("Java Basics");

        // Assert: Check the returned result and verify the mock interaction
        assertNotNull(result);
        assertEquals("Java Basics", result.getTrainingName());
        verify(trainingDAO, times(1)).getTraining("Java Basics");
    }

    @Test
    void testGetTraining_TrainingDoesNotExist() {
        // Arrange: Define behavior for the mock DAO (null result)
        when(trainingDAO.getTraining("Non-Existent Training")).thenReturn(null);

        // Act: Call the method being tested
        TrainingEntity result = trainingService.getTraining("Non-Existent Training");

        // Assert: Check that null is returned when the entity is not found
        assertNull(result);
        verify(trainingDAO, times(1)).getTraining("Non-Existent Training");
    }

    @Test
    void testGetAllTrainings() {
        // Arrange: Prepare a list of mock trainings
        List<TrainingEntity> mockTrainings = Arrays.asList(
                mockTraining,
                new TrainingEntity() {{ setTrainingName("Advanced Java"); }}
        );

        // Define behavior for the mock DAO
        when(trainingDAO.getAllTrainings()).thenReturn(mockTrainings);

        // Act: Call the method being tested
        List<TrainingEntity> result = trainingService.getAllTrainings();

        // Assert: Check the returned list and verify the mock interaction
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Java Basics", result.get(0).getTrainingName());
        assertEquals("Advanced Java", result.get(1).getTrainingName());
        verify(trainingDAO, times(1)).getAllTrainings();
    }

    @Test
    void testGetAllTrainings_EmptyList() {
        // Arrange: Define behavior for the mock DAO (empty list)
        when(trainingDAO.getAllTrainings()).thenReturn(Arrays.asList());

        // Act: Call the method being tested
        List<TrainingEntity> result = trainingService.getAllTrainings();

        // Assert: Check that the list is empty
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(trainingDAO, times(1)).getAllTrainings();
    }
}
