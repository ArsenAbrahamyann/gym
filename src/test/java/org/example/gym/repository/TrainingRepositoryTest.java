package org.example.gym.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingRepositoryTest {

    @Mock
    private TrainingRepository trainingRepository;

    private TraineeEntity trainee;
    private TrainerEntity trainer;
    private TrainingTypeEntity trainingType;

    /**
     * Sets up the test environment before each test method.
     * <p>
     * This method initializes instances of {@link TraineeEntity}, {@link TrainerEntity},
     * and {@link TrainingTypeEntity} to be used in the test cases.
     * The {@code trainee} and {@code trainer} objects are assigned valid IDs and
     * usernames to simulate realistic entities, while the {@code trainingType} is
     * assigned a name representing the type of training.
     * </p>
     *
     * <p>
     * This setup method ensures that each test case has a consistent starting state,
     * facilitating accurate and reliable unit tests for methods interacting with
     * these entities.
     * </p>
     */
    @BeforeEach
    public void setUp() {
        trainee = new TraineeEntity();
        trainee.setId(1L);
        trainee.setUsername("traineeUser");

        trainer = new TrainerEntity();
        trainer.setId(1L);
        trainer.setUsername("trainerUser");

        trainingType = new TrainingTypeEntity();
        trainingType.setId(1L);
        trainingType.setTrainingTypeName("Yoga");
    }

    @Test
    public void testFindTrainingsForTrainee() {
        LocalDateTime fromDate = LocalDateTime.now().minusDays(7);
        LocalDateTime toDate = LocalDateTime.now();
        TrainingEntity training = new TrainingEntity();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingType);
        training.setTrainingDate(LocalDateTime.now());

        // Mock
        when(trainingRepository.findTrainingsForTrainee(
                trainee.getId(), fromDate, toDate, null, trainingType))
                .thenReturn(List.of(training));

        // Call
        List<TrainingEntity> result = trainingRepository.findTrainingsForTrainee(
                trainee.getId(), fromDate, toDate, null, trainingType);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(trainee.getId(), result.get(0).getTrainee().getId());
        assertEquals(trainingType.getId(), result.get(0).getTrainingType().getId());
    }

    @Test
    public void testFindTrainingsForTrainer() {
        // Set
        LocalDateTime fromDate = LocalDateTime.now().minusDays(7);
        LocalDateTime toDate = LocalDateTime.now();
        TrainingEntity training = new TrainingEntity();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingType);
        training.setTrainingDate(LocalDateTime.now());

        // Mock
        when(trainingRepository.findTrainingsForTrainer(
                trainer.getId(), fromDate, toDate, "traineeUser"))
                .thenReturn(List.of(training));

        // Call
        List<TrainingEntity> result = trainingRepository.findTrainingsForTrainer(
                trainer.getId(), fromDate, toDate, "traineeUser");

        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(trainer.getId(), result.get(0).getTrainer().getId());
        assertEquals(trainingType.getId(), result.get(0).getTrainingType().getId());
    }
}
