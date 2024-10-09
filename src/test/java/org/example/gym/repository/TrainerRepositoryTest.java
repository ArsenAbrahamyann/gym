package org.example.gym.repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.example.gym.entity.TrainerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainerRepositoryTest {

    @Mock
    private TrainerRepository trainerRepository;


    /**
     * Set up method to initialize the test environment.
     * This method is called before each test case and initializes the necessary mocks.
     */
    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testFindTrainerByUsername_WhenExists() {
        String username = "trainer1";
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUsername(username);

        when(trainerRepository.findTrainerByUsername(username)).thenReturn(Optional.of(trainer));

        Optional<TrainerEntity> result = trainerRepository.findTrainerByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        verify(trainerRepository, times(1)).findTrainerByUsername(username);
    }

    @Test
    public void testFindTrainerByUsername_WhenNotExists() {
        String username = "trainer2";

        when(trainerRepository.findTrainerByUsername(username)).thenReturn(Optional.empty());

        Optional<TrainerEntity> result = trainerRepository.findTrainerByUsername(username);

        assertFalse(result.isPresent());
        verify(trainerRepository, times(1)).findTrainerByUsername(username);
    }

    @Test
    public void testFindByTrainees_Id() {
        Long traineeId = 1L;
        TrainerEntity trainer1 = new TrainerEntity();
        TrainerEntity trainer2 = new TrainerEntity();
        List<TrainerEntity> trainers = Arrays.asList(trainer1, trainer2);

        when(trainerRepository.findByTrainees_Id(traineeId)).thenReturn(trainers);

        List<TrainerEntity> result = trainerRepository.findByTrainees_Id(traineeId);

        assertEquals(2, result.size());
        verify(trainerRepository, times(1)).findByTrainees_Id(traineeId);
    }

    @Test
    public void testFindAllByIdIn() {
        List<Long> trainerIds = Arrays.asList(1L, 2L);
        TrainerEntity trainer1 = new TrainerEntity();
        TrainerEntity trainer2 = new TrainerEntity();
        List<TrainerEntity> trainers = Arrays.asList(trainer1, trainer2);

        when(trainerRepository.findAllByIdIn(trainerIds)).thenReturn(trainers);

        List<TrainerEntity> result = trainerRepository.findAllByIdIn(trainerIds);

        assertEquals(2, result.size());
        verify(trainerRepository, times(1)).findAllByIdIn(trainerIds);
    }

    @Test
    public void testFindAllByUsernameIn() {
        List<String> trainerUsernames = Arrays.asList("trainer1", "trainer2");
        TrainerEntity trainer1 = new TrainerEntity();
        TrainerEntity trainer2 = new TrainerEntity();
        List<TrainerEntity> trainers = Arrays.asList(trainer1, trainer2);

        when(trainerRepository.findAllByUsernameIn(trainerUsernames)).thenReturn(trainers);

        List<TrainerEntity> result = trainerRepository.findAllByUsernameIn(trainerUsernames);

        assertEquals(2, result.size());
        verify(trainerRepository, times(1)).findAllByUsernameIn(trainerUsernames);
    }
}
