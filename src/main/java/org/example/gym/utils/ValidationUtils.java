package org.example.gym.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.ValidationException;
import org.example.gym.paylod.request.TraineeTrainingsRequestDto;
import org.example.gym.paylod.request.TrainerTrainingRequestDto;
import org.springframework.stereotype.Component;

/**
 * Utility class for performing various validation operations related to entities.
 * Includes methods for validating trainees, trainers, training entities, and user passwords,
 * as well as validating date ranges and criteria for fetching trainings.
 */
@Component
@Slf4j
public class ValidationUtils {
    private final String dataFormat = "yyyy-MM-dd";

    /**
     * Validates the fields of a TraineeEntity.
     *
     * @param trainee The TraineeEntity to validate.
     * @throws ValidationException if any required fields are missing or invalid.
     */
    public void validateTrainee(TraineeEntity trainee) {
        if (trainee.getUsername() == null || trainee.getUsername().isEmpty()) {
            throw new ValidationException("Trainee username is required.");
        }

        if (trainee == null || trainee.getPassword() == null
                || trainee.getPassword().isEmpty()) {
            throw new ValidationException("Trainee password is required.");
        }

        if (trainee.getAddress() == null || trainee.getAddress().isEmpty()) {
            throw new ValidationException("Trainee address is required.");
        }

        if (trainee.getIsActive() == null) {
            throw new ValidationException("Trainee active state (IsActive) must be provided.");
        }
    }

    /**
     * Validates the fields of a TrainerEntity.
     *
     * @param trainer The TrainerEntity to validate.
     * @throws ValidationException if any required fields are missing or invalid.
     */
    public void validateTrainer(TrainerEntity trainer) {
        if (trainer.getUsername() == null || trainer.getUsername().isEmpty()) {
            throw new ValidationException("Trainer username is required.");
        }

        if (trainer == null || trainer.getPassword() == null
                || trainer.getPassword().isEmpty()) {
            throw new ValidationException("Trainer password is required.");
        }

        if (trainer.getSpecialization() == null) {
            throw new ValidationException("Trainer specialization is required.");
        }

        if (trainer.getIsActive() == null) {
            throw new ValidationException("Trainer active state (IsActive) must be provided.");
        }
    }

    /**
     * Validates that the password provided matches the password stored for the user.
     *
     * @param user The UserEntity containing the stored password.
     * @param enteredPassword The password entered for authentication.
     * @throws ValidationException if the user does not exist or the passwords do not match.
     */
    public void validatePasswordMatch(UserEntity user, String enteredPassword) {
        if (user == null) {
            throw new ValidationException("User does not exist.");
        }

        if (user.getPassword().equals(enteredPassword)) {
            log.info("it is to same password");
        }
    }

    /**
     * Validates the fields of a TraineeEntity for update operations.
     *
     * @param trainee The TraineeEntity to validate.
     * @throws ValidationException if the TraineeEntity ID is missing or the entity is invalid.
     */
    public void validateUpdateTrainee(TraineeEntity trainee) {
        if (trainee.getId() == null) {
            throw new ValidationException("Trainee ID is required for updates.");
        }

        validateTrainee(trainee);
    }

    /**
     * Validates the fields of a TrainerEntity for update operations.
     *
     * @param trainer The TrainerEntity to validate.
     * @throws ValidationException if the TrainerEntity ID is missing or the entity is invalid.
     */
    public void validateUpdateTrainer(TrainerEntity trainer) {
        if (trainer.getId() == null) {
            throw new ValidationException("Trainer ID is required for updates.");
        }

        validateTrainer(trainer);
    }

    /**
     * Validates the fields of a TraineeEntity for activation or deactivation.
     * @ throws ValidationException if the TraineeEntity ID or active state is missing.
     */
    public void validateActivateDeactivateTrainee(TraineeEntity trainee) {
        if (trainee.getId() == null) {
            throw new ValidationException("Trainee ID is required for activation/deactivation.");
        }

        if (trainee.getIsActive() == null) {
            throw new ValidationException("Trainee active state must be provided.");
        }
    }

    /**
     * Validates the fields of a TrainerEntity for activation or deactivation.
     * @ throws ValidationException if the TrainerEntity ID or active state is missing.
     */
    public void validateActivateDeactivateTrainer(TrainerEntity trainer) {
        if (trainer.getId() == null) {
            throw new ValidationException("Trainer ID is required for activation/deactivation.");
        }

        if (trainer.getIsActive() == null) {
            throw new ValidationException("Trainer active state must be provided.");
        }
    }

