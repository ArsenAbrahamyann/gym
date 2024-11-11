package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
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

/**
 * Test class for {@link TrainerService}.
 */
@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingService trainingService;

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private UserService userService;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private TrainerService trainerService;

    private TrainerEntity trainer;
    private UserEntity user;

    /**
     * Initializes the test environment by setting up a default `TrainerEntity` instance.
     * This method is run before each test case to ensure consistent starting values.
     * The `trainer` instance is created with the following default values:
     * - First name: "John"
     * - Last name: "Doe"
     * - Specialization: A `TrainingTypeEntity` with ID `1L` and type name "Yoga"
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
    public void testFindAll() {
        when(trainerRepository.findAll()).thenReturn(Arrays.asList(trainer));

        var trainers = trainerService.findAll();

        assertNotNull(trainers);
        assertEquals(1, trainers.size());
        verify(trainerRepository).findAll();
    }

    @Test
    public void testFindAssignedTrainers() {
        Long traineeId = 1L;
        when(trainerRepository.findByTrainees_Id(traineeId)).thenReturn(Arrays.asList(trainer));

        var trainers = trainerService.findAssignedTrainers(traineeId);

        assertNotNull(trainers);
        assertEquals(1, trainers.size());
        verify(trainerRepository).findByTrainees_Id(traineeId);
    }

    @Test
    public void testGetTrainer() {
        String username = "john.doe";
        when(trainerRepository.findByUser_Username(username)).thenReturn(Optional.of(trainer));

        TrainerEntity foundTrainer = trainerService.getTrainer(username);

        assertNotNull(foundTrainer);
        assertEquals(trainer, foundTrainer);
        verify(trainerRepository).findByUser_Username(username);
    }

    @Test
    public void testGetTrainerNotFound() {
        String username = "nonexistent.user";
        when(trainerRepository.findByUser_Username(username)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.getTrainer(username));
    }


    @Test
    public void testUpdateTrainerProfileNotFound() {
        String username = "nonexistent.user";
        UpdateTrainerRequestDto requestDto = new UpdateTrainerRequestDto();
        requestDto.setUsername(username);

        when(trainerRepository.findByUser_Username(username)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.updateTrainerProfile(requestDto));
    }

}