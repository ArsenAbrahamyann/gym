package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.example.gym.dto.request.TraineeTrainingsRequestDto;
import org.example.gym.dto.request.TrainerTrainingRequestDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.exeption.TrainingNotFoundException;
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

    @InjectMocks
    private TrainingService trainingService;

    private TrainingEntity trainingEntity;
    private TraineeEntity traineeEntity;
    private TrainerEntity trainerEntity;
    private TrainingTypeEntity trainingTypeEntity;

    /**
     * Sets up the test environment by initializing test data and mocking responses.
     */
    @BeforeEach
    public void setUp() {
        trainingEntity = new TrainingEntity();
        traineeEntity = new TraineeEntity();
        trainerEntity = new TrainerEntity();
        trainingTypeEntity = new TrainingTypeEntity();

        traineeEntity.setUsername("traineeUsername");
        trainerEntity.setId(1L);
        trainingTypeEntity.setTrainingTypeName("Yoga");

        trainingEntity.setTrainee(traineeEntity);
        trainingEntity.setTrainer(trainerEntity);
        trainingEntity.setTrainingType(trainingTypeEntity);
    }


    @Test
    public void testAddTraining() {
        trainingService.addTraining(trainingEntity);
        verify(trainingRepository, times(1)).save(trainingEntity);
    }


    @Test
    public void testGetTrainingsForTrainee_ValidData() {
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto();
        requestDto.setTraineeName("traineeUsername");
        requestDto.setTrainingType("Yoga");
        requestDto.setPeriodFrom(LocalDateTime.now().minusDays(10));
        requestDto.setPeriodTo(LocalDateTime.now());


        // Get the expected trainee ID from the traineeEntity mock.
        Long traineeId = traineeEntity.getId(); // Assuming getId() returns the ID.

        when(trainingRepository.findTrainingsForTrainee(any(), any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(trainingEntity));

        List<TrainingEntity> trainings = trainingService.getTrainingsForTrainee(requestDto);
        assertEquals(1, trainings.size());
        assertEquals("traineeUsername", trainings.get(0).getTrainee().getUsername());
    }


    @Test
    public void testGetTrainingsForTrainee_NoTrainingsFound() {
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto();
        requestDto.setTraineeName("traineeUsername");
        requestDto.setTrainingType("Yoga");
        requestDto.setPeriodFrom(LocalDateTime.now().minusDays(10));
        requestDto.setPeriodTo(LocalDateTime.now());


        // Get the expected trainee ID from the traineeEntity mock.
        Long traineeId = traineeEntity.getId(); // Assuming getId() returns the ID.

        when(trainingRepository.findTrainingsForTrainee(any(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        TrainingNotFoundException thrown = assertThrows(TrainingNotFoundException.class, () -> {
            trainingService.getTrainingsForTrainee(requestDto);
        });

        assertEquals("No trainings found for the specified criteria.", thrown.getMessage());
    }


    @Test
    public void testGetTrainingsForTrainer_ValidData() {
        TrainerTrainingRequestDto requestDto = new TrainerTrainingRequestDto();
        requestDto.setTrainerUsername("trainerUsername");
        requestDto.setPeriodFrom(LocalDateTime.now().minusDays(10));
        requestDto.setPeriodTo(LocalDateTime.now());

        when(trainerService.getTrainer("trainerUsername")).thenReturn(trainerEntity);
        when(trainingRepository.findTrainingsForTrainer(anyLong(), any(), any(), any()))
                .thenReturn(Collections.singletonList(trainingEntity));

        List<TrainingEntity> trainings = trainingService.getTrainingsForTrainer(requestDto);
        assertEquals(1, trainings.size());
        assertEquals(trainerEntity.getId(), trainings.get(0).getTrainer().getId());
    }

    @Test
    public void testGetTrainingsForTrainer_NoTrainingsFound() {
        TrainerTrainingRequestDto requestDto = new TrainerTrainingRequestDto();
        requestDto.setTrainerUsername("trainerUsername");
        requestDto.setPeriodFrom(LocalDateTime.now().minusDays(10));
        requestDto.setPeriodTo(LocalDateTime.now());

        when(trainerService.getTrainer("trainerUsername")).thenReturn(trainerEntity);
        when(trainingRepository.findTrainingsForTrainer(anyLong(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        TrainingNotFoundException thrown = assertThrows(TrainingNotFoundException.class, () -> {
            trainingService.getTrainingsForTrainer(requestDto);
        });

        assertEquals("No trainings found for the specified criteria.", thrown.getMessage());
    }


}
