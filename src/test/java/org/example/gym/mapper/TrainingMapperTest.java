package org.example.gym.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.example.gym.dto.response.GetTrainerTrainingListResponseDto;
import org.example.gym.dto.response.TrainingResponseDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class TrainingMapperTest {

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
        List<TrainingEntity> trainings = Collections.singletonList(mockTrainingEntity);

        List<TrainingResponseDto> result = trainingMapper.mapToDtoTrainingTrainee(trainings);

        assertThat(result).hasSize(1);
        TrainingResponseDto dto = result.get(0);
        assertThat(dto.getName()).isEqualTo("Strength Training");
        assertThat(dto.getTrainerName()).isEqualTo("trainerUser");
        assertThat(dto.getDuration()).isEqualTo(60);
        assertThat(dto.getType()).isEqualTo("Cardio");
    }


    @Test
    public void shouldMapToDtoTrainingTrainer() {
        List<TrainingEntity> trainings = Collections.singletonList(mockTrainingEntity);

        List<GetTrainerTrainingListResponseDto> result = trainingMapper.mapToDtoTrainingTrainer(trainings);

        assertThat(result).hasSize(1);
        GetTrainerTrainingListResponseDto dto = result.get(0);
        assertThat(dto.getTrainingName()).isEqualTo("Strength Training");
        assertThat(dto.getTraineeName()).isEqualTo("traineeUser");
        assertThat(dto.getTrainingDuration()).isEqualTo(60);
    }
}
