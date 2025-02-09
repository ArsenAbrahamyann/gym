package org.example.gym.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.example.gym.dto.request.TraineeTrainingsRequestDto;
import org.example.gym.dto.request.TrainerTrainingRequestDto;
import org.example.gym.dto.response.GetTrainerTrainingListResponseDto;
import org.example.gym.dto.response.TrainingResponseDto;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.mapper.TrainingMapper;
import org.example.gym.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TrainingControllerTest {

    @Mock
    private TrainingService trainingService;

    @Mock
    private TrainingMapper mapper;

    @InjectMocks
    private TrainingController trainingController;

    /**
     * Set up method to initialize mocks before each test.
     * This method is called before each test method in this class.
     */
    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testGetTraineeTrainingsList() {
        // Arrange
        String traineeName = "JohnDoe";
        LocalDateTime periodFrom = LocalDateTime.now().minusDays(7);
        LocalDateTime periodTo = LocalDateTime.now();
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto(traineeName,
                periodFrom, periodTo, null, null);
        TrainingEntity trainingEntity = new TrainingEntity();
        List<TrainingEntity> trainingEntities = Collections.singletonList(trainingEntity);
        TrainingResponseDto responseDto = new TrainingResponseDto();

        when(trainingService.getTrainingsForTrainee(requestDto)).thenReturn(trainingEntities);
        when(mapper.mapToDtoTrainingTrainee(trainingEntities)).thenReturn(Collections.singletonList(responseDto));

        // Act
        ResponseEntity<List<TrainingResponseDto>> response = trainingController.getTraineeTrainingsList(traineeName,
                periodFrom, periodTo, null, null);

        // Assert
        verify(trainingService).getTrainingsForTrainee(any());
        verify(mapper).mapToDtoTrainingTrainee(trainingEntities);
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void deleteTraining_Successful() {
        Long trainingId = 1L;

        // Set up conditions and responses
        doNothing().when(trainingService).deleteTraining(trainingId);

        // Perform the method call
        ResponseEntity<Void> response = trainingController.deleteTraining(trainingId);

        // Verification
        verify(trainingService, times(1)).deleteTraining(trainingId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deleteTraining_NotFound() {
        Long trainingId = 2L;

        // Setup to throw an exception when the training is not found
        doThrow(new RuntimeException("Training not found")).when(trainingService).deleteTraining(trainingId);

        // Expecting the controller to handle it and convert to a suitable HTTP status
        Exception exception = assertThrows(RuntimeException.class, () -> {
            trainingController.deleteTraining(trainingId);
        });

        // Perform verification
        assertEquals("Training not found", exception.getMessage());
        verify(trainingService, times(1)).deleteTraining(trainingId);
    }

    @Test
    public void testGetTrainerTrainingList() {
        // Arrange
        String trainerName = "JaneDoe";
        LocalDateTime periodFrom = LocalDateTime.parse(LocalDateTime.now().minusDays(7).toString());
        LocalDateTime periodTo = LocalDateTime.parse(LocalDateTime.now().toString());
        TrainerTrainingRequestDto requestDto = new TrainerTrainingRequestDto(trainerName,
                periodFrom, periodTo, null);
        TrainingEntity trainingEntity = new TrainingEntity();
        List<TrainingEntity> trainingEntities = Collections.singletonList(trainingEntity);
        GetTrainerTrainingListResponseDto responseDto = new GetTrainerTrainingListResponseDto();

        when(trainingService.getTrainingsForTrainer(requestDto)).thenReturn(trainingEntities);
        when(mapper.mapToDtoTrainingTrainer(trainingEntities)).thenReturn(Collections.singletonList(responseDto));

        // Act
        ResponseEntity<List<GetTrainerTrainingListResponseDto>> response = trainingController
                .getTrainerTrainingList(trainerName, periodFrom, periodTo, null);

        // Assert
        verify(trainingService).getTrainingsForTrainer(any());
        verify(mapper).mapToDtoTrainingTrainer(trainingEntities);
        assertEquals(1, response.getBody().size());
    }

}
