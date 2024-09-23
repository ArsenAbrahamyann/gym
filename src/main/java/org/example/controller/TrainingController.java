package org.example.controller;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainingDto;
import org.example.entity.TrainingEntity;
import org.example.service.TrainingService;
import org.springframework.stereotype.Controller;

/**
 * Controller class responsible for handling training-related operations.
 * Acts as a facade between the client and the TrainingService.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class TrainingController {
    private final TrainingService trainingService;

    /**
     * Adds a new training session.
     *
     * @param trainingDto Data Transfer Object containing details about the training session.
     */
    public void addTraining(TrainingDto trainingDto) {
        log.info("Facade: Adding new training");
        trainingService.addTraining(trainingDto);
    }

    /**
     * Fetches the list of training sessions for a specific trainee within a specified date range,
     * optionally filtered by the trainer's name and the type of training.
     *
     * @param traineeName The name of the trainee for whom to fetch the trainings.
     * @param fromDate The start date of the training period.
     * @param toDate The end date of the training period.
     * @param trainerName Optional parameter to filter trainings by the trainer's name.
     * @param trainingType Optional parameter to filter trainings by the type of training.
     * @return A list of TrainingEntity objects representing the trainee's scheduled trainings.
     */
    public List<TrainingEntity> getTrainingsForTrainee(String traineeName,
                                                       LocalDateTime fromDate,
                                                       LocalDateTime toDate,
                                                       String trainerName,
                                                       String trainingType) {
        log.info("Facade: Fetching trainings for trainee {}", traineeName);
        return trainingService.getTrainingsForTrainee(traineeName, fromDate, toDate, trainerName, trainingType);
    }

    /**
     * Fetches the list of training sessions for a specific trainer within a specified date range,
     * optionally filtered by the trainee's name.
     *
     * @param trainerUsername The username of the trainer for whom to fetch the trainings.
     * @param fromDate The start date of the training period.
     * @param toDate The end date of the training period.
     * @param traineeName Optional parameter to filter trainings by the trainee's name.
     * @return A list of TrainingEntity objects representing the trainer's scheduled trainings.
     */
    public List<TrainingEntity> getTrainingsForTrainer(String trainerUsername,
                                                       LocalDateTime fromDate,
                                                       LocalDateTime toDate,
                                                       String traineeName) {
        log.info("Facade: Fetching trainings for trainer {}", trainerUsername);
        return trainingService.getTrainingsForTrainer(trainerUsername, fromDate, toDate, traineeName);
    }
}
