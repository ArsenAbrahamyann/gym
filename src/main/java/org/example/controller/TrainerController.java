package org.example.controller;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainerDto;
import org.example.entity.TrainingEntity;
import org.example.service.TrainerService;
import org.springframework.stereotype.Controller;

/**
 * Controller class responsible for handling trainer-related operations.
 * Acts as a facade between the client and the TrainerService.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class TrainerController {
    private final TrainerService trainerService;

    /**
     * Creates a new trainer profile.
     *
     * @param trainerDto Data Transfer Object containing trainer information.
     * @return The created TrainerDto object with the trainer's profile details.
     */
    public TrainerDto createTrainer(TrainerDto trainerDto) {
        log.info("Controller: Creating trainer");
        return trainerService.createTrainerProfile(trainerDto);
    }

    /**
     * Updates an existing trainer's profile by username.
     *
     * @param username The username of the trainer to update.
     * @param trainerDto Data Transfer Object containing updated trainer information.
     */
    public void updateTrainerProfile(String username, TrainerDto trainerDto) {
        log.info("Controller: Updating trainer profile for {}", username);
        trainerService.updateTrainerProfile(username, trainerDto);
    }

    /**
     * Changes the password for a trainer.
     *
     * @param username The username of the trainer whose password is to be changed.
     * @param newPassword The new password for the trainer.
     */
    public void changeTrainerPassword(String username, String newPassword) {
        log.info("Controller: Changing password for trainer {}", username);
        trainerService.changeTrainerPassword(username, newPassword);
    }

    /**
     * Toggles the active status of a trainer (e.g., active/inactive).
     *
     * @param username The username of the trainer whose status is to be toggled.
     */
    public void toggleTrainerStatus(String username) {
        log.info("Controller: Toggling status for trainer {}", username);
        trainerService.toggleTrainerStatus(username);
    }

    /**
     * Fetches the list of trainings for a trainer within a specified date range, optionally filtered by a trainee's name.
     *
     * @param trainerUsername The username of the trainer for whom to fetch the trainings.
     * @param fromDate The start date of the training period.
     * @param toDate The end date of the training period.
     * @param traineeName Optional parameter to filter trainings by trainee name.
     * @return A list of TrainingEntity objects representing the trainer's scheduled trainings.
     */
    public List<TrainingEntity> getTrainerTrainings(String trainerUsername,
                                                    LocalDateTime fromDate,
                                                    LocalDateTime toDate,
                                                    String traineeName) {
        log.info("Controller: Fetching trainings for trainer {}", trainerUsername);
        return trainerService.getTrainerTrainings(trainerUsername, fromDate, toDate, traineeName);
    }
}
