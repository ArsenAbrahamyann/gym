package org.example.service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import org.example.entity.TrainingEntity;
import org.example.repository.TrainingDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
    @Mock
    private TrainingDao trainingDAO;

    @InjectMocks
    private TrainingService trainingService;

    private TrainingEntity mockTraining;

    @BeforeEach
    void setUp() {
        mockTraining = new TrainingEntity();
        mockTraining.setTrainingName("Java Basics");
        mockTraining.setTrainerId("1");
        mockTraining.setTraineeId("2");
    }

    @Test
    void testCreateTraining() throws NoSuchFieldException, IllegalAccessException {
        trainingService.createTraining(mockTraining);

        verify(trainingDAO, times(1)).createTraining(mockTraining);

        Field daoField = TrainingService.class.getDeclaredField("trainingDao");
        daoField.setAccessible(true);
        TrainingDao injectedDAO = (TrainingDao) daoField.get(trainingService);

        assertNotNull(injectedDAO);
        assertEquals(trainingDAO, injectedDAO);
    }

    @Test
    void testGetTraining_TrainingExists() {
        when(trainingDAO.getTraining("Java Basics")).thenReturn(mockTraining);

        TrainingEntity result = trainingService.getTraining("Java Basics");

        assertNotNull(result);
        assertEquals("Java Basics", result.getTrainingName());
        verify(trainingDAO, times(1)).getTraining("Java Basics");
    }

    @Test
    void testGetTraining_TrainingDoesNotExist() {
        when(trainingDAO.getTraining("Non-Existent Training")).thenReturn(null);

        TrainingEntity result = trainingService.getTraining("Non-Existent Training");

        assertNull(result);
        verify(trainingDAO, times(1)).getTraining("Non-Existent Training");
    }

    @Test
    void testGetAllTrainings() {
        List<TrainingEntity> mockTrainings = Arrays.asList(
                mockTraining,
                new TrainingEntity() {{
                    setTrainingName("Advanced Java");
                }}
        );

        when(trainingDAO.getAllTrainings()).thenReturn(mockTrainings);

        List<TrainingEntity> result = trainingService.getAllTrainings();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Java Basics", result.get(0).getTrainingName());
        assertEquals("Advanced Java", result.get(1).getTrainingName());
        verify(trainingDAO, times(1)).getAllTrainings();
    }

    @Test
    void testGetAllTrainings_EmptyList() {
        when(trainingDAO.getAllTrainings()).thenReturn(Arrays.asList());

        List<TrainingEntity> result = trainingService.getAllTrainings();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(trainingDAO, times(1)).getAllTrainings();
    }
}
