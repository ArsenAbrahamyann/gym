package org.example.gym.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.HashSet;
import org.example.gym.dto.request.TraineeTrainingsRequestDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.exeption.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ValidationUtilsTest {

    private ValidationUtils validationUtils;

    /**
     * Setup method to initialize the ValidationUtils instance before each test.
     */
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

        // Should not throw exception
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
    public void testValidateTraineeTrainingsCriteria_ValidRequestDto_NoException() {
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto();
        requestDto.setTraineeName("validTrainee");
        requestDto.setPeriodFrom(LocalDateTime.now());
        requestDto.setPeriodTo(LocalDateTime.now().plusDays(1));

        // Should not throw exception
        validationUtils.validateTraineeTrainingsCriteria(requestDto);
    }

    @Test
    public void testValidateTraineeTrainingsCriteria_EmptyTraineeName_ExceptionThrown() {
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto();
        requestDto.setTraineeName("");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validationUtils.validateTraineeTrainingsCriteria(requestDto);
        });
        assertEquals("Trainee username is required for fetching training list.", exception.getMessage());
    }

    @Test
    public void testValidateDateRange_InvalidDate_ExceptionThrown() {
        LocalDateTime fromDate = LocalDateTime.now().plusDays(1);
        LocalDateTime toDate = LocalDateTime.now();

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validationUtils.validateDateRange(fromDate, toDate);
        });
        assertEquals("'From date' must be before 'To date'.", exception.getMessage());
    }

    @Test
    public void testValidateUpdateTrainee_ValidTrainee_NoException() {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setId(1L);
        trainee.setUsername("validUsername");
        trainee.setPassword("validPassword");
        trainee.setAddress("validAddress");
        trainee.setIsActive(true);

        // Should not throw exception
        validationUtils.validateUpdateTrainee(trainee);
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
    public void testValidateUpdateTraineeTrainerList_ValidInputs_NoException() {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setId(1L);
        HashSet<TrainerEntity> trainers = new HashSet<>();
        trainers.add(new TrainerEntity());

        // Should not throw exception
        validationUtils.validateUpdateTraineeTrainerList(trainee, trainers);
    }

    @Test
    public void testValidateUpdateTraineeTrainerList_MissingTrainee_ExceptionThrown() {
        HashSet<TrainerEntity> trainers = new HashSet<>();
        trainers.add(new TrainerEntity());

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validationUtils.validateUpdateTraineeTrainerList(null, trainers);
        });
        assertEquals("Trainee ID is required.", exception.getMessage());
    }

    @Test
    public void testValidateUpdateTraineeTrainerList_MissingTrainers_ExceptionThrown() {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setId(1L);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validationUtils.validateUpdateTraineeTrainerList(trainee, null);
        });
        assertEquals("Trainers ID is required.", exception.getMessage());
    }
}
