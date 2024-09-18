package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.TraineeRepository;
import org.example.repository.TrainerRepository;
import org.example.repository.TrainingRepository;
import org.example.repository.TrainingTypeRepository;
import org.example.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
    private static final Long TRAINEE_ID = 59L;
    private static final Long TRAINER_ID = 27L;
    private static final Long TRAINING_TYPE_ID = 15L;
    private static final String TRAINING_NAME = "Yoga";
    private static final String TRAINEE_USERNAME = "traineeName";
    private static final String TRAINER_USERNAME = "trainerUsername";
    private static final LocalDateTime FROM_DATE = LocalDateTime.now().minusDays(1);
    private static final LocalDateTime TO_DATE = LocalDateTime.now();

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private ValidationUtils validationUtils;
    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingService trainingService;

    private TrainingDto trainingDto;
    private TrainingEntity trainingEntity;

    @BeforeEach
    void setUp() {
        trainingDto = new TrainingDto();
        trainingDto.setTraineeId(TRAINEE_ID);
        trainingDto.setTrainerId(TRAINER_ID);
        trainingDto.setTrainingName(TRAINING_NAME);
        trainingDto.setTrainingTypeId(TRAINING_TYPE_ID);
        trainingDto.setTrainingDuration(60);

        trainingEntity = new TrainingEntity();
        trainingEntity.setId(1L);
    }

    @Test
    void testAddTrainingSuccess() {
        when(traineeRepository.findById(TRAINEE_ID)).thenReturn(Optional.of(new TraineeEntity()));
        when(trainerRepository.findById(TRAINER_ID)).thenReturn(Optional.of(new TrainerEntity()));
        when(trainingTypeRepository.findById(anyLong())).thenReturn(Optional.of(new TrainingTypeEntity()));
        doNothing().when(validationUtils).validateTraining(any(TrainingEntity.class));

        trainingService.addTraining(trainingDto);

        verify(trainingRepository, times(1)).save(any(TrainingEntity.class));
    }

    @Test
    void testAddTrainingTraineeNotFound() {
        when(traineeRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            trainingService.addTraining(trainingDto);
        });
        assertEquals("Trainee not found", exception.getMessage());
    }

    @Test
    void testAddTrainingTrainerNotFound() {
        when(traineeRepository.findById(TRAINEE_ID)).thenReturn(Optional.of(new TraineeEntity()));
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            trainingService.addTraining(trainingDto);
        });
        assertEquals("Trainer not found", exception.getMessage());
    }

    @Test
    void testGetTrainingsForTraineeSuccess() {
        when(traineeRepository.findByTraineeFromUsername(TRAINEE_USERNAME)).thenReturn(Optional.of(new TraineeEntity()));
        when(trainingRepository.findTrainingsForTrainee(anyLong(), any(LocalDateTime.class),
                any(LocalDateTime.class), anyString(), anyString()))
                .thenReturn(Optional.of(List.of(trainingEntity)));

        List<TrainingEntity> result = trainingService.getTrainingsForTrainee(TRAINEE_USERNAME, FROM_DATE,
                TO_DATE, "trainerName", "trainingType");

        assertEquals(1, result.size());
        verify(trainingRepository, times(1)).findTrainingsForTrainee(anyLong(), any(LocalDateTime.class),
                any(LocalDateTime.class), anyString(), anyString());
    }

    @Test
    void testGetTrainingsForTraineeTraineeNotFound() {
        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            trainingService.getTrainingsForTrainee(TRAINEE_USERNAME, FROM_DATE, TO_DATE, "trainerName", "trainingType");
        });
        assertEquals("Trainee not found", exception.getMessage());
    }

    @Test
    void testGetTrainingsForTraineeNoTrainingsFound() {
        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.of(new TraineeEntity()));
        when(trainingRepository.findTrainingsForTrainee(anyLong(), any(LocalDateTime.class),
                any(LocalDateTime.class), anyString(), anyString()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            trainingService.getTrainingsForTrainee(TRAINEE_USERNAME, FROM_DATE, TO_DATE, "trainerName", "trainingType");
        });
        assertEquals("Trainings not found", exception.getMessage());
    }

    @Test
    void testGetTrainingsForTrainerSuccess() {
        when(trainerRepository.findByTrainerFromUsername(TRAINER_USERNAME)).thenReturn(Optional.of(new TrainerEntity()));
        when(trainingRepository.findTrainingsForTrainer(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), anyString()))
                .thenReturn(Optional.of(List.of(trainingEntity)));

        List<TrainingEntity> result = trainingService.getTrainingsForTrainer(TRAINER_USERNAME, FROM_DATE,
                TO_DATE, "traineeName");

        assertEquals(1, result.size());
        verify(trainingRepository, times(1)).findTrainingsForTrainer(anyLong(), any(LocalDateTime.class),
                any(LocalDateTime.class), anyString());
    }

    @Test
    void testGetTrainingsForTrainerTrainerNotFound() {
        when(trainerRepository.findByTrainerFromUsername(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            trainingService.getTrainingsForTrainer(TRAINER_USERNAME, FROM_DATE, TO_DATE, "traineeName");
        });
        assertEquals("Trainer not found", exception.getMessage());
    }

    @Test
    void testGetTrainingsForTrainerNoTrainingsFound() {
        when(trainerRepository.findByTrainerFromUsername(anyString())).thenReturn(Optional.of(new TrainerEntity()));
        when(trainingRepository.findTrainingsForTrainer(anyLong(), any(LocalDateTime.class),
                any(LocalDateTime.class), anyString()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            trainingService.getTrainingsForTrainer(TRAINER_USERNAME, FROM_DATE, TO_DATE, "traineeName");
        });
        assertEquals("Trainings not found", exception.getMessage());
    }
}
