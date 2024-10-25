package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import org.example.gym.dto.request.ActivateRequestDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.TraineeNotFoundException;
import org.example.gym.repository.TraineeRepository;
import org.example.gym.utils.UserUtils;
import org.example.gym.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the TraineeService class.
 */
@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerService trainerService;

    @Mock
    private UserService userService;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private TraineeService traineeService;

    /**
     * Setup method to initialize the test environment before each test.
     * This method is executed before each test in this class.
     */
    @BeforeEach
    public void setUp() {
    }

    @Test
    public void createTraineeProfile_ShouldCreateTrainee() {
        // Arrange
        TraineeEntity trainee = new TraineeEntity();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");

        when(userService.findAllUsernames()).thenReturn(new ArrayList<>());
        when(userUtils.generateUsername("John", "Doe", new ArrayList<>())).thenReturn("john.doe");
        when(userUtils.generatePassword()).thenReturn("password123");

        // Act
        TraineeEntity createdTrainee = traineeService.createTraineeProfile(trainee);

        // Assert
        assertNotNull(createdTrainee);
        assertEquals("john.doe", createdTrainee.getUsername());
        assertEquals("password123", createdTrainee.getPassword());
        verify(traineeRepository).save(trainee);
    }

    @Test
    public void toggleTraineeStatus_ShouldToggleActiveStatus() {
        // Arrange
        String username = "john.doe";
        ActivateRequestDto requestDto = new ActivateRequestDto();
        requestDto.setUsername(username);
        requestDto.setActive(true);

        TraineeEntity trainee = new TraineeEntity();
        trainee.setUsername(username);
        trainee.setIsActive(false);

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        // Act
        traineeService.toggleTraineeStatus(requestDto);

        // Assert
        assertTrue(trainee.getIsActive());
        verify(traineeRepository).save(trainee);
    }

    @Test
    public void updateTraineeTrainers_ShouldUpdateTrainers() {
        // Arrange
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUsername("john.doe");
        Set<TrainerEntity> trainers = Set.of(new TrainerEntity(), new TrainerEntity());

        trainee.setTrainers(trainers);
        when(traineeRepository.save(any(TraineeEntity.class))).thenReturn(trainee);

        // Act
        TraineeEntity updatedTrainee = traineeService.updateTraineeTrainers(trainee);

        // Assert
        assertNotNull(updatedTrainee);
        verify(traineeRepository).save(trainee);
    }

    @Test
    public void updateTraineeProfile_ShouldUpdateProfile() {
        // Arrange
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUsername("john.doe");

        when(traineeRepository.save(any(TraineeEntity.class))).thenReturn(trainee);

        // Act
        TraineeEntity updatedTrainee = traineeService.updateTraineeProfile(trainee);

        // Assert
        assertNotNull(updatedTrainee);
        verify(traineeRepository).save(trainee);
    }

    @Test
    public void deleteTraineeByUsername_ShouldDeleteTrainee() {
        // Arrange
        String username = "john.doe";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        TraineeEntity trainee = new TraineeEntity();

        when(userService.findByUsername(username)).thenReturn(user);
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        // Act
        traineeService.deleteTraineeByUsername(username);

        // Assert
        verify(traineeRepository).delete(trainee);
    }

    @Test
    public void getTrainee_ShouldReturnTrainee() {
        // Arrange
        String username = "john.doe";
        TraineeEntity trainee = new TraineeEntity();
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        // Act
        TraineeEntity foundTrainee = traineeService.getTrainee(username);

        // Assert
        assertNotNull(foundTrainee);
        assertEquals(trainee, foundTrainee);
    }

    @Test
    public void createTraineeProfile_TraineeNotFound() {
        // Arrange
        when(traineeRepository.findByUsername("invalidUser"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TraineeNotFoundException.class, () -> traineeService.getTrainee("invalidUser"));
    }


}
