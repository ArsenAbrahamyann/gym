package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @BeforeEach
    public void setUp() {
        // No setup needed as we are using Mockito's @InjectMocks and @Mock annotations
    }

    @Test
    public void createTraineeProfile_ShouldCreateTrainee() {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");

        when(userUtils.generateUsername("John", "Doe")).thenReturn("john.doe");
        when(userUtils.generatePassword()).thenReturn("password123");

        TraineeEntity createdTrainee = traineeService.createTraineeProfile(trainee);

        assertNotNull(createdTrainee);
        assertEquals("john.doe", createdTrainee.getUsername());
        assertEquals("password123", createdTrainee.getPassword());
        verify(traineeRepository).save(trainee);
    }

    @Test
    public void toggleTraineeStatus_ShouldToggleActiveStatus() {
        String username = "john.doe";
        ActivateRequestDto requestDto = new ActivateRequestDto();
        requestDto.setUsername(username);
        requestDto.setActive(true);

        TraineeEntity trainee = new TraineeEntity();
        trainee.setUsername(username);
        trainee.setIsActive(false);

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        traineeService.toggleTraineeStatus(requestDto);

        assertTrue(trainee.getIsActive());
        verify(traineeRepository).save(trainee);
    }

    @Test
    public void deleteTraineeByUsername_ShouldDeleteTrainee() {
        String username = "john.doe";
        TraineeEntity trainee = new TraineeEntity();

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        traineeService.deleteTraineeByUsername(username);

        verify(traineeRepository).delete(trainee);
    }

    @Test
    public void getTrainee_ShouldReturnTrainee() {
        String username = "john.doe";
        TraineeEntity trainee = new TraineeEntity();
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        TraineeEntity foundTrainee = traineeService.getTrainee(username);

        assertNotNull(foundTrainee);
        assertEquals(trainee, foundTrainee);
    }

    @Test
    public void getTrainee_ShouldThrowExceptionWhenNotFound() {
        when(traineeRepository.findByUsername("invalidUser")).thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> traineeService.getTrainee("invalidUser"));
    }

    @Test
    public void updateTraineeProfile_ShouldUpdateTraineeProfile() {
        UpdateTraineeRequestDto requestDto = new UpdateTraineeRequestDto();
        requestDto.setUsername("john.doe");
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setDateOfBirth(LocalDateTime.parse("2000-01-01T00:00:00"));
        requestDto.setAddress("123 Main St");
        requestDto.setPublic(true);

        TraineeEntity trainee = new TraineeEntity();
        trainee.setUsername("john.doe");

        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

        TraineeEntity updatedTrainee = traineeService.updateTraineeProfile(requestDto);

        assertEquals("John", updatedTrainee.getFirstName());
        assertEquals("Doe", updatedTrainee.getLastName());
        assertEquals("123 Main St", updatedTrainee.getAddress());
        assertTrue(updatedTrainee.getIsActive());
        verify(validationUtils).validateUpdateTrainee(trainee);
        verify(traineeRepository).save(trainee);
    }

    @Test
    public void updateTraineeTrainerList_ShouldUpdateTrainerList() {
        UpdateTraineeTrainerListRequestDto requestDto = new UpdateTraineeTrainerListRequestDto();
        requestDto.setTraineeUsername("john.doe");
        requestDto.setTrainerUsername(List.of("trainer1", "trainer2"));

        TraineeEntity trainee = new TraineeEntity();
        trainee.setUsername("john.doe");

        TrainerEntity trainer1 = new TrainerEntity();
        trainer1.setUsername("trainer1");

        TrainerEntity trainer2 = new TrainerEntity();
        trainer2.setUsername("trainer2");

        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));
        when(trainerService.findByUsernames(requestDto.getTrainerUsername()))
                .thenReturn(List.of(trainer1, trainer2));

        TraineeEntity updatedTrainee = traineeService.updateTraineeTrainerList(requestDto);

        assertEquals(2, updatedTrainee.getTrainers().size());
        assertTrue(updatedTrainee.getTrainers().contains(trainer1));
        assertTrue(updatedTrainee.getTrainers().contains(trainer2));
        verify(validationUtils).validateUpdateTraineeTrainerList(trainee, List.of(trainer1, trainer2));
        verify(traineeRepository).save(trainee);
    }


    @Test
    public void getUnassignedTrainers_ShouldReturnUnassignedTrainers() {
        // Arrange
        String username = "john.doe";
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUsername(username);
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        List<TrainerEntity> allTrainers = new ArrayList<>(List.of(new TrainerEntity(), new TrainerEntity()));
        List<TrainerEntity> assignedTrainers = List.of(allTrainers.get(0));

        when(trainerService.findAll()).thenReturn(allTrainers);
        when(trainerService.findAssignedTrainers(trainee.getId())).thenReturn(assignedTrainers);

        // Act
        List<TrainerEntity> unassignedTrainers = traineeService.getUnassignedTrainers(username);

        // Assert
        assertEquals(1, unassignedTrainers.size());
    }
}