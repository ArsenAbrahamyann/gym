package org.example.gym.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.repository.TrainerRepository;
import org.example.gym.utils.UserUtils;
import org.example.gym.utils.ValidationUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

/**
 * Service class for managing Trainer profiles and related operations.
 */
@Service
@Slf4j
@Lazy
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;
    private final UserService userService;
    private final UserUtils userUtils;
    private final ValidationUtils validationUtils;

    /**
     * Constructs a new {@code TrainerService} instance with dependencies injected for handling trainer-related operations.
     * Several services are injected lazily to avoid circular dependency issues that could arise during initialization.
     * This constructor takes repositories, services, and utilities necessary for managing trainer entities and related business logic.
     *
     * @param trainerRepository    the {@link TrainerRepository} used for performing CRUD operations on trainer entities.
     * @param trainingService      the {@link TrainingService} used for managing training sessions. This service is injected
     *                             lazily to prevent circular dependency.
     * @param trainingTypeService  the {@link TrainingTypeService} used for handling training type entities. This service is injected
     *                             lazily to avoid circular dependency.
     * @param userService          the {@link UserService} responsible for managing user-related operations. This service is injected
     *                             lazily due to its interaction with multiple services.
     * @param validationUtils      the {@link ValidationUtils} used for performing validation tasks for trainer data.
     */
    public TrainerService(TrainerRepository trainerRepository, @Lazy TrainingService trainingService,
                          @Lazy TrainingTypeService trainingTypeService, @Lazy UserService userService,
                          ValidationUtils validationUtils, UserUtils userUtils) {
        this.trainerRepository = trainerRepository;
        this.trainingService = trainingService;
        this.trainingTypeService = trainingTypeService;
        this.userService = userService;
        this.validationUtils = validationUtils;
        this.userUtils = userUtils;
    }

    /**
     * Creates a new trainer profile.
     *
     * @return The created TrainerDto object.
     */
    @Transactional
    public TrainerEntity createTrainerProfile(TrainerEntity trainer) {
        log.info("Creating trainer profile ");
        try {
            List<String> allUsernames = userService.findAllUsernames();
            String generateUsername = userUtils.generateUsername(trainer.getFirstName(), trainer.getLastName(), allUsernames);
            trainer.setUsername(generateUsername);
            String generatePassword = userUtils.generatePassword();
            trainer.setPassword(generatePassword);
            validationUtils.validateTrainer(trainer);
            TrainingTypeEntity trainingType = trainer.getSpecialization();

            if (trainingType.getId() == null) {
                trainingTypeService.save(trainingType);
            }

            userService.authenticateUser(trainer.getUsername(), trainer.getPassword());
            trainerRepository.save(trainer);
            log.debug("Trainer profile created: {}", trainer);
            return trainer;
        } catch (Exception e) {
            log.error("Failed to create trainer profile", e);
            return null;
        }
    }

    /**
     * Changes the password of the trainer.
     *
     * @param username    The username of the trainer.
     * @param newPassword The new password to set.
     */
    @Transactional
    public void changeTrainerPassword(String username, String newPassword) {
        log.info("Changing password for trainer {}", username);
        try {
            log.info("Changing password for trainer {}", username);
            UserEntity user = userService.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("Trainer not found with username: "
                            + username));

            validationUtils.validatePasswordMatch(user, newPassword);
            user.setPassword(newPassword);
            userService.update(user);
            log.info("Password updated successfully for trainer {}", username);
        } catch (Exception e) {
            log.error("Error changing password for trainer {}: {}", username, e.getMessage());
        }
    }

    /**
     * Toggles the active status of a trainer.
     *
     * @param username The username of the trainer.
     */
    @Transactional
    public void toggleTrainerStatus(String username, boolean isActive) {
        try {
            log.info("Toggling trainer status for {}", username);
            TrainerEntity trainer = getTrainer(username);
            trainer.setIsActive(isActive);
            trainerRepository.save(trainer);
            log.info("Trainer status toggled successfully for {}", username);
        } catch (Exception e) {
            log.error("Error toggling trainer status for {}: {}", username, e.getMessage());
        }
    }

    /**
     * Updates the profile of the trainer.
     *
     */
    @Transactional
    public TrainerEntity updateTrainerProfile(TrainerEntity trainer) {
        try {
            log.info("Updating trainer profile for {}", trainer.getUsername());
            validationUtils.validateUpdateTrainer(trainer);
            List<String> allUsernames = userService.findAllUsernames();
            String generateUsername = userUtils.generateUsername(trainer.getFirstName(),
                    trainer.getLastName(), allUsernames);
            trainer.setUsername(generateUsername);
            trainerRepository.save(trainer);
            log.info("Trainer profile updated successfully for {}", trainer.getUsername());
            return trainer;
        } catch (Exception e) {
            log.error("Error updating trainer profile for {}: {}", trainer.getUsername(), e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves the list of trainings for a trainer within a given date range.
     *
     * @param trainerUsername The username of the trainer.
     * @param fromDate        The start date of the date range.
     * @param toDate          The end date of the date range.
     * @param traineeName     The name of the trainee (optional).
     * @return The list of trainings for the trainer.
     */
    @Transactional
    public List<TrainingEntity> getTrainerTrainings(String trainerUsername, LocalDateTime fromDate,
                                                    LocalDateTime toDate, String traineeName) {
        try {
            log.info("Fetching trainings for trainer: {}", trainerUsername);
            TrainerEntity trainer = trainerRepository.findByUsername(trainerUsername)
                    .orElseThrow(() -> new EntityNotFoundException("Trainer not found with username: "
                            + trainerUsername));
            validationUtils.validateTrainerTrainingsCriteria(trainerUsername, fromDate, toDate, traineeName);

            List<TrainingEntity> trainings = trainingService.findTrainingsForTrainer(
                            trainer.getId(), fromDate, toDate, traineeName);
            if (trainings == null) {
                log.info("Trainings not found");
            }

            log.info("Found {} trainings for trainer: {}", trainings.size(), trainerUsername);
            return trainings;
        } catch (Exception e) {
            log.error("Error fetching trainings for trainer {}: {}", trainerUsername, e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves all {@link TrainerEntity} records from the database.
     * This method is transactional, ensuring that the database operations are
     * executed within a single transaction.
     *
     * @return an {@link Optional} containing a list of all {@link TrainerEntity} records,
     *         or an empty {@link Optional} if no records are found.
     */
    @Transactional
    public List<TrainerEntity> findAll() {
        try {
            return trainerRepository.findAll();
        } catch (Exception e) {
            log.error("Error retrieving all trainers: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves all assigned {@link TrainerEntity} records for a specific ID.
     * This method is transactional, ensuring that the database operations are
     * executed within a single transaction.
     *
     * @param id the ID for which assigned trainers need to be retrieved.
     * @return an {@link Optional} containing a list of {@link TrainerEntity} records assigned
     *         to the given ID, or an empty {@link Optional} if no trainers are found.
     */
    @Transactional
    public List<TrainerEntity> findAssignedTrainers(Long id) {
        try {
            return trainerRepository.findByTrainees_Id(id);
        } catch (Exception e) {
            log.error("Error retrieving assigned trainers for ID {}: {}", id, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves a list of {@link TrainerEntity} records by their unique IDs.
     * This method is transactional, ensuring that the database operations are
     * executed within a single transaction.
     *
     * @param trainerIds the list of unique trainer IDs to find.
     * @return an {@link Optional} containing a list of {@link TrainerEntity} records corresponding
     *         to the provided IDs, or an empty {@link Optional} if no records are found.
     */
    @Transactional
    public List<TrainerEntity> findAllById(List<Long> trainerIds) {
        try {
            return trainerRepository.findAllByIdIn(trainerIds);
        } catch (Exception e) {
            log.error("Error retrieving trainers by IDs {}: {}", trainerIds, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Finds a {@link TrainerEntity} by its unique ID.
     * This method is transactional, ensuring that the database operations are
     * executed within a single transaction.
     *
     * @param trainerId the unique ID of the trainer to find.
     * @return an {@link Optional} containing the {@link TrainerEntity} if found,
     *         or an empty {@link Optional} if no trainer is found with the given ID.
     */
    @Transactional
    public Optional<TrainerEntity> findById(Long trainerId) {
        try {
            return trainerRepository.findById(trainerId);
        } catch (Exception e) {
            log.error("Error retrieving trainer by ID {}: {}", trainerId, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Finds a {@link TrainerEntity} by the trainer's username.
     * This method is transactional, ensuring that the database operations are
     * executed within a single transaction.
     *
     * @param trainerUsername the username of the trainer to find.
     * @return an {@link Optional} containing the {@link TrainerEntity} if found,
     *         or an empty {@link Optional} if no trainer is found with the given username.
     */
    @Transactional
    public TrainerEntity getTrainer(String trainerUsername) {
        try {
            Optional<TrainerEntity> byTrainerFromUsername = trainerRepository.findByUsername(
                    trainerUsername);
            if (byTrainerFromUsername.isPresent()) {
                return byTrainerFromUsername.get();
            } else {
                log.info("Trainer not found by ", trainerUsername);
            }
        } catch (Exception e) {
            log.error("Error retrieving trainer by username {}: {}", trainerUsername, e.getMessage());
            return null;
        }
        return null;
    }

    public List<TrainerEntity> findByUsernames(List<String> trainerUsernames) {
        try {
            List<TrainerEntity> byUsernames = trainerRepository.findAllByUsernameIn(trainerUsernames);
            return byUsernames;
        } catch (Exception e) {
            log.error("Error retrieving trainers by trainerUsernames {}: {}", trainerUsernames, e.getMessage());
            return Collections.emptyList();
        }
    }
}
