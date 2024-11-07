package org.example.gym.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.example.gym.dto.request.TraineeTrainingsRequestDto;
import org.example.gym.dto.request.TrainerTrainingRequestDto;
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

    @Test
    public void testValidateTrainee_ValidTrainee_NoException() {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUsername("validUsername");
        trainee.setPassword("validPassword");
        trainee.setAddress("validAddress");
        trainee.setIsActive(true);

        validationUtils.validateTrainee(trainee);
    }

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

    @Test
    public void testValidateTrainer_ValidTrainer_NoException() {
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUsername("validTrainer");
        trainer.setPassword("password");
        trainer.setSpecialization(new TrainingTypeEntity());
        trainer.setIsActive(true);

        validationUtils.validateTrainer(trainer);
    }

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

    @Test
    public void testValidateTraineeTrainingsCriteria_ValidRequestDto_NoException() {
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto();
        requestDto.setTraineeName("validTrainee");
        requestDto.setPeriodFrom(LocalDateTime.now());
        requestDto.setPeriodTo(LocalDateTime.now().plusDays(1));

        validationUtils.validateTraineeTrainingsCriteria(requestDto);
    }

    @Test
    public void testValidateUpdateTraineeTrainerList_MissingTrainee_ExceptionThrown() {
        List<TrainerEntity> trainers = new ArrayList<>();
        trainers.add(new TrainerEntity());

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validationUtils.validateUpdateTraineeTrainerList(null, trainers);
        });
        assertEquals("Trainee ID is required.", exception.getMessage());
    }

    @Test
    public void testValidateUpdateTraineeTrainerList_ValidInputs_NoException() {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setId(1L);
        List<TrainerEntity> trainers = new ArrayList<>();
        trainers.add(new TrainerEntity());

        validationUtils.validateUpdateTraineeTrainerList(trainee, trainers);
    }

    @Test
    public void testValidateDateFormat_ValidFormat_NoException() {
        LocalDateTime validDate = LocalDateTime.now();
        validationUtils.validateDateFormat(validDate, "Valid date");
    }

    @Test
    public void testValidateTrainee_NullTrainee_ExceptionThrown() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validationUtils.validateTrainee(null);
        });
        assertEquals("Trainee not found", exception.getMessage());
    }

    @Test
    public void testValidateUpdateTraineeTrainerList_EmptyTrainersList_NoException() {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setId(1L);
        List<TrainerEntity> emptyTrainersList = new ArrayList<>();

        validationUtils.validateUpdateTraineeTrainerList(trainee, emptyTrainersList);
    }

    @Test
    public void testValidateTraineeTrainingsCriteria_MissingTraineeName_ExceptionThrown() {
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto();
        requestDto.setPeriodFrom(LocalDateTime.now().minusDays(1));
        requestDto.setPeriodTo(LocalDateTime.now());

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validationUtils.validateTraineeTrainingsCriteria(requestDto);
        });
        assertEquals("Trainee username is required for fetching training list.", exception.getMessage());
    }

    @Test
    public void testValidateTrainerTrainingsCriteria_NullDates_NoException() {
        TrainerTrainingRequestDto requestDto = new TrainerTrainingRequestDto();
        requestDto.setTrainerUsername("validTrainer");

        validationUtils.validateTrainerTrainingsCriteria(requestDto);
    }
}