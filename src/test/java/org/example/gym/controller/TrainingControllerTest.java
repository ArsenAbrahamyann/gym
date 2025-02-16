package org.example.gym.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.example.gym.dto.request.AddTrainingRequestDto;
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
import org.springframework.http.HttpHeaders;
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

    @Test
    public void testAddTraining() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer token123");
        AddTrainingRequestDto requestDto = new AddTrainingRequestDto();
        requestDto.setTraineeUsername("JohnD");
        requestDto.setTrainerUsername("JaneD");

        doNothing().when(trainingService).addTraining(any(AddTrainingRequestDto.class));

        // Act
        ResponseEntity<Void> response = trainingController.addTraining(requestDto);

        // Assert
        verify(trainingService).addTraining(requestDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testDeleteTraining() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer token123");
        Long trainingId = 1L;


        // Act
        ResponseEntity<Void> response = trainingController.deleteTraining(
                //headers,
                trainingId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
