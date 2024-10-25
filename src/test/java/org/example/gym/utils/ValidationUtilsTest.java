package org.example.gym.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.HashSet;
import org.example.gym.dto.request.TraineeTrainingsRequestDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.exeption.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ValidationUtilsTest {

    private ValidationUtils validationUtils;

    @BeforeEach
    public void setUp() {
        validationUtils = new ValidationUtils();
    }

    // Test for a valid Trainee entity
    @Test
    public void testValidateTrainee_ValidTrainee_NoException() {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUsername("validUsername");
        trainee.setPassword("validPassword");
        trainee.setAddress("validAddress");
        trainee.setIsActive(true);

        validationUtils.validateTrainee(trainee);
    }

    // Test for a missing Trainee username
    @Test
    public void testValidateTrainee_EmptyUsername_ExceptionThrown() {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUsername("");
        trainee.setPassword("validPassword");
        trainee.setAddress("validAddress");
        trainee.setIsActive(true);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validationUtils.validateTrainee(trainee);
        });
        assertEquals("Trainee username is required.", exception.getMessage());
    }

    // Additional test: null Trainee password
    @Test
    public void testValidateTrainee_NullPassword_ExceptionThrown() {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUsername("validUsername");
        trainee.setPassword(null);
        trainee.setAddress("validAddress");
        trainee.setIsActive(true);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validationUtils.validateTrainee(trainee);
        });
        assertEquals("Trainee password is required.", exception.getMessage());
    }

    // Test for Trainer validation with valid fields
    @Test
    public void testValidateTrainer_ValidTrainer_NoException() {
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUsername("validTrainer");
        trainer.setPassword("password");
        trainer.setSpecialization(new TrainingTypeEntity());
        trainer.setIsActive(true);

        validationUtils.validateTrainer(trainer);
    }

    // Test for Trainer with empty specialization
    @Test
    public void testValidateTrainer_EmptySpecialization_ExceptionThrown() {
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUsername("validTrainer");
        trainer.setPassword("password");
        trainer.setSpecialization(null);
        trainer.setIsActive(true);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validationUtils.validateTrainer(trainer);
        });
        assertEquals("Trainer specialization is required.", exception.getMessage());
    }

    // Date validation tests
    @Test
    public void testValidateDateRange_InvalidDateRange_ExceptionThrown() {
        LocalDateTime fromDate = LocalDateTime.now().plusDays(1);
        LocalDateTime toDate = LocalDateTime.now();

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validationUtils.validateDateRange(fromDate, toDate);
        });
        assertEquals("'From date' must be before 'To date'.", exception.getMessage());
    }

    @Test
    public void testValidateDateRange_ValidDateRange_NoException() {
        LocalDateTime fromDate = LocalDateTime.now();
        LocalDateTime toDate = LocalDateTime.now().plusDays(1);

        validationUtils.validateDateRange(fromDate, toDate);
    }

    @Test
    public void testValidateDateFormat_InvalidFormat_ExceptionThrown() {
        LocalDateTime invalidDate = null;

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validationUtils.validateDateFormat(invalidDate, "Invalid date");
        });
        assertEquals("Invalid date must be in the format yyyy-MM-dd", exception.getMessage());
    }

    // Validating update for Trainer
    @Test
    public void testValidateUpdateTrainer_ValidTrainer_NoException() {
        TrainerEntity trainer = new TrainerEntity();
        trainer.setId(1L);
        trainer.setUsername("validTrainer");
        trainer.setPassword("password");
        trainer.setSpecialization(new TrainingTypeEntity());
        trainer.setIsActive(true);

        validationUtils.validateUpdateTrainer(trainer);
    }

    // Test for validateUpdateTrainee with missing ID
    @Test
    public void testValidateUpdateTrainee_MissingId_ExceptionThrown() {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUsername("validUsername");
        trainee.setPassword("validPassword");
        trainee.setAddress("validAddress");
        trainee.setIsActive(true);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validationUtils.validateUpdateTrainee(trainee);
        });
        assertEquals("Trainee ID is required for updates.", exception.getMessage());
    }

    // Test for validating TraineeTrainingsCriteria with valid DTO
    @Test
    public void testValidateTraineeTrainingsCriteria_ValidRequestDto_NoException() {
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto();
        requestDto.setTraineeName("validTrainee");
        requestDto.setPeriodFrom(LocalDateTime.now());
        requestDto.setPeriodTo(LocalDateTime.now().plusDays(1));

        validationUtils.validateTraineeTrainingsCriteria(requestDto);
    }

    // Validation of updating Trainee trainer list with missing trainee ID
    @Test
    public void testValidateUpdateTraineeTrainerList_MissingTrainee_ExceptionThrown() {
        HashSet<TrainerEntity> trainers = new HashSet<>();
        trainers.add(new TrainerEntity());

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validationUtils.validateUpdateTraineeTrainerList(null, trainers);
        });
        assertEquals("Trainee ID is required.", exception.getMessage());
    }

    // Additional test: Validating UpdateTraineeTrainerList with valid inputs
    @Test
    public void testValidateUpdateTraineeTrainerList_ValidInputs_NoException() {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setId(1L);
        HashSet<TrainerEntity> trainers = new HashSet<>();
        trainers.add(new TrainerEntity());

        validationUtils.validateUpdateTraineeTrainerList(trainee, trainers);
    }
}
