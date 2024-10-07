package org.example.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeDto;
import org.example.entity.TrainerEntity;
import org.example.service.TraineeService;
import org.springframework.stereotype.Controller;

/**
 * This controller handles the management of trainees, such as creating trainee profiles,
 * updating trainee information, assigning trainers to trainees, and more.
 * It serves as a facade between the client and the {@link TraineeService}.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class TraineeController {
    private final TraineeService traineeService;

    /**
     * Creates a new trainee profile based on the provided {@link TraineeDto}.
     *
     * @param traineeDto the data transfer object containing trainee details
     * @return the created trainee profile as a {@link TraineeDto}
     */
    public TraineeDto createTrainee(TraineeDto traineeDto) {
        log.info("Controller: Creating trainee");
        return traineeService.createTraineeProfile(traineeDto);
    }

    /**
     * Updates the profile of an existing trainee identified by the provided username.
     *
     * @param username the username of the trainee whose profile is being updated
     * @param traineeDto the data transfer object containing updated trainee information
     */
    public void updateTraineeProfile(String username, TraineeDto traineeDto) {
        log.info("Controller: Updating trainee profile for {}", username);
        traineeService.updateTraineeProfile(username, traineeDto);
    }

    /**
     * Changes the password of the trainee identified by the given username.
     *
     * @param username the username of the trainee whose password is being changed
     * @param newPassword the new password for the trainee
     */
    public void changeTraineePassword(String username, String newPassword) {
        log.info("Controller: Changing password for trainee {}", username);
        traineeService.changeTraineePassword(username, newPassword);
    }

    /**
     * Toggles the active status of the trainee. This may activate or deactivate the trainee
     * depending on their current state.
     *
     * @param username the username of the trainee whose status is being toggled
     */
    public void toggleTraineeStatus(String username) {
        log.info("Controller: Toggling status for trainee {}", username);
        traineeService.toggleTraineeStatus(username);
    }

    /**
     * Deletes the trainee identified by the given username from the system.
     *
     * @param username the username of the trainee to be deleted
     */
    public void deleteTrainee(String username) {
        log.info("Controller: Deleting trainee {}", username);
        traineeService.deleteTraineeByUsername(username);
    }

    /**
     * Retrieves a list of trainers who are not currently assigned to the trainee.
     *
     * @param username the username of the trainee
     * @return a list of unassigned trainers as {@link TrainerEntity} objects
     */
    public List<TrainerEntity> getUnassignedTrainers(String username) {
        log.info("Controller: Fetching unassigned trainers for trainee {}", username);
        return traineeService.getUnassignedTrainers(username);
    }

    /**
     * Assigns a list of trainers to the trainee identified by the given username.
     *
     * @param username the username of the trainee
     * @param trainerIds a list of trainer IDs to be assigned to the trainee
     */
    public void assignTrainersToTrainee(String username, List<Long> trainerIds) {
        log.info("Controller: Assigning trainers {} to trainee {}", trainerIds, username);
        traineeService.updateTraineeTrainers(username, trainerIds);
    }
}
