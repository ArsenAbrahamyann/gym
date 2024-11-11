package org.example.gym.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.ActivateRequestDto;
import org.example.gym.dto.request.UpdateTraineeRequestDto;
import org.example.gym.dto.request.UpdateTraineeTrainerListRequestDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.TraineeNotFoundException;
import org.example.gym.repository.TraineeRepository;
import org.example.gym.utils.UserUtils;
import org.example.gym.utils.ValidationUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing Trainee profiles and related operations.
 * This class provides functionalities to create, update, delete, and manage trainee profiles and their associations.
 */
@Service
@Slf4j
public class TraineeService {
    private final TraineeRepository traineeRepository;
    private final TrainerService trainerService;
    private final ValidationUtils validationUtils;
    private final UserUtils userUtils;
    private final UserService userService;


    /**
     * Constructs a new {@code TraineeService} instance with dependencies injected for managing trainees.
     *
     * @param trainerService   a {@link TrainerService} instance for handling trainer-related operations.
     *                         The {@code @Lazy} annotation is used to avoid circular dependency issues.
     * @param traineeRepository a {@link TraineeRepository} instance for accessing and managing {@code Trainee} entities.
     * @param validationUtils   a {@link ValidationUtils} instance for performing validation checks on input data.
     * @param userUtils         a {@link UserUtils} instance for generating usernames and passwords for trainees.
     * @param userService       a {@link UserService} instance for managing user-related operations.
     *                         The {@code @Lazy} annotation is used to avoid circular dependency issues.
     */
    public TraineeService(@Lazy TrainerService trainerService, TraineeRepository traineeRepository,
                          ValidationUtils validationUtils, UserUtils userUtils, @Lazy UserService userService) {
        this.trainerService = trainerService;
        this.traineeRepository = traineeRepository;
        this.validationUtils = validationUtils;
        this.userUtils = userUtils;
        this.userService = userService;
    }

    /**
     * Creates a new trainee profile and generates username and password.
     *
     * @param trainee The trainee trainee containing profile information.
     * @return The created TraineeEntity.
     */
    @Transactional
    public TraineeEntity createTraineeProfile(TraineeEntity trainee) {
        log.info("Creating trainee profile");
        UserEntity user = trainee.getUser();
        userService.save(user);
        traineeRepository.save(trainee);
        log.info("Trainee profile created successfully for {}", trainee.getUser().getUsername());
        return trainee;
    }

    /**
     * Toggles the active status of a trainee.
     *
     * @param requestDto The request containing the username and new active status.
     */
    @Transactional
    public void toggleTraineeStatus(ActivateRequestDto requestDto) {
        log.info("Toggling trainee status for {}", requestDto.getUsername());
        TraineeEntity trainee = getTrainee(requestDto.getUsername());
        UserEntity user = trainee.getUser();
        user.setIsActive(requestDto.isActive());
        UserEntity save = userService.save(user);
        trainee.setUser(save);
        traineeRepository.save(trainee);
        log.info("Trainee status toggled successfully for {}", requestDto.getUsername());
    }

    /**
     * Retrieves a list of unassigned trainers for the specified trainee.
     *
     * @param traineeUsername The username of the trainee.
     * @return A list of TrainerEntity that are unassigned to the trainee.
     */
    @Transactional
    public List<TrainerEntity> getUnassignedTrainers(String traineeUsername) {
        log.info("Fetching unassigned trainers for trainee: {}", traineeUsername);

        TraineeEntity trainee = traineeRepository.findByUser_Username(traineeUsername)
                .orElseThrow(() -> new TraineeNotFoundException("Trainee not found for username: "
                        + traineeUsername));

        List<TrainerEntity> allTrainers = trainerService.findAll();
        List<TrainerEntity> assignedTrainers = trainerService.findAssignedTrainers(trainee.getId());

        allTrainers.removeAll(assignedTrainers);
        log.info("Found {} unassigned trainers for trainee: {}", allTrainers.size(), traineeUsername);
        return allTrainers;
    }

    /**
     * Updates the trainers associated with a trainee.
     *
     * @param requestDto The trainee trainee with updated trainers.
     * @return The updated TraineeEntity.
     */
    @Transactional
    public TraineeEntity updateTraineeTrainerList(UpdateTraineeTrainerListRequestDto requestDto) {
        log.info("Updating trainer list for trainee: {}", requestDto.getTraineeUsername());

        TraineeEntity trainee = traineeRepository.findByUser_Username(requestDto.getTraineeUsername())
                .orElseThrow(() -> new TraineeNotFoundException("Trainee not found"));

        List<TrainerEntity> trainers = new ArrayList<>();
        List<String> trainerUsername = requestDto.getTrainerUsername();
        for (String s : trainerUsername) {
            TrainerEntity trainer = trainerService.getTrainer(s);
            trainers.add(trainer);
        }
        trainee.setTrainers(new HashSet<>(trainers));

        validationUtils.validateUpdateTraineeTrainerList(trainee, trainers);
        traineeRepository.save(trainee);
        log.info("Updated trainer list for trainee: {}", trainee.getUser().getUsername());

        return trainee;
    }

    /**
     * Updates the profile of an existing trainee.
     *
     * @param requestDto The trainee trainee with updated profile information.
     * @return The updated TraineeEntity.
     */
    @Transactional
    public TraineeEntity updateTraineeProfile(UpdateTraineeRequestDto requestDto) {
        log.info("Updating trainee profile for {}", requestDto.getUsername());
        TraineeEntity trainee = getTrainee(requestDto.getUsername());
        if (requestDto.getDateOfBirth() != null) {
            trainee.setDateOfBirth(requestDto.getDateOfBirth());
        }
        if (requestDto.getAddress() != null) {
            trainee.setAddress(requestDto.getAddress());
        }
        UserEntity user = trainee.getUser();
        user.setUsername(requestDto.getUsername());
        user.setLastName(requestDto.getLastName());
        user.setFirstName(requestDto.getFirstName());
        user.setIsActive(requestDto.isPublic());
        UserEntity save = userService.save(user);
        trainee.setUser(save);
        validationUtils.validateUpdateTrainee(trainee);
        traineeRepository.save(trainee);

        log.info("Trainee profile updated successfully for {}", save.getUsername());
        return trainee;
    }

    /**
     * Deletes a trainee profile by username.
     *
     * @param username The username of the trainee to delete.
     */
    @Transactional
    public void deleteTraineeByUsername(String username) {
        log.info("Deleting trainee with username: {}", username);
        TraineeEntity trainee = traineeRepository.findByUser_Username(username)
                .orElseThrow(() -> new TraineeNotFoundException("TraineeEntity not found"));
        traineeRepository.delete(trainee);
        log.info("Trainee deleted successfully with username: {}", username);
    }

    /**
     * Retrieves a trainee by their username.
     *
     * @param username The username of the trainee.
     * @return The found TraineeEntity.
     */
    @Transactional
    public TraineeEntity getTrainee(String username) {
        log.info("Retrieving trainee by username: {}", username);
        return traineeRepository.findByUser_Username(username)
                .orElseThrow(() -> new TraineeNotFoundException("Trainee not found for username: "
                        + username));
    }

}
