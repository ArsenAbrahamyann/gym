package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import org.example.gym.dto.request.ActivateRequestDto;
import org.example.gym.dto.request.UpdateTrainerRequestDto;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingTypeEntity;
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
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setSpecialization(new TrainingTypeEntity(1L, "Yoga"));
    }



    @Test
    public void testToggleTrainerStatus() {
        String username = "john.doe";
        ActivateRequestDto requestDto = new ActivateRequestDto(username, true);
        when(trainerRepository.findTrainerByUsername(username)).thenReturn(Optional.of(trainer));

        trainerService.toggleTrainerStatus(requestDto);

        assertTrue(trainer.getIsActive());
        verify(trainerRepository).save(trainer);
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
        when(trainerRepository.findTrainerByUsername(username)).thenReturn(Optional.of(trainer));

        TrainerEntity foundTrainer = trainerService.getTrainer(username);

        assertNotNull(foundTrainer);
        assertEquals(trainer, foundTrainer);
        verify(trainerRepository).findTrainerByUsername(username);
    }

    @Test
    public void testGetTrainerNotFound() {
        String username = "nonexistent.user";
        when(trainerRepository.findTrainerByUsername(username)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.getTrainer(username));
    }

    @Test
    public void testUpdateTrainerProfile() {
        String username = "john.doe";
        UpdateTrainerRequestDto requestDto = new UpdateTrainerRequestDto(username, "John", "Doe",
                1L, true);

        when(trainerRepository.findTrainerByUsername(username)).thenReturn(Optional.of(trainer));
        when(trainingTypeService.findById(1L))
                .thenReturn(new TrainingTypeEntity(1L, "Yoga"));
        when(trainerRepository.save(trainer)).thenReturn(trainer);

        TrainerEntity updatedTrainer = trainerService.updateTrainerProfile(requestDto);

        assertNotNull(updatedTrainer);
        assertEquals("john.doe", updatedTrainer.getUsername());
        assertEquals("John", updatedTrainer.getFirstName());
        assertEquals("Doe", updatedTrainer.getLastName());
        assertTrue(updatedTrainer.getIsActive());

        verify(trainerRepository).findTrainerByUsername(username);
        verify(trainingTypeService).findById(1L);
        verify(trainerRepository).save(trainer);
    }

    @Test
    public void testUpdateTrainerProfileNotFound() {
        String username = "nonexistent.user";
        UpdateTrainerRequestDto requestDto = new UpdateTrainerRequestDto();
        requestDto.setUsername(username);

        when(trainerRepository.findTrainerByUsername(username)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.updateTrainerProfile(requestDto));
    }

    @Test
    public void testFindByUsernames() {
        String username = "john.doe";
        when(trainerRepository.findAllByUsernameIn(Arrays.asList(username))).thenReturn(Arrays.asList(trainer));

        var trainers = trainerService.findByUsernames(Arrays.asList(username));

        assertNotNull(trainers);
        assertEquals(1, trainers.size());
        verify(trainerRepository).findAllByUsernameIn(Arrays.asList(username));
    }
}