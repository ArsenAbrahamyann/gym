package org.example.gym.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.ResourceNotFoundException;
import org.example.gym.repository.TraineeRepository;
import org.example.gym.utils.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing Trainee profiles and related operations.
 */
@Service
@Slf4j
public class TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerService trainerService;
    private final UserService userService;
    private final ValidationUtils validationUtils;
    private final ModelMapper modelMapper;

    /**
     * Constructs a new {@code TraineeService} instance with dependencies injected for handling trainee-related operations.
     * The {@link TrainerService} is injected lazily to avoid circular dependency issues that could arise from direct
     * initialization. This constructor also accepts repositories and utilities needed for performing trainee-related tasks.
     *
     * @param trainerService     the {@link TrainerService} used for managing trainer-related functionalities.
     *                           This service is injected lazily to prevent circular dependency.
     * @param traineeRepository  the {@link TraineeRepository} used for performing CRUD operations on trainee entities.
     * @param userService        the {@link UserService} responsible for handling user-related operations.
     * @param validationUtils    the {@link ValidationUtils} used for validating trainee data during service operations.
     * @param modelMapper        the {@link ModelMapper} used for mapping between DTOs and entities.
     */
    public TraineeService(@Lazy TrainerService trainerService, TraineeRepository traineeRepository,
                          UserService userService, ValidationUtils validationUtils, ModelMapper modelMapper) {
        this.trainerService = trainerService;
        this.traineeRepository = traineeRepository;
        this.userService = userService;
        this.validationUtils = validationUtils;
        this.modelMapper = modelMapper;
    }


    /**
     * Creates a new trainee profile.
     *
     * @return The created TraineeDto object.
     */
    @Transactional
    public TraineeEntity createTraineeProfile(TraineeEntity trainee) {
        log.info("Creating trainee profile");
        try {
            userService.authenticateUser(trainee.getUsername(), trainee.getPassword());
            traineeRepository.save(trainee);
            log.info("Trainee profile created successfully for {}", trainee.getUsername());
            return trainee;
        } catch (RuntimeException e) {
            log.error("Failed to create trainee profile: {}", e.getMessage());
            return null;
        }
    }


    /**
     * Changes the password of a trainee.
     *
     * @param username    The username of the trainee.
     * @param newPassword The new password to set.
     */
    @Transactional
    public void changeTraineePassword(String username, String newPassword) {
        log.info("Changing password for trainee {}", username);
        try {
            UserEntity user = userService.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Trainee not found with username: " + username));

            validationUtils.validatePasswordMatch(user, newPassword);
            user.setPassword(newPassword);
            userService.update(user);
            log.info("Password updated successfully for trainee {}", username);
        } catch (Exception e) {
            log.error("Failed to change password for trainee {}: {}", username, e.getMessage());
        }
    }

    /**
     * Toggles the active status of a trainee.
     *
     * @param username The username of the trainee.
     */
    @Transactional
    public void toggleTraineeStatus(String username, boolean isActive) {
        log.info("Toggling trainee status for {}", username);
        try {
            TraineeEntity trainee = getTrainee(username);
            trainee.setIsActive(isActive);
            traineeRepository.update(trainee);
            log.info("Trainee status toggled successfully for {}", username);
        } catch (Exception e) {
            log.error("Failed to toggle trainee status for {}: {}", username, e.getMessage());
        }
    }

    /**
     * Retrieves unassigned trainers for a trainee.
     *
     * @param traineeUsername The username of the trainee.
     * @return The list of unassigned trainers.
     */
    @Transactional
    public List<TrainerEntity> getUnassignedTrainers(String traineeUsername) {
        log.info("Fetching unassigned trainers for trainee: {}", traineeUsername);

        try {
            TraineeEntity trainee = traineeRepository.findByTraineeFromUsername(traineeUsername)
                    .orElseThrow(() -> new ResourceNotFoundException("Trainee not found for username: "
                            + traineeUsername));

            List<TrainerEntity> allTrainers = trainerService.findAll();
            if (allTrainers == null) {
                log.info("Trainers not found.");
            }

            List<TrainerEntity> assignedTrainers = trainerService.findAssignedTrainers(trainee.getId());
            if (assignedTrainers.isEmpty()) {
                log.info("Assigned trainers not found for trainee ID: "
                        + trainee.getId());
            }

            allTrainers.removeAll(assignedTrainers);
            log.info("Found {} unassigned trainers for trainee: {}", allTrainers.size(), traineeUsername);
            return allTrainers;
        } catch (Exception e) {
            log.error("Failed to fetch unassigned trainers for {}: {}", traineeUsername, e.getMessage());
            return List.of();
        }

    }

    /**
     * Updates the list of trainers assigned to a specific trainee.
     *
     */
    @Transactional
    public void updateTraineeTrainers(TraineeEntity trainee) {
        log.info("Updating trainers for trainee: {}", trainee.getUsername());
        Set<TrainerEntity> trainers = trainee.getTrainers();
        try {
            validationUtils.validateUpdateTraineeTrainerList(trainee, trainers);
            trainers.forEach(validationUtils::validateTrainer);
            traineeRepository.update(trainee);
            log.info("Successfully updated trainer list for trainee: {}", trainee.getUsername());
        } catch (Exception e) {
            log.error("Failed to update trainers for {}: {}", trainee.getUsername(), e.getMessage());
        }
    }

    /**
     * Updates the profile information of a trainee.
     *
     */
    @Transactional
    public void updateTraineeProfile(TraineeEntity traineeEntity) {
        log.info("Updating trainee profile for {}", traineeEntity.getUsername());
        try {
            validationUtils.validateUpdateTrainee(traineeEntity);
            traineeRepository.update(traineeEntity);
            log.info("Trainee profile updated successfully for {}", traineeEntity.getUsername());
        } catch (Exception e) {
            log.error("Failed to update trainee profile for {}: {}", traineeEntity.getUsername(), e.getMessage());
        }
    }

    /**
     * Deletes a trainee by their username.
     *
     * @param username The username of the trainee to delete.
     * @throws ResourceNotFoundException If the user or trainee is not found.
     */
    @Transactional
    public void deleteTraineeByUsername(String username) {
        log.info("Deleting trainee with username: {}", username);
        try {
            UserEntity user = userService.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            TraineeEntity trainee = traineeRepository.findByTraineeFromUsername(user.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("TraineeEntity not found"));

            traineeRepository.delete(trainee);
            log.info("Trainee deleted successfully with username: {}", username);
        } catch (Exception e) {
            log.error("Failed to delete trainee with username {}: {}", username, e.getMessage());
        }
    }

    /**
     * Finds a {@link TraineeEntity} by its unique ID.
     * This method is transactional, ensuring that the database operations are
     * executed in a single transaction, and also uses {@code @SneakyThrows} to
     * automatically handle checked exceptions that may be thrown during the
     * execution.
     *
     * @param traineeId the unique ID of the trainee to find.
     * @return an {@link Optional} containing the {@link TraineeEntity} if found,
     *         or an empty {@link Optional} if no trainee is found with the given ID.
     */
    @Transactional
    public Optional<TraineeEntity> findById(Long traineeId) {
        try {
            return traineeRepository.findById(traineeId);
        } catch (Exception e) {
            log.error("Error retrieving trainee by ID {}: {}", traineeId, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Finds a {@link TraineeEntity} by its username.
     * This method is transactional, ensuring that the database operations are
     * executed in a single transaction, and also uses {@code @SneakyThrows} to
     * automatically handle checked exceptions that may be thrown during the
     * execution.
     *
     * @param username the username of the trainee to find.
     * @return an {@link Optional} containing the {@link TraineeEntity} if found,
     *         or an empty {@link Optional} if no trainee is found with the given username.
     */
    @Transactional
    public TraineeEntity getTrainee(String username) {
        try {
            Optional<TraineeEntity> byTraineeFromUsername = traineeRepository.findByTraineeFromUsername(username);
            if (byTraineeFromUsername.isPresent()) {
                return byTraineeFromUsername.get();
            } else {
                log.info("Trainee not found.", username);
            }

        } catch (Exception e) {
            log.error("Error retrieving trainee by username {}: {}", username, e.getMessage());
            return null;
        }
        return null;
    }

}
