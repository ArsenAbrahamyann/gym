package org.example.gym.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainerRepositoryTest {

    @Mock
    private TrainerRepository trainerRepository;
    private UserEntity mockUser;


    /**
     * Set up method to initialize the test environment.
     * This method is called before each test case and initializes the necessary mocks.
     */
    @BeforeEach
    public void setUp() {
        mockUser = new UserEntity();
    }

    @Test
    public void testFindTrainerByUsername_WhenExists() {
        String username = "trainer1";
        TrainerEntity trainer = new TrainerEntity();
        mockUser.setUsername(username);
        trainer.setUser(mockUser);

        when(trainerRepository.findByUser_Username(username)).thenReturn(Optional.of(trainer));

        Optional<TrainerEntity> result = trainerRepository.findByUser_Username(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUser().getUsername());
        verify(trainerRepository, times(1)).findByUser_Username(username);
    }

    @Test
    public void testFindTrainerByUsername_WhenNotExists() {
        String username = "trainer2";

        when(trainerRepository.findByUser_Username(username)).thenReturn(Optional.empty());

        Optional<TrainerEntity> result = trainerRepository.findByUser_Username(username);

        assertFalse(result.isPresent());
        verify(trainerRepository, times(1)).findByUser_Username(username);
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

}
