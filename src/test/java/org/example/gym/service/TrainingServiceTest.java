package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.example.gym.dto.request.AddTrainingRequestDto;
import org.example.gym.dto.request.TraineeTrainingsRequestDto;
import org.example.gym.dto.request.TrainerTrainingRequestDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.exeption.TrainingNotFoundException;
import org.example.gym.mapper.TrainingMapper;
import org.example.gym.repository.TrainingRepository;
import org.example.gym.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TrainingService trainingService;

    private TrainingEntity trainingEntity;
    private TraineeEntity traineeEntity;
    private TrainerEntity trainerEntity;
    private TrainingTypeEntity trainingTypeEntity;


    /**
     * Sets up the necessary preconditions for each test case in this test class.
     * This method is annotated with {@link BeforeEach} to ensure that it is
     * executed before each test method.
     *
     * <p>The setup includes initializing instances of the following entities:
     * <ul>
     *     <li>{@link TraineeEntity} - Represents a trainee with a predefined username.</li>
     *     <li>{@link TrainerEntity} - Represents a trainer with a predefined ID.</li>
     *     <li>{@link TrainingTypeEntity} - Represents a type of training with a predefined name.</li>
     *     <li>{@link TrainingEntity} - Represents a training session linking the trainee,
     *         trainer, and training type.</li>
     * </ul>
     * This method ensures that each test starts with a consistent and known state
     * of the entities involved, facilitating reliable and repeatable testing.</p>
     */
    @BeforeEach
    public void setUp() {
        traineeEntity = new TraineeEntity();
        trainerEntity = new TrainerEntity();
        trainingTypeEntity = new TrainingTypeEntity();

        traineeEntity.setUsername("traineeUsername");
        trainerEntity.setId(1L);
        trainingTypeEntity.setTrainingTypeName("Yoga");

        trainingEntity = new TrainingEntity();
        trainingEntity.setTrainee(traineeEntity);
        trainingEntity.setTrainer(trainerEntity);
        trainingEntity.setTrainingType(trainingTypeEntity);

    }

    @Test
    public void testAddTraining_Success() {
        AddTrainingRequestDto requestDto = new AddTrainingRequestDto();
        requestDto.setTraineeUsername("traineeUsername");
        requestDto.setTrainerUsername("trainerUsername");

        when(traineeService.getTrainee("traineeUsername")).thenReturn(traineeEntity);
        when(trainerService.getTrainer("trainerUsername")).thenReturn(trainerEntity);
        when(trainingMapper.requestDtoMapToTrainingEntity(any(), any(), any())).thenReturn(trainingEntity);

        trainingService.addTraining(requestDto);

        verify(trainingRepository, times(1)).save(trainingEntity);
    }

    @Test
    public void testGetTrainingsForTrainee_WithValidData() {
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto();
        requestDto.setTraineeName("traineeUsername");
        requestDto.setTrainingType("Yoga");
        requestDto.setPeriodFrom(LocalDateTime.now().minusDays(10));
        requestDto.setPeriodTo(LocalDateTime.now());

        when(trainingRepository.findTrainingsForTrainee(any(), any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(trainingEntity));

        List<TrainingEntity> trainings = trainingService.getTrainingsForTrainee(requestDto);

        assertEquals(1, trainings.size(), "Training count should be 1");
        assertEquals("traineeUsername", trainings.get(0).getTrainee().getUsername());
    }

    @Test
    public void testGetTrainingsForTrainee_NoTrainingsFound() {
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto();
        requestDto.setTraineeName("traineeUsername");
        requestDto.setTrainingType("Yoga");
        requestDto.setPeriodFrom(LocalDateTime.now().minusDays(10));
        requestDto.setPeriodTo(LocalDateTime.now());

        when(trainingRepository.findTrainingsForTrainee(any(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        TrainingNotFoundException thrown = assertThrows(TrainingNotFoundException.class, () ->
                trainingService.getTrainingsForTrainee(requestDto));

        assertEquals("No trainings found for the specified criteria.", thrown.getMessage());
    }

    @Test
    public void testGetTrainingsForTrainer_WithValidData() {
        TrainerTrainingRequestDto requestDto = new TrainerTrainingRequestDto();
        requestDto.setTrainerUsername("trainerUsername");
        requestDto.setPeriodFrom(LocalDateTime.now().minusDays(10));
        requestDto.setPeriodTo(LocalDateTime.now());

        when(trainerService.getTrainer("trainerUsername")).thenReturn(trainerEntity);
        when(trainingRepository.findTrainingsForTrainer(any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(trainingEntity));

        List<TrainingEntity> trainings = trainingService.getTrainingsForTrainer(requestDto);

        assertEquals(1, trainings.size(), "Training count should be 1");
        assertEquals(trainerEntity.getId(), trainings.get(0).getTrainer().getId());
    }

    @Test
    public void testGetTrainingsForTrainer_NoTrainingsFound() {
        TrainerTrainingRequestDto requestDto = new TrainerTrainingRequestDto();
        requestDto.setTrainerUsername("trainerUsername");
        requestDto.setPeriodFrom(LocalDateTime.now().minusDays(10));
        requestDto.setPeriodTo(LocalDateTime.now());

        when(trainerService.getTrainer("trainerUsername")).thenReturn(trainerEntity);
        when(trainingRepository.findTrainingsForTrainer(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        TrainingNotFoundException thrown = assertThrows(TrainingNotFoundException.class, () ->
                trainingService.getTrainingsForTrainer(requestDto));

        assertEquals("No trainings found for the specified criteria.", thrown.getMessage());
    }
}