package org.example.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingTypeDto;
import org.example.dto.UserDto;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.entity.UserEntity;
import org.example.repository.TrainerRepository;
import org.example.repository.TrainingRepository;
import org.example.repository.TrainingTypeRepository;
import org.example.repository.UserRepository;
import org.example.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

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
    public void testCreateTrainerProfile_ShouldCreateTrainer() {
        TrainerEntity trainer = new TrainerEntity();
        trainer.setId(1L);

        trainerRepository.save(trainer);

        verify(trainerRepository, times(1)).save(trainer);
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
        List<TrainerEntity> trainers = List.of(new TrainerEntity());
        when(trainerRepository.findAssignedTrainers(1L)).thenReturn(Optional.of(trainers));

        Optional<List<TrainerEntity>> assignedTrainers = trainerRepository.findAssignedTrainers(1L);

        assertThat(assignedTrainers).isPresent();
        assertThat(assignedTrainers.get());
    }
}
