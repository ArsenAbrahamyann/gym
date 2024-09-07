package org.example.utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.springframework.stereotype.Service;

/**
 * Utility class for validation of various inputs and entities.
 * <p>
 * This class provides methods for validating birth dates, training durations, and the existence of trainees
 * and trainers. It uses services to interact with the persistence layer and provides validation feedback.
 * </p>
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ValidationUtils {

    private final TrainerService trainerService;
    private final TraineeService traineeService;

    /**
     * Validates the format and values of the given birth date string.
     * <p>
     * The birth date string must be in the format YYYY-MM-DD. The method also ensures that the birth date is not
     * in the future.
     * </p>
     *
     * @param birthDateStr the birth date string to validate
     * @return true if the birth date is valid, otherwise false
     */
    public boolean validateBirthDate(String birthDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate birthDate = LocalDate.parse(birthDateStr, formatter);

            if (birthDate.isAfter(LocalDate.now())) {
                log.warn("Birthdate cannot be in the future: {}", birthDateStr);
                return false;
            }

            return true;
        } catch (DateTimeParseException e) {
            log.warn("Invalid date format: {}", birthDateStr);
            return false;
        }
    }

    /**
     * Validates the format of the training duration input.
     * <p>
     * The input should be in HH:MM format, where HH represents hours and MM represents minutes. The method
     * parses the input and returns a {@link Duration} object if valid.
     * </p>
     *
     * @param trainingDurationInput the input string representing the duration
     * @return the parsed {@link Duration} if the input is valid
     * @throws IllegalArgumentException if the input is invalid
     */
    public Duration validateTrainingDuration(String trainingDurationInput) {
        try {
            String[] parts = trainingDurationInput.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Duration must be in HH:MM format.");
            }
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            if (hours < 0 || minutes < 0 || minutes >= 60) {
                throw new IllegalArgumentException(
                        "Invalid duration values. Hours must be >= 0 and minutes between 0 and 59.");
            }

            return Duration.ofHours(hours).plusMinutes(minutes);
        } catch (NumberFormatException e) {
            log.warn("Invalid training duration format: {}", trainingDurationInput);
            throw new IllegalArgumentException("Invalid duration format. Please use HH:MM.");
        }
    }

    /**
     * Validates if a trainee exists based on the given username.
     * <p>
     * This method checks if a trainee with the specified username exists in the system. If not found, it logs
     * a warning and returns null.
     * </p>
     *
     * @param traineeUsername the username of the trainee to validate
     * @return the {@link TraineeEntity} if found, otherwise null
     */
    public TraineeEntity validateTraineeExists(String traineeUsername) {
        TraineeEntity trainee = traineeService.getTrainee(traineeUsername);
        if (trainee == null) {
            log.warn("Trainee not found with username: {}", traineeUsername);
            return null;
        }
        return trainee;
    }

    /**
     * Validates if a trainer exists based on the given username.
     * <p>
     * This method checks if a trainer with the specified username exists in the system. If not found, it logs
     * a warning and returns null.
     * </p>
     *
     * @param trainerUsername the username of the trainer to validate
     * @return the {@link TrainerEntity} if found, otherwise null
     */
    public TrainerEntity validateTrainerExists(String trainerUsername) {
        TrainerEntity trainer = trainerService.getTrainer(trainerUsername);
        if (trainer == null) {
            log.warn("Trainer not found with username: {}", trainerUsername);
            return null;
        }
        return trainer;
    }

    /**
     * Validates if the given string can be parsed into a boolean value.
     *
     * @param input the string to validate
     * @return true if the input is a valid boolean representation, otherwise false
     */
    public boolean isValidBoolean(String input) {
        if (input == null) {
            log.error("Input is null.");
            return false;
        }
        String trimmedInput = input.trim().toLowerCase();
        boolean isValid = trimmedInput.equals("true")
                || trimmedInput.equals("false");
        if (! isValid) {
            log.error("Invalid boolean input: {}", input);
        }
        return isValid;
    }
}
