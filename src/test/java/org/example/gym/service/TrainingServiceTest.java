package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.example.gym.dto.request.AddTrainingRequestDto;
import org.example.gym.dto.request.TraineeTrainingsRequestDto;
import org.example.gym.dto.request.TrainerTrainingRequestDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.entity.UserEntity;
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
    @Mock private TrainingRepository trainingRepository;
    @Mock private TraineeService traineeService;
    @Mock private TrainerService trainerService;
    @Mock private TrainingMapper trainingMapper;
    @Mock private ValidationUtils validationUtils;
    @Mock private JmsProducerService jmsProducerService;
    @InjectMocks private TrainingService trainingService;

    private TrainingEntity trainingEntity;
    private TraineeEntity traineeEntity;
    private TrainerEntity trainerEntity;
    private UserEntity trainerUser;

    /**
     * Initializes entities for the test environment before each test case.
     * This setup creates and configures multiple related entity instances
     * including trainers, trainees, and training types.
     */
    @BeforeEach
    public void setUp() {
        UserEntity traineeUser = new UserEntity();
        traineeUser.setUsername("traineeUsername");
        traineeUser.setIsActive(true);

        traineeEntity = new TraineeEntity();
        traineeEntity.setUser(traineeUser);

        trainerUser = new UserEntity();
        trainerUser.setUsername("trainerUsername");
        trainerUser.setIsActive(true);

        trainerEntity = new TrainerEntity();
        trainerEntity.setId(1L);
        trainerEntity.setUser(trainerUser);

        TrainingTypeEntity trainingType = new TrainingTypeEntity();
        trainingType.setTrainingTypeName("Yoga");

        trainingEntity = new TrainingEntity();
        trainingEntity.setTrainee(traineeEntity);
        trainingEntity.setTrainer(trainerEntity);
        trainingEntity.setTrainingType(trainingType);
    }

    @Test
    public void testAddTraining_Success() {
        AddTrainingRequestDto requestDto = new AddTrainingRequestDto();
        requestDto.setTraineeUsername("traineeUsername");
        requestDto.setTrainerUsername("trainerUsername");
        String token = "token";

        when(traineeService.getTrainee(any())).thenReturn(traineeEntity);
        when(trainerService.getTrainer(any())).thenReturn(trainerEntity);
        when(trainingMapper.requestDtoMapToTrainingEntity(any(), any(), any())).thenReturn(trainingEntity);

        trainingService.addTraining(requestDto);

        verify(trainingRepository, times(1)).save(trainingEntity);
        verify(jmsProducerService, times(1)).sendTrainingUpdate(any());
    }

    @Test
    public void testDeleteTraining_Success() {
        String token = "token";
        Long trainingId = 1L;

        when(trainingRepository.findById(trainingId)).thenReturn(java.util.Optional.of(trainingEntity));

        trainingService.deleteTraining(trainingId);

        verify(trainingRepository, times(1)).delete(trainingEntity);
        verify(jmsProducerService, times(1)).sendTrainingUpdate(any());
    }

    @Test
    public void testDeleteTraining_NotFound() {
        when(trainingRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(TrainingNotFoundException.class, () -> trainingService.deleteTraining(1L));
    }

    @Test
    public void testGetTrainingsForTrainee_Success() {
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto();
        requestDto.setTraineeName("traineeUsername");

        when(trainingRepository.findTrainingsForTrainee(any(), any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(trainingEntity));

        List<TrainingEntity> result = trainingService.getTrainingsForTrainee(requestDto);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetTrainingsForTrainee_NotFound() {
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto();
        requestDto.setTraineeName("traineeUsername");

        when(trainingRepository.findTrainingsForTrainee(any(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        assertThrows(TrainingNotFoundException.class, () -> trainingService.getTrainingsForTrainee(requestDto));
    }

    @Test
    public void testGetTrainingsForTrainer_Success() {
        TrainerTrainingRequestDto requestDto = new TrainerTrainingRequestDto();
        requestDto.setTrainerUsername("trainerUsername");

        when(trainerService.getTrainer(any())).thenReturn(trainerEntity);
        when(trainingRepository.findTrainingsForTrainer(any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(trainingEntity));

        List<TrainingEntity> result = trainingService.getTrainingsForTrainer(requestDto);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetTrainingsForTrainer_NotFound() {
        TrainerTrainingRequestDto requestDto = new TrainerTrainingRequestDto();
        requestDto.setTrainerUsername("trainerUsername");

        when(trainerService.getTrainer(any())).thenReturn(trainerEntity);
        when(trainingRepository.findTrainingsForTrainer(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        assertThrows(TrainingNotFoundException.class, () -> trainingService.getTrainingsForTrainer(requestDto));
    }
}