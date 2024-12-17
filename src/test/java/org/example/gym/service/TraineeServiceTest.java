package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.gym.dto.request.ActivateRequestDto;
import org.example.gym.dto.request.UpdateTraineeRequestDto;
import org.example.gym.dto.request.UpdateTraineeTrainerListRequestDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.TraineeNotFoundException;
import org.example.gym.repository.TraineeRepository;
import org.example.gym.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private ValidationUtils validationUtils;

    @InjectMocks
    private TraineeService traineeService;

    private TraineeEntity trainee;
    private UserEntity user;

    /**
     * Sets up the necessary objects for testing before each test method is executed.
     * This method initializes a sample `UserEntity` and a `TraineeEntity` with a `UserEntity`
     * to be used in the test methods. The `UserEntity` is assigned a username "john.doe".
     * This setup ensures that the `trainee` object has a valid user reference for testing.
     */
    @BeforeEach
    public void setUp() {
        user = new UserEntity();
        user.setUsername("john.doe");
        trainee = new TraineeEntity();
        trainee.setUser(user);
    }

    @Test
    public void createTraineeProfile_ShouldCreateTraineeProfile() {
        // Arrange
        TraineeEntity trainee = new TraineeEntity();
        UserEntity user = new UserEntity();
        trainee.setUser(user);
        user.setPassword("plainPassword");

        // Mock the behavior of the passwordEncoder
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        when(userService.save(any(UserEntity.class))).thenReturn(user); // Mock userService.save()

        // Act
        TraineeEntity createdTrainee = traineeService.createTraineeProfile(trainee);

        // Assert
        assertNotNull(createdTrainee);
        assertEquals("encodedPassword", createdTrainee.getUser().getPassword()); // Verify that the password is encoded
    }

    @Test
    public void getTrainee_ShouldReturnTrainee() {
        // Arrange
        String username = "john.doe";
        TraineeEntity trainee = new TraineeEntity();
        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.of(trainee));

        // Act
        TraineeEntity foundTrainee = traineeService.getTrainee(username);

        // Assert
        assertNotNull(foundTrainee);
        assertEquals(trainee, foundTrainee);
    }

    @Test
    public void getTrainee_ShouldThrowExceptionWhenNotFound() {
        // Arrange
        when(traineeRepository.findByUser_Username("invalidUser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TraineeNotFoundException.class, () -> traineeService.getTrainee("invalidUser"));
    }

    @Test
    public void toggleTraineeStatus_ShouldToggleStatus() {
        // Arrange
        ActivateRequestDto requestDto = new ActivateRequestDto();
        requestDto.setUsername("john.doe");
        requestDto.setActive(true);

        when(traineeRepository.findByUser_Username("john.doe")).thenReturn(Optional.of(trainee));
        when(userService.save(any(UserEntity.class))).thenReturn(user);

        // Act
        traineeService.toggleTraineeStatus(requestDto);

        // Assert
        assertEquals(true, trainee.getUser().getIsActive());
        verify(userService, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void toggleTraineeStatus_ShouldThrowExceptionWhenTraineeNotFound() {
        // Arrange
        ActivateRequestDto requestDto = new ActivateRequestDto();
        requestDto.setUsername("invalidUser");
        requestDto.setActive(true);

        when(traineeRepository.findByUser_Username("invalidUser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TraineeNotFoundException.class, () -> traineeService.toggleTraineeStatus(requestDto));
    }

    @Test
    public void getUnassignedTrainers_ShouldReturnUnassignedTrainers() {
        // Arrange
        String username = "john.doe";
        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.of(trainee));

        List<TrainerEntity> allTrainers = new ArrayList<>();
        allTrainers.add(new TrainerEntity());
        allTrainers.add(new TrainerEntity());

        List<TrainerEntity> assignedTrainers = new ArrayList<>();
        assignedTrainers.add(allTrainers.get(0));

        when(trainerService.findAll()).thenReturn(allTrainers);
        when(trainerService.findAssignedTrainers(trainee.getId())).thenReturn(assignedTrainers);

        // Act
        List<TrainerEntity> unassignedTrainers = traineeService.getUnassignedTrainers(username);

        // Assert
        assertEquals(1, unassignedTrainers.size());
    }

    @Test
    public void updateTraineeTrainerList_ShouldUpdateTrainers() {
        // Arrange
        List<String> trainerUsernames = new ArrayList<>();
        trainerUsernames.add("trainer1");

        UpdateTraineeTrainerListRequestDto requestDto = new UpdateTraineeTrainerListRequestDto();
        requestDto.setTraineeUsername("john.doe");
        requestDto.setTrainerUsername(trainerUsernames);

        when(traineeRepository.findByUser_Username("john.doe")).thenReturn(Optional.of(trainee));
        TrainerEntity trainer = new TrainerEntity();
        when(trainerService.getTrainer("trainer1")).thenReturn(trainer);

        // Act
        TraineeEntity updatedTrainee = traineeService.updateTraineeTrainerList(requestDto);

        // Assert
        assertNotNull(updatedTrainee);
        assertEquals(1, updatedTrainee.getTrainers().size());
        verify(traineeRepository, times(1)).save(any(TraineeEntity.class));
    }

    @Test
    public void updateTraineeProfile_ShouldUpdateTraineeProfile() {
        // Arrange
        UpdateTraineeRequestDto requestDto = new UpdateTraineeRequestDto();
        requestDto.setUsername("john.doe");
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setAddress("New Address");
        requestDto.setDateOfBirth(LocalDateTime.now());
        requestDto.setPublic(true);

        when(traineeRepository.findByUser_Username("john.doe")).thenReturn(Optional.of(trainee));
        when(userService.save(any(UserEntity.class))).thenReturn(user);

        // Act
        TraineeEntity updatedTrainee = traineeService.updateTraineeProfile(requestDto);

        // Assert
        assertNotNull(updatedTrainee);
        assertEquals("John", updatedTrainee.getUser().getFirstName());
        verify(traineeRepository, times(1)).save(any(TraineeEntity.class));
    }

    @Test
    public void deleteTraineeByUsername_ShouldDeleteTrainee() {
        // Arrange
        when(traineeRepository.findByUser_Username("john.doe")).thenReturn(Optional.of(trainee));

        // Act
        traineeService.deleteTraineeByUsername("john.doe");

        // Assert
        verify(traineeRepository, times(1)).delete(any(TraineeEntity.class));
    }

    @Test
    public void deleteTraineeByUsername_ShouldThrowExceptionWhenTraineeNotFound() {
        // Arrange
        when(traineeRepository.findByUser_Username("invalidUser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TraineeNotFoundException.class, () -> traineeService.deleteTraineeByUsername("invalidUser"));
    }
}