    /**
     * Validates the fields of a TrainerEntity for activation or deactivation.
     * @ throws ValidationException if the TrainerEntity ID or active state is missing.
     */
    public void validateTraining(TrainingEntity training) {
        if (training.getTrainee() == null || training.getTrainee().getId() == null) {
            throw new ValidationException("Trainee must be selected for the training.");
        }

        if (training.getTrainer() == null || training.getTrainer().getId() == null) {
            throw new ValidationException("Trainer must be selected for the training.");
        }

        if (training.getTrainingType() == null || training.getTrainingType().getId() == null) {
            throw new ValidationException("Training type is required.");
        }

        if (training.getTrainingName() == null || training.getTrainingName().isEmpty()) {
            throw new ValidationException("Training name is required.");
        }

        if (training.getTrainingDate() == null) {
            throw new ValidationException("Training date is required.");
        }

        if (training.getTrainingDuration() == null || training.getTrainingDuration() <= 0) {
            throw new ValidationException("Training duration must be a positive number.");
        }
    }

    /**
     * Validates the criteria for fetching trainings for a trainee.
     *
     * @param traineeUsername The username of the trainee.
     * @param fromDate The start date for fetching trainings.
     * @param toDate The end date for fetching trainings.
     * @param trainerName Optional trainer name for filtering.
     * @param trainingType Optional training type for filtering.
     * @throws ValidationException if any criteria are invalid.
     */
    public void validateTraineeTrainingsCriteria(TraineeTrainingsRequestDto requestDto) {
        if (requestDto.getTraineeName() == null || requestDto.getTraineeName().isEmpty()) {
            throw new ValidationException("Trainee username is required for fetching training list.");
        }

        if (requestDto.getPeriodFrom() != null && requestDto.getPeriodFrom() != null) {
            validateDateFormat(requestDto.getPeriodFrom(), "From date");
        }

        if (requestDto.getPeriodTo() != null && requestDto.getPeriodTo() != null) {
            validateDateFormat(requestDto.getPeriodTo(), "To date");
        }

        if (requestDto.getPeriodFrom() != null && requestDto.getPeriodTo() != null) {
            validateDateRange(requestDto.getPeriodFrom(), requestDto.getPeriodTo());
        }

        if (requestDto.getTrainerName() != null && requestDto.getTrainerName().isEmpty()) {
            throw new ValidationException("Training name cannot be empty if provided.");
        }
    }

    /**
     * Validates the criteria for fetching trainings for a trainer.
     *
     * @throws ValidationException if any criteria are invalid.
     */
    public void validateTrainerTrainingsCriteria(TrainerTrainingRequestDto requestDto) {
        if (requestDto.getTrainerUsername() == null || requestDto.getTrainerUsername().isEmpty()) {
            throw new ValidationException("Trainer username is required for fetching training list.");
        }

        if (requestDto.getPeriodFrom() != null && requestDto.getPeriodFrom() != null) {
            validateDateFormat(requestDto.getPeriodFrom(), "From date");
        }

        if (requestDto.getPeriodTo() != null && requestDto.getPeriodTo() != null) {
            validateDateFormat(requestDto.getPeriodTo(), "To date");
        }

        if (requestDto.getPeriodFrom() != null && requestDto.getPeriodTo() != null) {
            validateDateRange(requestDto.getPeriodFrom(), requestDto.getPeriodTo());
        }

        if (requestDto.getTraineeName() != null && requestDto.getTraineeName().isEmpty()) {
            throw new ValidationException("Trainee name cannot be empty if provided.");
        }
    }

    /**
     * Validates that the 'from' date is before the 'to' date.
     *
     * @param fromDate The start date.
     * @param toDate The end date.
     * @throws ValidationException if the 'from' date is after the 'to' date or the dates are invalid.
     */
    void validateDateRange(LocalDateTime fromDate, LocalDateTime toDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(dataFormat);
        try {
            if (sdf.parse(String.valueOf(fromDate)).after(sdf.parse(String.valueOf(toDate)))) {
                throw new ValidationException("'From date' must be before 'To date'.");
            }
        } catch (ParseException e) {
            throw new ValidationException("Invalid date range.");
        }
    }

    /**
     * Validates that the date is in the correct format.
     *
     * @param date The date to validate.
     * @param fieldName The name of the field being validated.
     * @throws ValidationException if the date is not in the correct format.
     */
    private void validateDateFormat(LocalDateTime date, String fieldName) {
        SimpleDateFormat sdf = new SimpleDateFormat(dataFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(String.valueOf(date));
        } catch (ParseException e) {
            throw new ValidationException(fieldName
                    + " must be in the format "
                    + dataFormat);
        }
    }

    /**
     * Validates that a trainee and a list of trainers are provided for updating the trainee's trainer list.
     *
     * @param trainee The TraineeEntity to validate.
     * @param trainers The list of TrainerEntity to validate.
     * @throws ValidationException if the trainee ID or trainers list is missing.
     */
    public void validateUpdateTraineeTrainerList(TraineeEntity trainee, Set<TrainerEntity> trainers) {
        if (trainee == null || trainee.getId() == null) {
            throw new ValidationException("Trainee ID is required.");
        }
        if (trainers == null) {
            throw new ValidationException("Trainers ID is required.");
        }
    }
}
