package org.example.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.example.dto.TrainingDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.entity.UserEntity;
import org.example.repository.TrainingRepository;
import org.example.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
    @InjectMocks
    private TrainingService trainingService;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private ValidationUtils validationUtils;

    private TraineeEntity trainee;
    private TrainerEntity trainer;
    private TrainingTypeEntity trainingType;

    @BeforeEach
    void setUp() {
        trainee = new TraineeEntity();
        trainee.setId(1L);
        trainee.setUser(new UserEntity(1L, "firstName", "lastName",
                "traineeUser", "password", true));

        trainer = new TrainerEntity();
        trainer.setId(1L);
        trainer.setUser(new UserEntity(1L, "firstName", "lastName",
                "trainerUser", "password", true));

        trainingType = new TrainingTypeEntity();
        trainingType.setId(1L);
    }

    @Test
    void testAddTraining_Success() {
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setTraineeId(1L);
        trainingDto.setTrainerId(1L);
        trainingDto.setTrainingTypeId(1L);
        trainingDto.setTrainingName("Yoga");
        trainingDto.setTrainingDuration(60);

        when(traineeService.findById(1L)).thenReturn(Optional.of(trainee));
        when(trainerService.findById(1L)).thenReturn(Optional.of(trainer));
        when(trainingTypeService.findById(1L)).thenReturn(Optional.of(trainingType));

        trainingService.addTraining(trainingDto);

        verify(trainingRepository, times(1)).save(any(TrainingEntity.class));
    }

    @Test
    void testGetTrainingsForTrainee_Success() {
        String traineeName = "traineeUser";
        LocalDateTime fromDate = LocalDateTime.now().minusDays(7);
        LocalDateTime toDate = LocalDateTime.now();
        String trainerName = null;
        String trainingType = null;

        when(traineeService.getTrainee(traineeName)).thenReturn(Optional.of(trainee));
        when(trainingRepository.findTrainingsForTrainee(1L, fromDate, toDate, trainerName, trainingType))
                .thenReturn(Collections.singletonList(new TrainingEntity()));

        List<TrainingEntity> result = trainingService.getTrainingsForTrainee(traineeName, fromDate, toDate,
                trainerName, trainingType);

        assertThat(result).isNotEmpty();
        verify(trainingRepository, times(1)).findTrainingsForTrainee(1L, fromDate,
                toDate, trainerName, trainingType);
    }

    @Test
    void testGetTrainingsForTrainer_Success() {
        String trainerUsername = "trainerUser";
        LocalDateTime fromDate = LocalDateTime.now().minusDays(7);
        LocalDateTime toDate = LocalDateTime.now();
        String traineeName = null;

        when(trainerService.findByTrainerFromUsername(trainerUsername)).thenReturn(Optional.of(trainer));
        when(trainingRepository.findTrainingsForTrainer(1L, fromDate, toDate, traineeName))
                .thenReturn(Collections.singletonList(new TrainingEntity()));

        List<TrainingEntity> result = trainingService.getTrainingsForTrainer(trainerUsername,
                fromDate, toDate, traineeName);

        assertThat(result).isNotEmpty();
        verify(trainingRepository, times(1)).findTrainingsForTrainer(1L, fromDate,
                toDate, traineeName);
    }

    @Test
    void testFindTrainingsForTrainer_Success() {
        Long trainerId = 1L;
        LocalDateTime fromDate = LocalDateTime.now().minusDays(7);
        LocalDateTime toDate = LocalDateTime.now();
        String traineeName = null;

        when(trainingRepository.findTrainingsForTrainer(trainerId, fromDate, toDate, traineeName))
                .thenReturn(Collections.singletonList(new TrainingEntity()));

        List<TrainingEntity> result = trainingService.findTrainingsForTrainer(trainerId, fromDate,
                toDate, traineeName);

        assertThat(result).isNotEmpty();
        verify(trainingRepository, times(1)).findTrainingsForTrainer(trainerId, fromDate,
                toDate, traineeName);
    }

    @Test
    void testFindTrainingsForTrainer_NoTrainingsFound() {
        Long trainerId = 1L;
        LocalDateTime fromDate = LocalDateTime.now().minusDays(7);
        LocalDateTime toDate = LocalDateTime.now();
        String traineeName = null;

        when(trainingRepository.findTrainingsForTrainer(trainerId, fromDate, toDate, traineeName))
                .thenReturn(Collections.EMPTY_LIST);

        List<TrainingEntity> result = trainingService.findTrainingsForTrainer(trainerId, fromDate,
                toDate, traineeName);

        assertThat(result).isEmpty();
        verify(trainingRepository, times(1)).findTrainingsForTrainer(trainerId, fromDate,
                toDate, traineeName);
    }
}
