package org.example.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.UserEntity;
import org.example.exeption.ValidationException;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class ValidationUtils {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public void validateTrainee(TraineeEntity trainee) {
        if (trainee.getUser().getUsername() == null || trainee.getUser().getUsername().isEmpty()) {
            throw new ValidationException("Trainee username is required.");
        }

        if (trainee.getUser() == null || trainee.getUser().getPassword() == null || trainee.getUser().getPassword().isEmpty()) {
            throw new ValidationException("Trainee password is required.");
        }

        if (trainee.getAddress() == null || trainee.getAddress().isEmpty()) {
            throw new ValidationException("Trainee address is required.");
        }

        if (trainee.getUser().getIsActive() == null) {
            throw new ValidationException("Trainee active state (IsActive) must be provided.");
        }
    }

    public void validateTrainer(TrainerEntity trainer) {
        if (trainer.getUser().getUsername() == null || trainer.getUser().getUsername().isEmpty()) {
            throw new ValidationException("Trainer username is required.");
        }

        if (trainer.getUser() == null || trainer.getUser().getPassword() == null || trainer.getUser().getPassword().isEmpty()) {
            throw new ValidationException("Trainer password is required.");
        }

        if (trainer.getSpecialization() == null) {
            throw new ValidationException("Trainer specialization is required.");
        }

        if (trainer.getUser().getIsActive() == null) {
            throw new ValidationException("Trainer active state (IsActive) must be provided.");
        }
    }

    public void validatePasswordMatch(UserEntity user, String enteredPassword) {
        if (user == null) {
            throw new ValidationException("User does not exist.");
        }

        if (!user.getPassword().equals(enteredPassword)) {
            throw new ValidationException("Invalid password. Please try again.");
        }
    }

    public void validateUpdateTrainee(TraineeEntity trainee) {
        if (trainee.getId() == null) {
            throw new ValidationException("Trainee ID is required for updates.");
        }

        validateTrainee(trainee);
    }

    public void validateUpdateTrainer(TrainerEntity trainer) {
        if (trainer.getId() == null) {
            throw new ValidationException("Trainer ID is required for updates.");
        }

        validateTrainer(trainer);
    }

    public void validateActivateDeactivateTrainee(TraineeEntity trainee) {
        if (trainee.getId() == null) {
            throw new ValidationException("Trainee ID is required for activation/deactivation.");
        }

        if (trainee.getUser().getIsActive() == null) {
            throw new ValidationException("Trainee active state must be provided.");
        }
    }

    public void validateActivateDeactivateTrainer(TrainerEntity trainer) {
        if (trainer.getId() == null) {
            throw new ValidationException("Trainer ID is required for activation/deactivation.");
        }

        if (trainer.getUser().getIsActive() == null) {
            throw new ValidationException("Trainer active state must be provided.");
        }
    }

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


    public void validateTraineeTrainingsCriteria(String traineeUsername, String fromDate, String toDate, String trainerName, String trainingType) {
        if (traineeUsername == null || traineeUsername.isEmpty()) {
            throw new ValidationException("Trainee username is required for fetching training list.");
        }

        if (fromDate != null && !fromDate.isEmpty()) {
            validateDateFormat(fromDate, "From date");
        }

        if (toDate != null && !toDate.isEmpty()) {
            validateDateFormat(toDate, "To date");
        }

        if (fromDate != null && toDate != null) {
            validateDateRange(fromDate, toDate);
        }

        if (trainerName != null && trainerName.isEmpty()) {
            throw new ValidationException("Trainer name cannot be empty if provided.");
        }

        if (trainingType != null && trainingType.isEmpty()) {
            throw new ValidationException("Training type cannot be empty if provided.");
        }
    }

    public void validateTrainerTrainingsCriteria(String trainerUsername, String fromDate, String toDate, String traineeName) {
        if (trainerUsername == null || trainerUsername.isEmpty()) {
            throw new ValidationException("Trainer username is required for fetching training list.");
        }

        if (fromDate != null && !fromDate.isEmpty()) {
            validateDateFormat(fromDate, "From date");
        }

        if (toDate != null && !toDate.isEmpty()) {
            validateDateFormat(toDate, "To date");
        }

        if (fromDate != null && toDate != null) {
            validateDateRange(fromDate, toDate);
        }

        if (traineeName != null && traineeName.isEmpty()) {
            throw new ValidationException("Trainee name cannot be empty if provided.");
        }
    }

    private void validateDateFormat(String date, String fieldName) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);
        try {
            sdf.parse(date);
        } catch (ParseException e) {
            throw new ValidationException(fieldName + " must be in the format " + DATE_FORMAT);
        }
    }

    private void validateDateRange(String fromDate, String toDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            if (sdf.parse(fromDate).after(sdf.parse(toDate))) {
                throw new ValidationException("'From date' must be before 'To date'.");
            }
        } catch (ParseException e) {
            throw new ValidationException("Invalid date range.");
        }
    }

    public void validateUpdateTraineeTrainerList(TraineeEntity trainee, TrainerEntity trainer) {
        if (trainee == null || trainee.getId() == null) {
            throw new ValidationException("Trainee ID is required.");
        }

        if (trainer == null || trainer.getId() == null) {
            throw new ValidationException("Trainer ID is required.");
        }
    }
}
