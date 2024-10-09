package org.example.gym.mapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.example.gym.dto.request.AddTrainingRequestDto;
import org.example.gym.dto.response.GetTrainerTrainingListResponseDto;
import org.example.gym.dto.response.TrainingResponseDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.service.TraineeService;
import org.example.gym.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TrainingMapperTest {

    @Mock
    private TrainerService trainerService;

    @Mock
    private TraineeService traineeService;

    @InjectMocks
    private TrainingMapper trainingMapper;

    private TrainingEntity mockTrainingEntity;
    private TrainerEntity mockTrainerEntity;
    private TraineeEntity mockTraineeEntity;
    private TrainingTypeEntity mockTrainingType;

    /**
     * Sets up the test data before each test method.
     */
    @BeforeEach
    public void setUp() {
        mockTrainerEntity = new TrainerEntity();
        mockTrainerEntity.setUsername("trainerUser");

        mockTraineeEntity = new TraineeEntity();
        mockTraineeEntity.setUsername("traineeUser");

        mockTrainingType = new TrainingTypeEntity();
        mockTrainingType.setTrainingTypeName("Cardio");

        mockTrainingEntity = new TrainingEntity();
        mockTrainingEntity.setTrainingName("Strength Training");
        mockTrainingEntity.setTrainingDate(LocalDateTime.now());
        mockTrainingEntity.setTrainingDuration(60);
        mockTrainingEntity.setTrainer(mockTrainerEntity);
        mockTrainingEntity.setTrainee(mockTraineeEntity);
        mockTrainingEntity.setTrainingType(mockTrainingType); // Initialize training type
    }


    @Test
    public void shouldMapToDtoTrainingTrainee() {
        // Arrange
        List<TrainingEntity> trainings = Collections.singletonList(mockTrainingEntity);

        // Act
        List<TrainingResponseDto> result = trainingMapper.mapToDtoTrainingTrainee(trainings);

        // Assert
        assertThat(result).hasSize(1);
        TrainingResponseDto dto = result.get(0);
        assertThat(dto.getName()).isEqualTo("Strength Training");
        assertThat(dto.getTrainerName()).isEqualTo("trainerUser");
        assertThat(dto.getDuration()).isEqualTo(60);
        assertThat(dto.getType()).isEqualTo("Cardio");
    }


    @Test
    public void shouldMapToDtoTrainingTrainer() {
        // Arrange
        List<TrainingEntity> trainings = Collections.singletonList(mockTrainingEntity);

        // Act
        List<GetTrainerTrainingListResponseDto> result = trainingMapper.mapToDtoTrainingTrainer(trainings);

        // Assert
        assertThat(result).hasSize(1);
        GetTrainerTrainingListResponseDto dto = result.get(0);
        assertThat(dto.getTrainingName()).isEqualTo("Strength Training");
        assertThat(dto.getTraineeName()).isEqualTo("traineeUser");
        assertThat(dto.getTrainingDuration()).isEqualTo(60);
    }


    @Test
    public void shouldMapRequestDtoToTrainingEntity() {
        // Arrange
        AddTrainingRequestDto requestDto = new AddTrainingRequestDto();
        requestDto.setTrainingName("Strength Training");
        requestDto.setTrainingDate(LocalDateTime.now());
        requestDto.setTrainingDuration(60);
        requestDto.setTraineeUsername("traineeUser");
        requestDto.setTrainerUsername("trainerUser");

        when(traineeService.getTrainee("traineeUser")).thenReturn(mockTraineeEntity);
        when(trainerService.getTrainer("trainerUser")).thenReturn(mockTrainerEntity);

        // Act
        TrainingEntity result = trainingMapper.requestDtoMapToTrainingEntity(requestDto);

        // Assert
        assertThat(result.getTrainingName()).isEqualTo("Strength Training");
        assertThat(result.getTrainingDuration()).isEqualTo(60);
        assertThat(result.getTrainee().getUsername()).isEqualTo("traineeUser");
        assertThat(result.getTrainer().getUsername()).isEqualTo("trainerUser");
    }
}
