package org.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.example.dto.TrainerDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.entity.UserEntity;
import org.example.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class TrainerRepositoryTest {
    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainerEntity trainer;

    @Mock
    private UserEntity user;

    @Mock
    private TrainingTypeEntity specialization;

    @BeforeEach
    public void setUp() {
        user = new UserEntity();
        user.setId(1L);
        user.setUsername("testtrainer");
        user.setPassword("password");
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setIsActive(true);

        specialization = new TrainingTypeEntity();
        specialization.setId(1L);

        trainer = new TrainerEntity();
        trainer.setId(1L);
        trainer.setSpecialization(specialization);
        trainer.setUser(user);

        Set<TrainerEntity> trainers = new HashSet<>();
        trainers.add(trainer);
    }

    @Test
    public void testFindByTrainerFromUsername() {
        // Arrange
        when(trainerRepository.findByTrainerFromUsername(any(String.class))).thenReturn(Optional.of(trainer));

        // Act
        Optional<TrainerEntity> result = trainerRepository.findByTrainerFromUsername("testtrainer");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(trainer.getUser().getUsername(), result.get().getUser().getUsername());
        verify(trainerRepository, times(1)).findByTrainerFromUsername(any(String.class));
    }

    @Test
    public void testFindById() {
        // Arrange
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.of(trainer));

        // Act
        Optional<TrainerEntity> result = trainerRepository.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(trainer.getId(), result.get().getId());
        verify(trainerRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testSave() {
        // Arrange
        doNothing().when(trainerRepository).save(any(TrainerEntity.class));

        // Act
        trainerRepository.save(trainer);

        // Assert
        verify(trainerRepository, times(1)).save(any(TrainerEntity.class));
    }

    @Test
    public void testUpdate() {
        // Arrange
        doNothing().when(trainerRepository).update(any(TrainerEntity.class));

        // Act
        trainerRepository.update(trainer);

        // Assert
        verify(trainerRepository, times(1)).update(any(TrainerEntity.class));
    }

    @Test
    public void testFindAll() {
        // Arrange
        List<TrainerEntity> trainers = new ArrayList<>();
        trainers.add(trainer);
        when(trainerRepository.findAll()).thenReturn(Optional.of(trainers));

        // Act
        Optional<List<TrainerEntity>> result = trainerRepository.findAll();

        // Assert
        assertTrue(result.isPresent());
        assertEquals(trainers.size(), result.get().size());
        verify(trainerRepository, times(1)).findAll();
    }

    @Test
    public void testFindAssignedTrainers() {
        // Arrange
        List<TrainerEntity> trainers = new ArrayList<>();
        trainers.add(trainer);
        when(trainerRepository.findAssignedTrainers(anyLong())).thenReturn(Optional.of(trainers));

        // Act
        Optional<List<TrainerEntity>> result = trainerRepository.findAssignedTrainers(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(trainers.size(), result.get().size());
        verify(trainerRepository, times(1)).findAssignedTrainers(anyLong());
    }

    @Test
    public void testFindAllById() {
        // Arrange
        List<TrainerEntity> trainers = new ArrayList<>();
        trainers.add(trainer);
        when(trainerRepository.findAllById(anyList())).thenReturn(Optional.of(trainers));

        // Act
        Optional<List<TrainerEntity>> result = trainerRepository.findAllById(List.of(1L));

        // Assert
        assertTrue(result.isPresent());
        assertEquals(trainers.size(), result.get().size());
        verify(trainerRepository, times(1)).findAllById(anyList());
    }
}
