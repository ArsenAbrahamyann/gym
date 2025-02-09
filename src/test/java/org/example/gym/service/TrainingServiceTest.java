package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.example.gym.dto.request.AddTrainingRequestDto;
import org.example.gym.dto.request.TraineeTrainingsRequestDto;
import org.example.gym.dto.request.TrainerTrainingRequestDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.TrainingNotFoundException;
import org.example.gym.repository.TrainingRepository;
import org.example.gym.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
    @Mock private TrainingRepository trainingRepository;
    @Mock private TraineeService traineeService;
    @Mock private TrainerService trainerService;
    @Mock private ValidationUtils validationUtils;

    @InjectMocks private TrainingService trainingService;

    private TrainingEntity trainingEntity;
    private TraineeEntity traineeEntity;
    private TrainerEntity trainerEntity;


    /**
     * Initializes mock entities for testing purposes.
     *
     * <p>This method sets up the initial state required for unit tests by creating and configuring mock
     * instances of {@code UserEntity}, {@code TraineeEntity}, {@code TrainerEntity}, {@code TrainingTypeEntity},
     * and {@code TrainingEntity}. These mock objects simulate real entities and their relationships to isolate
     * and test the functionality of the system under test without relying on external dependencies.</p>
     *
     * <p>The method performs the following initialization steps:</p>
     * <ul>
     *     <li>Creates a {@code UserEntity} with a sample username ("traineeUsername") and assigns it to a {@code TraineeEntity}.</li>
     *     <li>Creates a {@code TrainerEntity} with a sample ID (1L).</li>
     *     <li>Creates a {@code TrainingTypeEntity} with a training type name ("Yoga").</li>
     *     <li>Creates a {@code TrainingEntity} and sets the mock trainee, trainer, and training type entities.</li>
     * </ul>
     *
     * <p>This method is executed before each test to ensure a consistent and isolated environment for each test case.</p>
     */
    @BeforeEach
    public void setUp() {
        UserEntity traineeUser = new UserEntity();
        traineeUser.setUsername("traineeUsername");

        traineeEntity = new TraineeEntity();
        traineeEntity.setUser(traineeUser);

        UserEntity trainerUser = new UserEntity();
        trainerUser.setUsername("trainerUsername");

        trainerEntity = new TrainerEntity();
        trainerEntity.setId(1L);
        trainerEntity.setUser(trainerUser);

        TrainingTypeEntity trainingType = new TrainingTypeEntity();
        trainingType.setTrainingTypeName("Yoga");

        trainingEntity = new TrainingEntity();
        trainingEntity.setTrainee(traineeEntity);
        trainingEntity.setTrainer(trainerEntity);
        trainingEntity.setTrainingType(trainingType);
    }

    @Test
    public void testAddTraining_InvalidTrainee() {
        AddTrainingRequestDto requestDto = new AddTrainingRequestDto();
        requestDto.setTraineeUsername("invalidTrainee");

        when(traineeService.getTrainee("invalidTrainee")).thenThrow(new IllegalArgumentException("Invalid Trainee"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                trainingService.addTraining(requestDto));

        assertEquals("Invalid Trainee", exception.getMessage());
        verify(traineeService, times(1)).getTrainee("invalidTrainee");
        verifyNoInteractions(trainingRepository);
    }

    @Test
    public void testGetTrainingsForTrainee_ValidData() {
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto();
        requestDto.setTraineeName("traineeUsername");

        when(trainingRepository.findTrainingsForTrainee(any(), any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(trainingEntity));

        List<TrainingEntity> trainings = trainingService.getTrainingsForTrainee(requestDto);

        assertEquals(1, trainings.size());
        assertEquals("traineeUsername", trainings.get(0).getTrainee().getUser().getUsername());
        verify(validationUtils, times(1)).validateTraineeTrainingsCriteria(requestDto);
    }

    @Test
    public void testGetTrainingsForTrainee_NoTrainingsFound() {
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto();
        requestDto.setTraineeName("traineeUsername");

        when(trainingRepository.findTrainingsForTrainee(any(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        TrainingNotFoundException exception = assertThrows(TrainingNotFoundException.class, () ->
                trainingService.getTrainingsForTrainee(requestDto));

        assertEquals("No trainings found for the specified criteria.", exception.getMessage());
        verify(validationUtils, times(1)).validateTraineeTrainingsCriteria(requestDto);
    }




    @Test
    public void testGetTrainingsForTrainer_InvalidTrainer() {
        TrainerTrainingRequestDto requestDto = new TrainerTrainingRequestDto();
        requestDto.setTrainerUsername("invalidTrainer");

        when(trainerService.getTrainer("invalidTrainer")).thenThrow(new IllegalArgumentException("Invalid Trainer"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                trainingService.getTrainingsForTrainer(requestDto));

        assertEquals("Invalid Trainer", exception.getMessage());
        verifyNoInteractions(trainingRepository);
    }

}