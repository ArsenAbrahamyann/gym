package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import org.example.gym.dto.request.ActivateRequestDto;
import org.example.gym.dto.request.UpdateTrainerRequestDto;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.TrainerNotFoundException;
import org.example.gym.repository.TrainerRepository;
import org.example.gym.utils.UserUtils;
import org.example.gym.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Test class for {@link TrainerService}.
 */
@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private UserService userService;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private UserUtils userUtils;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private TrainerService trainerService;

    private TrainerEntity trainer;
    private UserEntity user;

    /**
     * Initializes the test data before each test case.
     * <p>
     * This method sets up the necessary objects for the tests. It creates a new {@link TrainerEntity}
     * and a {@link UserEntity}, and sets the user's first name and last name to "John" and "Doe", respectively.
     * Additionally, it associates the {@link UserEntity} with the {@link TrainerEntity} and sets the trainer's
     * specialization to a new {@link TrainingTypeEntity} with an ID of 1L and the name "Yoga".
     * </p>
     * <p>
     * This setup ensures that each test runs with a fresh and consistent set of data, allowing for proper
     * isolation of test cases.
     * </p>
     */
    @BeforeEach
    public void setUp() {
        trainer = new TrainerEntity();
        user = new UserEntity();
        user.setFirstName("John");
        user.setLastName("Doe");
        trainer.setUser(user);
        trainer.setSpecialization(new TrainingTypeEntity(1L, "Yoga"));
    }

    @Test
    public void testCreateTrainerProfile() {
        // Arrange
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userService.save(any(UserEntity.class))).thenReturn(user);
        when(trainerRepository.save(any(TrainerEntity.class))).thenReturn(trainer);

        // Act
        TrainerEntity createdTrainer = trainerService.createTrainerProfile(trainer);

        // Assert
        assertNotNull(createdTrainer);
        assertEquals(user.getFirstName(), createdTrainer.getUser().getFirstName());
        verify(trainerRepository).save(trainer);
    }

    @Test
    public void testToggleTrainerStatus() {
        // Arrange
        ActivateRequestDto requestDto = new ActivateRequestDto();
        requestDto.setUsername("john.doe");
        requestDto.setActive(true);
        when(trainerRepository.findByUser_Username("john.doe")).thenReturn(Optional.of(trainer));
        when(userService.save(any(UserEntity.class))).thenReturn(user);

        // Act
        trainerService.toggleTrainerStatus(requestDto);

        // Assert
        assertTrue(user.getIsActive());
        verify(trainerRepository).save(trainer);
    }

    @Test
    public void testUpdateTrainerProfile() {
        // Arrange
        UpdateTrainerRequestDto requestDto = new UpdateTrainerRequestDto();
        requestDto.setUsername("john.doe");
        requestDto.setFirstName("Jane");
        requestDto.setLastName("Doe");
        requestDto.setTrainingTypeId(1L);
        requestDto.setPublic(true);

        TrainingTypeEntity trainingType = new TrainingTypeEntity(1L, "Yoga");
        when(trainerRepository.findByUser_Username("john.doe")).thenReturn(Optional.of(trainer));
        when(trainingTypeService.findById(1L)).thenReturn(trainingType);
        when(userService.save(any(UserEntity.class))).thenReturn(user);
        when(trainerRepository.save(any(TrainerEntity.class))).thenReturn(trainer);

        // Act
        TrainerEntity updatedTrainer = trainerService.updateTrainerProfile(requestDto);

        // Assert
        assertNotNull(updatedTrainer);
        assertEquals("Jane", updatedTrainer.getUser().getFirstName());
        verify(trainerRepository).save(trainer);
    }

    @Test
    public void testFindAll() {
        // Arrange
        when(trainerRepository.findAll()).thenReturn(Arrays.asList(trainer));

        // Act
        var trainers = trainerService.findAll();

        // Assert
        assertNotNull(trainers);
        assertEquals(1, trainers.size());
        verify(trainerRepository).findAll();
    }

    @Test
    public void testFindAssignedTrainers() {
        // Arrange
        Long traineeId = 1L;
        when(trainerRepository.findByTrainees_Id(traineeId)).thenReturn(Arrays.asList(trainer));

        // Act
        var trainers = trainerService.findAssignedTrainers(traineeId);

        // Assert
        assertNotNull(trainers);
        assertEquals(1, trainers.size());
        verify(trainerRepository).findByTrainees_Id(traineeId);
    }

    @Test
    public void testGetTrainer() {
        // Arrange
        String username = "john.doe";
        when(trainerRepository.findByUser_Username(username)).thenReturn(Optional.of(trainer));

        // Act
        TrainerEntity foundTrainer = trainerService.getTrainer(username);

        // Assert
        assertNotNull(foundTrainer);
        assertEquals(trainer, foundTrainer);
        verify(trainerRepository).findByUser_Username(username);
    }

    @Test
    public void testGetTrainerNotFound() {
        // Arrange
        String username = "nonexistent.user";
        when(trainerRepository.findByUser_Username(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TrainerNotFoundException.class, () -> trainerService.getTrainer(username));
    }

    @Test
    public void testUpdateTrainerProfileNotFound() {
        // Arrange
        String username = "nonexistent.user";
        UpdateTrainerRequestDto requestDto = new UpdateTrainerRequestDto();
        requestDto.setUsername(username);

        when(trainerRepository.findByUser_Username(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TrainerNotFoundException.class, () -> trainerService.updateTrainerProfile(requestDto));
    }
}