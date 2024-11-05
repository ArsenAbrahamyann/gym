package org.example.gym.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.ActivateRequestDto;
import org.example.gym.dto.request.UpdateTraineeRequestDto;
import org.example.gym.dto.request.UpdateTraineeTrainerListRequestDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
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
    private final UserService userService;
    private final ValidationUtils validationUtils;
    private final UserUtils userUtils;

    /**
     * Constructs a new {@link TraineeService} instance, injecting the necessary dependencies for managing trainee operations.
     * This constructor uses Spring's `@Lazy` annotation for injecting {@link TrainerService} to avoid circular dependencies.
     *
     * @param trainerService    the {@link TrainerService} instance used for managing trainers, injected lazily to prevent circular dependency issues
     * @param traineeRepository the {@link TraineeRepository} used for CRUD operations on {@link TraineeEntity} objects
     * @param userService       the {@link UserService} responsible for handling user-related operations such as authentication and registration
     * @param validationUtils   a utility class {@link ValidationUtils} used for performing validation checks on trainee data
     * @param userUtils         a utility class {@link UserUtils} used for handling user-related helper methods, such as user generation or formatting
     */
    public TraineeService(@Lazy TrainerService trainerService, TraineeRepository traineeRepository,
                          UserService userService, ValidationUtils validationUtils, UserUtils userUtils) {
        this.trainerService = trainerService;
        this.traineeRepository = traineeRepository;
        this.userService = userService;
        this.validationUtils = validationUtils;
        this.userUtils = userUtils;
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

        String generatedUsername = userUtils.generateUsername(trainee.getFirstName(), trainee.getLastName());
        trainee.setUsername(generatedUsername);
        trainee.setPassword(userUtils.generatePassword());
        validationUtils.validateTrainee(trainee); // TODO I could get new ValidationException("Trainee not found");
        // although I was not searching for anything here technically

        traineeRepository.save(trainee);
        log.info("Trainee profile created successfully for {}", trainee.getUsername());

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
        trainee.setIsActive(requestDto.isActive());
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

        TraineeEntity trainee = traineeRepository.findByUsername(traineeUsername)
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

        TraineeEntity trainee = traineeRepository.findByUsername(requestDto.getTraineeUsername())
                .orElseThrow(() -> new TraineeNotFoundException("Trainee not found"));

        List<TrainerEntity> trainers = trainerService.findByUsernames(requestDto.getTrainerUsername());
        validationUtils.validateUpdateTraineeTrainerList(trainee, trainers);
        trainee.setTrainers(new HashSet<>(trainers));

        traineeRepository.save(trainee);
        log.info("Updated trainer list for trainee: {}", trainee.getUsername());

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
        trainee.setDateOfBirth(LocalDateTime.parse(requestDto.getDateOfBirth()));
        trainee.setAddress(requestDto.getAddress());
        trainee.setUsername(requestDto.getUsername());
        trainee.setLastName(requestDto.getLastName());
        trainee.setFirstName(requestDto.getFirstName());
        trainee.setIsActive(requestDto.isPublic());
        validationUtils.validateUpdateTrainee(trainee);
        traineeRepository.save(trainee);

        log.info("Trainee profile updated successfully for {}", trainee.getUsername());
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
        TraineeEntity trainee = traineeRepository.findByUsername(username)
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
        return traineeRepository.findByUsername(username)
                .orElseThrow(() -> new TraineeNotFoundException("Trainee not found for username: "
                        + username));
    }

}
