package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
import org.example.dto.TrainingTypeDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.UserEntity;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.TraineeRepository;
import org.example.repository.TrainerRepository;
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
    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private ValidationUtils validationUtils;

    @InjectMocks
    private TrainingService trainingService;

    private TraineeDto traineeDto;
    private TrainerDto trainerDto;
    private TrainingEntity trainingEntity;
    private TrainingDto trainingDto;

    @BeforeEach
    void setUp() {
        traineeDto = new TraineeDto();
        trainerDto = new TrainerDto();
        trainingEntity = new TrainingEntity();
        trainingEntity.setId(1L);
        trainingDto = new TrainingDto();
        trainingDto.setTrainee(traineeDto);
        trainingDto.setTrainer(trainerDto);
        trainingDto.setTrainingName("Yoga");
        trainingDto.setTrainingType(new TrainingTypeDto());
        trainingDto.setTrainingDate(new Date());
        trainingDto.setTrainingDuration(60);
    }

    @Test
    void testAddTrainingSuccess() {
        // Given
        when(traineeRepository.findById(1L)).thenReturn(Optional.of(new TraineeEntity()));
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(new TrainerEntity()));
        doNothing().when(validationUtils).validateTraining(any(TrainingEntity.class));

        // When
        trainingService.addTraining(trainingDto);

        // Then
        verify(trainingRepository, times(1)).save(any(TrainingEntity.class));
    }

    @Test
    void testAddTrainingTraineeNotFound() {
        // Given
        when(traineeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            trainingService.addTraining(trainingDto);
        });
        assertEquals("Trainee not found", exception.getMessage());
    }

    @Test
    void testAddTrainingTrainerNotFound() {
        // Given
        when(traineeRepository.findById(1L)).thenReturn(Optional.of(new TraineeEntity()));
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            trainingService.addTraining(trainingDto);
        });
        assertEquals("Trainer not found", exception.getMessage());
    }

    @Test
    void testGetTrainingsForTraineeSuccess() {
        // Given
        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.of(new TraineeEntity()));
        when(trainingRepository.findTrainingsForTrainee(anyLong(), any(Date.class),
                any(Date.class), anyString(), anyString()))
                .thenReturn(Optional.of(List.of(trainingEntity)));

        // When
        List<TrainingEntity> result = trainingService.getTrainingsForTrainee("traineeName", new Date(),
                new Date(), "trainerName", "trainingType");

        // Then
        assertEquals(1, result.size());
        verify(trainingRepository, times(1)).findTrainingsForTrainee(anyLong(),
                any(Date.class), any(Date.class), anyString(), anyString());
    }

    @Test
    void testGetTrainingsForTraineeTraineeNotFound() {
        // Given
        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            trainingService.getTrainingsForTrainee("traineeName", new Date(),
                    new Date(), "trainerName", "trainingType");
        });
        assertEquals("Trainee not found", exception.getMessage());
    }

    @Test
    void testGetTrainingsForTraineeNoTrainingsFound() {
        // Given
        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.of(new TraineeEntity()));
        when(trainingRepository.findTrainingsForTrainee(anyLong(), any(Date.class),
                any(Date.class), anyString(), anyString()))
                .thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            trainingService.getTrainingsForTrainee("traineeName", new Date(),
                    new Date(), "trainerName", "trainingType");
        });
        assertEquals("Trainings not found", exception.getMessage());
    }

    @Test
    void testGetTrainingsForTrainerSuccess() {
        // Given
        when(trainerRepository.findByTrainerFromUsername(anyString())).thenReturn(Optional.of(new TrainerEntity()));
        when(trainingRepository.findTrainingsForTrainer(anyLong(), any(Date.class), any(Date.class), anyString()))
                .thenReturn(Optional.of(List.of(trainingEntity)));

        // When
        List<TrainingEntity> result = trainingService.getTrainingsForTrainer("trainerUsername",
                new Date(), new Date(), "traineeName");

        // Then
        assertEquals(1, result.size());
        verify(trainingRepository, times(1)).findTrainingsForTrainer(anyLong(),
                any(Date.class), any(Date.class), anyString());
    }

    @Test
    void testGetTrainingsForTrainerTrainerNotFound() {
        // Given
        when(trainerRepository.findByTrainerFromUsername(anyString())).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            trainingService.getTrainingsForTrainer("trainerUsername",
                    new Date(), new Date(), "traineeName");
        });
        assertEquals("Trainer not found", exception.getMessage());
    }

    @Test
    void testGetTrainingsForTrainerNoTrainingsFound() {
        // Given
        when(trainerRepository.findByTrainerFromUsername(anyString())).thenReturn(Optional.of(new TrainerEntity()));
        when(trainingRepository.findTrainingsForTrainer(anyLong(), any(Date.class),
                any(Date.class), anyString()))
                .thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            trainingService.getTrainingsForTrainer("trainerUsername",
                    new Date(), new Date(), "traineeName");
        });
        assertEquals("Trainings not found", exception.getMessage());
    }
}
