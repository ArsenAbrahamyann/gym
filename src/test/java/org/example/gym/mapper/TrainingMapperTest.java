package org.example.gym.mapper;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.example.gym.entity.UserEntity;
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
    private UserEntity mockUserEntity;

    /**
     * Sets up the mock entities used for testing.
     * This method is executed before each test case to initialize mock data
     * for `TrainerEntity`, `TraineeEntity`, `TrainingTypeEntity`, and `TrainingEntity`
     * objects, ensuring consistent and isolated test conditions.
     * The mock entities are initialized as follows:
     * - `mockTrainerEntity`: A `TrainerEntity` with username "trainerUser".
     * - `mockTraineeEntity`: A `TraineeEntity` with username "traineeUser".
     * - `mockTrainingType`: A `TrainingTypeEntity` with training type name "Cardio".
     * - `mockTrainingEntity`: A `TrainingEntity` representing a "Strength Training" session with
     *   the current date and time as the training date, a duration of 60 minutes,
     *   and associations with `mockTrainerEntity`, `mockTraineeEntity`, and `mockTrainingType`.
     */
    @BeforeEach
    public void setUp() {
        mockTrainerEntity = new TrainerEntity();
        mockUserEntity = new UserEntity();
        mockUserEntity.setUsername("trainerUser");
        mockTrainerEntity.setUser(mockUserEntity);

        mockTraineeEntity = new TraineeEntity();
        mockUserEntity.setUsername("traineeUser");
        mockTraineeEntity.setUser(mockUserEntity);

        mockTrainingType = new TrainingTypeEntity();
        mockTrainingType.setTrainingTypeName("Cardio");

        mockTrainingEntity = new TrainingEntity();
        mockTrainingEntity.setTrainingName("Strength Training");
        mockTrainingEntity.setTrainingDate(LocalDateTime.now());
        mockTrainingEntity.setTrainingDuration(60);
        mockTrainingEntity.setTrainer(mockTrainerEntity);
        mockTrainingEntity.setTrainee(mockTraineeEntity);
        mockTrainingEntity.setTrainingType(mockTrainingType);
    }

    @Test
    public void shouldMapToDtoTrainingTrainee() {
        List<TrainingEntity> trainings = Collections.singletonList(mockTrainingEntity);

        List<TrainingResponseDto> result = trainingMapper.mapToDtoTrainingTrainee(trainings);

        assertThat(result).hasSize(1);
        TrainingResponseDto dto = result.get(0);
        assertThat(dto.getName()).isEqualTo("Strength Training");
        assertThat(dto.getTrainerName()).isEqualTo("traineeUser");
        assertThat(dto.getDuration()).isEqualTo(60);
        assertThat(dto.getType()).isEqualTo("Cardio");
    }

    @Test
    public void shouldMapToDtoTrainingTrainee_WhenEmptyList() {
        List<TrainingEntity> trainings = Collections.emptyList();

        List<TrainingResponseDto> result = trainingMapper.mapToDtoTrainingTrainee(trainings);

        assertThat(result).isEmpty();
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

    @Test
    public void shouldMapToDtoTrainingTrainer_WhenEmptyList() {
        List<TrainingEntity> trainings = Collections.emptyList();

        List<GetTrainerTrainingListResponseDto> result = trainingMapper.mapToDtoTrainingTrainer(trainings);

        assertThat(result).isEmpty();
    }

    @Test
    public void shouldMapRequestDtoToTrainingEntity() {
        AddTrainingRequestDto requestDto = new AddTrainingRequestDto();
        requestDto.setTrainingName("Yoga");
        requestDto.setTrainingDuration(45);
        requestDto.setTrainingDate(LocalDateTime.now());

        TrainingEntity result = trainingMapper.requestDtoMapToTrainingEntity(requestDto,
                mockTraineeEntity, mockTrainerEntity);

        assertThat(result).isNotNull();
        assertThat(result.getTrainingName()).isEqualTo("Yoga");
        assertThat(result.getTrainingDuration()).isEqualTo(45);
        assertThat(result.getTrainer()).isEqualTo(mockTrainerEntity);
        assertThat(result.getTrainee()).isEqualTo(mockTraineeEntity);
    }

    @Test
    public void shouldHandleNullValues() {
        TrainingEntity entity = new TrainingEntity();
        entity.setTrainingName(null);
        entity.setTrainingDuration(null);
        TrainerEntity trainer = new TrainerEntity();
        mockUserEntity.setUsername("Doe");
        trainer.setUser(mockUserEntity);
        entity.setTrainer(trainer);
        entity.setTrainee(null);
        entity.setTrainingDate(LocalDateTime.parse("2000-01-01T00:00:00"));
        TrainingTypeEntity trainingType = new TrainingTypeEntity();
        trainingType.setTrainingTypeName("Doe");
        entity.setTrainingType(trainingType);

        List<TrainingResponseDto> result = trainingMapper.mapToDtoTrainingTrainee(Collections.singletonList(entity));

        assertThat(result).hasSize(1);
        TrainingResponseDto dto = result.get(0);
        assertThat(dto.getName()).isNull();
        assertThat(dto.getDuration()).isNull();
        assertThat(dto.getTrainerName()).isEqualTo("Doe");
    }
}