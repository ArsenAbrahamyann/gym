package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public void getTrainee_ShouldReturnTrainee() {
        String username = "john.doe";
        TraineeEntity trainee = new TraineeEntity();
        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.of(trainee));

        TraineeEntity foundTrainee = traineeService.getTrainee(username);

        assertNotNull(foundTrainee);
        assertEquals(trainee, foundTrainee);
    }

    @Test
    public void getTrainee_ShouldThrowExceptionWhenNotFound() {
        when(traineeRepository.findByUser_Username("invalidUser")).thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> traineeService.getTrainee("invalidUser"));
    }



    @Test
    public void getUnassignedTrainers_ShouldReturnUnassignedTrainers() {
        // Arrange
        String username = "john.doe";
        TraineeEntity trainee = new TraineeEntity();
        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.of(trainee));

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