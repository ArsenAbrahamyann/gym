package org.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.hibernate.type.LocalDateTimeType;
import org.hibernate.type.LocalDateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingRepositoryTest {
    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainingEntity training;

    @Mock
    private TraineeEntity trainee;

    @Mock
    private TrainerEntity trainer;

    @Mock
    private TrainingTypeEntity trainingType;

    @BeforeEach
    public void setUp() {
        training = new TrainingEntity();
        training.setId(1L);
        training.setTrainingName("Yoga Basics");
        training.setTrainingDate(LocalDateTime.now());
        training.setTrainingDuration(60);
        training.setTrainingType(trainingType);
        training.setTrainee(trainee);
        training.setTrainer(trainer);

        trainee = new TraineeEntity();
        trainee.setId(1L);

        trainer = new TrainerEntity();
        trainer.setId(1L);

        trainingType = new TrainingTypeEntity();
        trainingType.setId(1L);
        trainingType.setTrainingTypeName("Yoga");
    }

    @Test
    public void testFindByTrainingName() {
        // Arrange
        when(trainingRepository.findByTrainingName(anyString())).thenReturn(Optional.of(training));

        // Act
        Optional<TrainingEntity> result = trainingRepository.findByTrainingName("Yoga Basics");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(training.getTrainingName(), result.get().getTrainingName());
        verify(trainingRepository, times(1)).findByTrainingName(anyString());
    }

    @Test
    public void testFindByTrainingName_NotFound() {
        // Arrange
        when(trainingRepository.findByTrainingName(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<TrainingEntity> result = trainingRepository.findByTrainingName("Nonexistent Training");

        // Assert
        assertFalse(result.isPresent());
        verify(trainingRepository, times(1)).findByTrainingName(anyString());
    }

    @Test
    public void testSave() {
        // Arrange
        doNothing().when(trainingRepository).save(any(TrainingEntity.class));

        // Act
        trainingRepository.save(training);

        // Assert
        verify(trainingRepository, times(1)).save(any(TrainingEntity.class));
    }

    @Test
    public void testFindTrainingsForTrainee() {
        // Arrange
        List<TrainingEntity> trainings = new ArrayList<>();
        trainings.add(training);
        when(trainingRepository.findTrainingsForTrainee(anyLong(), any(), any(), anyString(), anyString()))
                .thenReturn(Optional.of(trainings));

        // Act
        Optional<List<TrainingEntity>> result = trainingRepository.findTrainingsForTrainee(
                1L, LocalDateTime.now(), LocalDateTime.now(), "John Doe", "Yoga");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        verify(trainingRepository, times(1))
                .findTrainingsForTrainee(anyLong(), any(), any(), anyString(), anyString());
    }

    @Test
    public void testFindTrainingsForTrainee_EmptyList() {
        // Arrange
        when(trainingRepository.findTrainingsForTrainee(anyLong(), any(), any(), anyString(), anyString()))
                .thenReturn(Optional.of(new ArrayList<>()));

        // Act
        Optional<List<TrainingEntity>> result = trainingRepository.findTrainingsForTrainee(
                1L, LocalDateTime.now(), LocalDateTime.now(), "John Doe", "Yoga");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(0, result.get().size());
        verify(trainingRepository, times(1))
                .findTrainingsForTrainee(anyLong(), any(), any(), anyString(), anyString());
    }

    @Test
    public void testFindTrainingsForTrainer() {
        // Arrange
        List<TrainingEntity> trainings = new ArrayList<>();
        trainings.add(training);
        when(trainingRepository.findTrainingsForTrainer(anyLong(), any(), any(), anyString()))
                .thenReturn(Optional.of(trainings));

        // Act
        Optional<List<TrainingEntity>> result = trainingRepository.findTrainingsForTrainer(
                1L, LocalDateTime.now(), LocalDateTime.now(), "Jane Doe");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        verify(trainingRepository, times(1))
                .findTrainingsForTrainer(anyLong(), any(), any(), anyString());
    }

    @Test
    public void testFindTrainingsForTrainer_EmptyList() {
        // Arrange
        when(trainingRepository.findTrainingsForTrainer(anyLong(), any(), any(), anyString()))
                .thenReturn(Optional.of(new ArrayList<>()));

        // Act
        Optional<List<TrainingEntity>> result = trainingRepository.findTrainingsForTrainer(
                1L, LocalDateTime.now(), LocalDateTime.now(), "Jane Doe");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(0, result.get().size());
        verify(trainingRepository, times(1))
                .findTrainingsForTrainer(anyLong(), any(), any(), anyString());
    }
}
