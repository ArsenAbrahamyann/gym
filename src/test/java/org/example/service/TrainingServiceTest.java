package org.example.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.example.dto.TrainingDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.exeption.ResourceNotFoundException;
import org.example.exeption.ValidationException;
import org.example.repository.TraineeRepository;
import org.example.repository.TrainerRepository;
import org.example.repository.TrainingRepository;
import org.example.repository.TrainingTypeRepository;
import org.example.repository.UserRepository;
import org.example.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TrainingService trainingService;

    private TraineeEntity trainee;
    private TrainerEntity trainer;
    private TrainingEntity trainingEntity;
    private TrainingDto trainingDto;

    /**
     * Initializes the test data before each test case.
     * This method is annotated with {@code @BeforeEach}, meaning it runs before each test method in the class.
     * It sets up the required objects for testing, including {@code TraineeEntity}, {@code TrainerEntity},
     * {@code TrainingEntity}, and {@code TrainingDto}, and assigns their respective fields with test values.
     *
     * <p>Specifically, it performs the following steps:
     * <ul>
     *     <li>Creates a {@code TraineeEntity} object with an ID of 1L.</li>
     *     <li>Creates a {@code TrainerEntity} object with an ID of 1L.</li>
     *     <li>Initializes a {@code TrainingEntity} object, setting its trainee, trainer, and training name to
     *         "Test Training".</li>
     *     <li>Creates a {@code TrainingDto} object, setting the trainee and trainer IDs to 1L, the training name
     *         to "Test Training", the training type ID to 1L, and the duration to 60 minutes.</li>
     * </ul>
     */
    @BeforeEach
    public void setUp() {
        trainee = new TraineeEntity();
        trainee.setId(1L);

        trainer = new TrainerEntity();
        trainer.setId(1L);

        trainingEntity = new TrainingEntity();
        trainingEntity.setTrainee(trainee);
        trainingEntity.setTrainer(trainer);
        trainingEntity.setTrainingName("Test Training");

        trainingDto = new TrainingDto();
        trainingDto.setTraineeId(1L);
        trainingDto.setTrainerId(1L);
        trainingDto.setTrainingName("Test Training");
        trainingDto.setTrainingTypeId(1L);
        trainingDto.setTrainingDuration(60);
    }

    @Test
    public void testAddTraining_Success() {
        Long traineeId = 1L;
        Long trainerId = 2L;
        Long trainingTypeId = 3L;
        TrainingDto trainingDto = new TrainingDto(traineeId, trainerId, "Training Name", trainingTypeId, 60);

        TraineeEntity trainee = new TraineeEntity();
        TrainerEntity trainer = new TrainerEntity();
        TrainingEntity trainingEntity = new TrainingEntity();

        // Mock repository behavior
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(trainee));
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(trainer));
        when(trainingTypeRepository.findById(trainingTypeId)).thenReturn(Optional.of(new TrainingTypeEntity()));
        doNothing().when(trainingRepository).save(any(TrainingEntity.class));

        // Act
        trainingService.addTraining(trainingDto);

        // Assert
        verify(traineeRepository).findById(traineeId);
        verify(trainerRepository).findById(trainerId);
        verify(trainingTypeRepository).findById(trainingTypeId);
        verify(trainingRepository).save(any(TrainingEntity.class));
    }

    @Test
    public void testAddTraining_TraineeNotFound() {
        when(traineeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> trainingService.addTraining(trainingDto))
                .withMessageContaining("Trainee not found");
    }

    @Test
    public void testGetTrainingsForTrainee_Success() {
        when(traineeRepository.findByTraineeFromUsername(any())).thenReturn(Optional.of(trainee));
        when(trainingRepository.findTrainingsForTrainee(anyLong(), any(), any(), any(), any()))
                .thenReturn(Optional.of(List.of(trainingEntity)));

        List<TrainingEntity> result = trainingService.getTrainingsForTrainee("traineeName",
                LocalDateTime.now(), LocalDateTime.now(), "trainerName", "trainingType");

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isEqualTo(trainingEntity);
    }

    @Test
    public void testGetTrainingsForTrainee_TraineeNotFound() {
        when(traineeRepository.findByTraineeFromUsername(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> trainingService.getTrainingsForTrainee("traineeName",
                        LocalDateTime.now(), LocalDateTime.now(), "trainerName", "trainingType"))
                .withMessageContaining("Trainee not found");
    }

    @Test
    public void testGetTrainingsForTrainee_TrainingsNotFound() {
        when(traineeRepository.findByTraineeFromUsername(any())).thenReturn(Optional.of(trainee));
        when(trainingRepository.findTrainingsForTrainee(anyLong(), any(), any(), any(),
                any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> trainingService.getTrainingsForTrainee("traineeName",
                        LocalDateTime.now(), LocalDateTime.now(), "trainerName", "trainingType"))
                .withMessageContaining("Trainings not found");
    }

    @Test
    public void testGetTrainingsForTrainer_Success() {
        when(trainerRepository.findByTrainerFromUsername(any())).thenReturn(Optional.of(trainer));
        when(trainingRepository.findTrainingsForTrainer(anyLong(), any(), any(), any()))
                .thenReturn(Optional.of(List.of(trainingEntity)));

        List<TrainingEntity> result = trainingService.getTrainingsForTrainer("trainerUsername",
                LocalDateTime.now(), LocalDateTime.now(), "traineeName");

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isEqualTo(trainingEntity);
    }

    @Test
    public void testGetTrainingsForTrainer_TrainerNotFound() {
        when(trainerRepository.findByTrainerFromUsername(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> trainingService.getTrainingsForTrainer("trainerUsername",
                        LocalDateTime.now(), LocalDateTime.now(), "traineeName"))
                .withMessageContaining("Trainer not found");
    }

    @Test
    public void testGetTrainingsForTrainer_TrainingsNotFound() {
        when(trainerRepository.findByTrainerFromUsername(any())).thenReturn(Optional.of(trainer));
        when(trainingRepository.findTrainingsForTrainer(anyLong(), any(), any(), any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> trainingService.getTrainingsForTrainer("trainerUsername",
                        LocalDateTime.now(), LocalDateTime.now(), "traineeName"))
                .withMessageContaining("Trainings not found");
    }
}
