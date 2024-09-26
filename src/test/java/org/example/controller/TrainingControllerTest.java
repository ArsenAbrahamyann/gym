package org.example.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.example.dto.TrainingDto;
import org.example.entity.TrainingEntity;
import org.example.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingControllerTest {

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingController trainingController;

    private TrainingDto trainingDto;
    private List<TrainingEntity> trainingEntities;

    /**
     * Initializes the necessary objects before each test case is run.
     * This method is annotated with {@code @BeforeEach}, which ensures that
     * it runs before each individual test in the class. The method sets up
     * the mock data used in the test cases, including a new {@link TrainingDto}
     * object and an empty {@link ArrayList} of {@link TrainingEntity}.
     * This setup provides a clean state for each test case, ensuring no shared
     * state between tests.
     */
    @BeforeEach
    void setUp() {
        trainingDto = new TrainingDto();
        trainingEntities = new ArrayList<>();
    }

    @Test
    public void addTraining_ShouldInvokeTrainingService() {
        trainingController.addTraining(trainingDto);

        verify(trainingService).addTraining(trainingDto);
    }

    @Test
    public void getTrainingsForTrainee_ShouldReturnTrainings() {
        String traineeName = "John Doe";
        LocalDateTime fromDate = LocalDateTime.now().minusDays(7);
        LocalDateTime toDate = LocalDateTime.now();
        String trainerName = "Jane Doe";
        String trainingType = "Strength";

        when(trainingService.getTrainingsForTrainee(traineeName, fromDate, toDate, trainerName, trainingType))
                .thenReturn(trainingEntities);

        List<TrainingEntity> result = trainingController.getTrainingsForTrainee(traineeName, fromDate,
                toDate, trainerName, trainingType);

        verify(trainingService).getTrainingsForTrainee(traineeName, fromDate, toDate, trainerName, trainingType);
    }

    @Test
    public void getTrainingsForTrainer_ShouldReturnTrainings() {
        String trainerUsername = "JaneDoe123";
        LocalDateTime fromDate = LocalDateTime.now().minusDays(7);
        LocalDateTime toDate = LocalDateTime.now();
        String traineeName = "John Doe";

        when(trainingService.getTrainingsForTrainer(trainerUsername, fromDate, toDate, traineeName))
                .thenReturn(trainingEntities);

        List<TrainingEntity> result = trainingController.getTrainingsForTrainer(trainerUsername, fromDate,
                toDate, traineeName);

        verify(trainingService).getTrainingsForTrainer(trainerUsername, fromDate, toDate, traineeName);
    }

}
