package org.example.utils;

import java.time.Duration;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValidationUtilsTest {
    @Mock
    private TrainerService trainerService;

    @Mock
    private TraineeService traineeService;

    @InjectMocks
    private ValidationUtils validationUtils;

    @Test
    public void testValidateBirthDate_ValidDate() {
        boolean result = validationUtils.validateBirthDate("2000-01-01");
        assertThat(result).isTrue();
    }

    @Test
    public void testValidateBirthDate_FutureDate() {
        boolean result = validationUtils.validateBirthDate("2100-01-01");
        assertThat(result).isFalse();
    }

    @Test
    public void testValidateBirthDate_InvalidFormat() {
        boolean result = validationUtils.validateBirthDate("01-01-2000");
        assertThat(result).isFalse();
    }

    @Test
    public void testValidateTrainingDuration_ValidFormat() {
        Duration result = validationUtils.validateTrainingDuration("01:30");
        assertThat(result).isEqualTo(Duration.ofHours(1).plusMinutes(30));
    }

    @Test
    public void testValidateTrainingDuration_InvalidValues() {
        IllegalArgumentException thrown = org.assertj.core.api.Assertions.catchThrowableOfType(() ->
                validationUtils.validateTrainingDuration("01:60"), IllegalArgumentException.class);
        assertThat(thrown).isNotNull().hasMessageContaining("Invalid duration values.");
    }

    @Test
    public void testValidateTraineeExists_TraineeFound() {
        TraineeEntity trainee = new TraineeEntity(); // Create a dummy trainee entity
        when(traineeService.getTrainee("validUser")).thenReturn(trainee);

        TraineeEntity result = validationUtils.validateTraineeExists("validUser");
        assertThat(result).isNotNull();
    }

    @Test
    public void testValidateTraineeExists_TraineeNotFound() {
        when(traineeService.getTrainee("invalidUser")).thenReturn(null);

        TraineeEntity result = validationUtils.validateTraineeExists("invalidUser");
        assertThat(result).isNull();
    }

    @Test
    public void testValidateTrainerExists_TrainerFound() {
        TrainerEntity trainer = new TrainerEntity(); // Create a dummy trainer entity
        when(trainerService.getTrainer("validUser")).thenReturn(trainer);

        TrainerEntity result = validationUtils.validateTrainerExists("validUser");
        assertThat(result).isNotNull();
    }

    @Test
    public void testValidateTrainerExists_TrainerNotFound() {
        when(trainerService.getTrainer("invalidUser")).thenReturn(null);

        TrainerEntity result = validationUtils.validateTrainerExists("invalidUser");
        assertThat(result).isNull();
    }

    @Test
    public void testIsValidBoolean_ValidTrue() {
        boolean result = validationUtils.isValidBoolean("true");
        assertThat(result).isTrue();
    }

    @Test
    public void testIsValidBoolean_ValidFalse() {
        boolean result = validationUtils.isValidBoolean("false");
        assertThat(result).isTrue();
    }

    @Test
    public void testIsValidBoolean_InvalidValue() {
        boolean result = validationUtils.isValidBoolean("notABoolean");
        assertThat(result).isFalse();
    }

    @Test
    public void testIsValidBoolean_NullValue() {
        boolean result = validationUtils.isValidBoolean(null);
        assertThat(result).isFalse();
    }
}
