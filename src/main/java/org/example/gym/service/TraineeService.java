package org.example.gym.service;

import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.ActivateRequestDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.ResourceNotFoundException;
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
     * @param trainee The trainee entity containing profile information.
     * @return The created TraineeEntity.
     */
    @Transactional
    public TraineeEntity createTraineeProfile(TraineeEntity trainee) {
        log.info("Creating trainee profile");
        List<String> allUsernames = userService.findAllUsernames();
        String generateUsername = userUtils.generateUsername(trainee.getFirstName(), trainee.getLastName(),
                allUsernames);
        trainee.setUsername(generateUsername);
        String generatePassword = userUtils.generatePassword();
        trainee.setPassword(generatePassword);
        validationUtils.validateTrainee(trainee);
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
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found for username: "
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
     * @param trainee The trainee entity with updated trainers.
     * @return The updated TraineeEntity.
     */
    @Transactional
    public TraineeEntity updateTraineeTrainers(TraineeEntity trainee) {
        log.info("Updating trainers for trainee: {}", trainee.getUsername());
        Set<TrainerEntity> trainers = trainee.getTrainers();
        validationUtils.validateUpdateTraineeTrainerList(trainee, trainers);
        trainers.forEach(validationUtils::validateTrainer);
        TraineeEntity traineeEntity = traineeRepository.save(trainee);
        log.info("Successfully updated trainer list for trainee: {}", trainee.getUsername());
        return traineeEntity;
    }

    /**
     * Updates the profile of an existing trainee.
     *
     * @param traineeEntity The trainee entity with updated profile information.
     * @return The updated TraineeEntity.
     */
    @Transactional
    public TraineeEntity updateTraineeProfile(TraineeEntity traineeEntity) {
        log.info("Updating trainee profile for {}", traineeEntity.getUsername());
        validationUtils.validateUpdateTrainee(traineeEntity);
        TraineeEntity trainee = traineeRepository.save(traineeEntity);

        log.info("Trainee profile updated successfully for {}", traineeEntity.getUsername());
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
        UserEntity user = userService.findByUsername(username);

        TraineeEntity trainee = traineeRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("TraineeEntity not found"));

        traineeRepository.delete(trainee);
        log.info("Trainee deleted successfully with username: {}", username);
    }

    /**
     * Finds a trainee by their ID.
     *
     * @param traineeId The ID of the trainee.
     * @return The found TraineeEntity.
     */
    @Transactional
    public TraineeEntity findById(Long traineeId) {
        return traineeRepository.findById(traineeId)
                .orElseThrow(() -> new TraineeNotFoundException("Trainee not found for traineeId: "
                        + traineeId));
    }

    /**
     * Retrieves a trainee by their username.
     *
     * @param username The username of the trainee.
     * @return The found TraineeEntity.
     */
    @Transactional
    public TraineeEntity getTrainee(String username) {
        return traineeRepository.findByUsername(username)
                .orElseThrow(() -> new TraineeNotFoundException("Trainee not found for username: "
                        + username));
    }

}
