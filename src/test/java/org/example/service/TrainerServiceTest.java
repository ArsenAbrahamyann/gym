package org.example.service;

import static javax.management.Query.eq;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
import org.example.dto.TrainingTypeDto;
import org.example.dto.UserDto;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.entity.UserEntity;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.TrainerRepository;
import org.example.repository.TrainingRepository;
import org.example.repository.TrainingTypeRepository;
import org.example.repository.UserRepository;
import org.example.utils.UserUtils;
import org.example.utils.ValidationUtils;
import org.hibernate.type.LocalDateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TrainerService trainerService;

    private TrainerDto trainerDto;
    private TrainerEntity trainerEntity;
    private UserEntity userEntity;
    private TrainingTypeEntity trainingTypeEntity;

    @BeforeEach
    void setup() {
        userEntity = new UserEntity();
        userEntity.setUsername("johndoe");
        userEntity.setPassword("password");
        userEntity.setIsActive(true);

        trainingTypeEntity = new TrainingTypeEntity();
        trainingTypeEntity.setId(1L);
        trainingTypeEntity.setTrainingTypeName("Yoga");

        trainerEntity = new TrainerEntity();
        trainerEntity.setId(1L);
        trainerEntity.setUser(userEntity);
        trainerEntity.setSpecialization(trainingTypeEntity);

        trainerDto = new TrainerDto();
        trainerDto.setSpecialization(new TrainingTypeDto("Yoga"));
        trainerDto.setUser(new UserDto("John", "Doe", true, "password", "johndoe"));
    }

    @Test
    @Transactional
    void testCreateTrainerProfile() {
        // Arrange
        TrainerDto trainerDto = new TrainerDto();
        TrainerEntity trainerEntity = new TrainerEntity();
        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity();
        UserDto userDto = new UserDto();


        trainerDto.setUser(userDto);
        trainerEntity.setSpecialization(trainingTypeEntity);
        trainerEntity.setUser(userEntity);

        when(modelMapper.map(trainerDto, TrainerEntity.class)).thenReturn(trainerEntity);
        when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        doNothing().when(validationUtils).validateTrainer(trainerEntity);

        // Act
        TrainerDto result = trainerService.createTrainerProfile(trainerDto);

        // Assert
        verify(trainingTypeRepository).save(trainingTypeEntity);
        verify(userRepository).save(userEntity);
        verify(userService).authenticateUser(userEntity.getUsername(), userEntity.getPassword());
        verify(trainerRepository).save(trainerEntity);

        assertThat(result).isEqualTo(trainerDto);
    }

    @Test
    void testChangeTrainerPassword() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));

        trainerService.changeTrainerPassword("johndoe", "newpassword");

        verify(userRepository).findByUsername("johndoe");
        verify(userRepository).update(userEntity);
        assertEquals("newpassword", userEntity.getPassword());
    }

    @Test
    void testToggleTrainerStatus() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));

        trainerService.toggleTrainerStatus("johndoe");

        verify(userRepository).findByUsername("johndoe");
        verify(userRepository).update(userEntity);
        assertNotEquals(true, userEntity.getIsActive());
    }

    @Test
    void testUpdateTrainerProfileWithNonExistingTrainer() {
        when(trainerRepository.findByTrainerFromUsername(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            trainerService.updateTrainerProfile("nonexistent", trainerDto);
        });

        assertEquals("Trainer not found with username: nonexistent", exception.getMessage());
        verify(trainerRepository).findByTrainerFromUsername("nonexistent");
    }

    @Test
    void testGetTrainerTrainingsWithNoTrainings() {
        // Arrange
        when(trainerRepository.findByTrainerFromUsername(anyString())).thenReturn(Optional.of(trainerEntity));
        when(trainingRepository.findTrainingsForTrainer(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), anyString()))
                .thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            trainerService.getTrainerTrainings("johndoe", LocalDateTime.now().minusDays(1), LocalDateTime.now(), "traineeName");
        });

        assertEquals("Trainings not found", thrown.getMessage());
        verify(trainerRepository).findByTrainerFromUsername("johndoe");
        verify(trainingRepository).findTrainingsForTrainer(1L, LocalDateTime.now().minusDays(1), LocalDateTime.now(), "traineeName");
    }
}
