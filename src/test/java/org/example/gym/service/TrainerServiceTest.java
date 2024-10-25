package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import org.example.gym.dto.request.ActivateRequestDto;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingTypeEntity;
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
     * Sets up the test environment by initializing necessary objects and mocking
     * dependencies before each test.
     */
    @BeforeEach
    public void setUp() {
        trainer = new TrainerEntity();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setSpecialization(new TrainingTypeEntity(1L, "Yoga"));
    }

    @Test
    public void testCreateTrainerProfile() {
        when(userService.findAllUsernames()).thenReturn(Arrays.asList("existingUser"));
        when(userUtils.generateUsername("John", "Doe", Arrays.asList("existingUser"))).thenReturn("john.doe");
        when(userUtils.generatePassword()).thenReturn("generatedPassword");
        when(trainerRepository.save(trainer)).thenReturn(trainer);

        TrainerEntity createdTrainer = trainerService.createTrainerProfile(trainer);

        assertNotNull(createdTrainer);
        assertEquals("john.doe", createdTrainer.getUsername());
        verify(trainerRepository).save(trainer);
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
    public void testUpdateTrainerProfile() {
        when(trainerRepository.save(trainer)).thenReturn(trainer);

        TrainerEntity updatedTrainer = trainerService.updateTrainerProfile(trainer);

        assertNotNull(updatedTrainer);
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
    public void testFindByUsernames() {
        String username = "john.doe";
        when(trainerRepository.findAllByUsernameIn(Arrays.asList(username))).thenReturn(Arrays.asList(trainer));

        var trainers = trainerService.findByUsernames(Arrays.asList(username));

        assertNotNull(trainers);
        assertEquals(1, trainers.size());
        verify(trainerRepository).findAllByUsernameIn(Arrays.asList(username));
    }
}
