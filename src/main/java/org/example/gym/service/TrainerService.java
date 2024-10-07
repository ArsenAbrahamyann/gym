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
import org.example.gym.paylod.request.ActivateRequestDto;
import org.example.gym.repository.TrainerRepository;
import org.example.gym.utils.UserUtils;
import org.example.gym.utils.ValidationUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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
     * @return The created TrainerEntity object.
     */
    @Transactional
    public TrainerEntity createTrainerProfile(TrainerEntity trainer) {
        log.info("Creating trainer profile ");

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

        trainerRepository.save(trainer);
        userService.authenticateUser(trainer.getUsername(), trainer.getPassword());
        log.debug("Trainer profile created: {}", trainer);
        return trainer;
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
        UserEntity user = userService.findByUsername(username);

        validationUtils.validatePasswordMatch(user, newPassword);
        user.setPassword(newPassword);
        userService.update(user);
        log.info("Password updated successfully for trainer {}", username);
    }

    /**
     * Toggles the active status of a trainer.
     *
     * @param requestDto The username of the trainer.
     */
    @Transactional
    public void toggleTrainerStatus(ActivateRequestDto requestDto) {
        log.info("Toggling trainer status for {}", requestDto.getUsername());
        TrainerEntity trainer = getTrainer(requestDto.getUsername());
        trainer.setIsActive(requestDto.isPublic());
        trainerRepository.save(trainer);
        log.info("Trainer status toggled successfully for {}", requestDto.getUsername());
    }

    /**
     * Updates the profile of the trainer.
     *
     * @param trainer The trainer entity with updated information.
     * @return The updated TrainerEntity.
     */
    @Transactional
    public TrainerEntity updateTrainerProfile(TrainerEntity trainer) {
        log.info("Updating trainer profile for {}", trainer.getUsername());
        validationUtils.validateUpdateTrainer(trainer);

        List<String> allUsernames = userService.findAllUsernames();
        String generateUsername = userUtils.generateUsername(trainer.getFirstName(), trainer.getLastName(), allUsernames);
        trainer.setUsername(generateUsername);

        return trainerRepository.save(trainer);
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
        log.info("Fetching trainings for trainer: {}", trainerUsername);
        TrainerEntity trainer = trainerRepository.findByUsername(trainerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with username: " + trainerUsername));

        validationUtils.validateTrainerTrainingsCriteria(trainerUsername, fromDate, toDate, traineeName);

        return trainingService.findTrainingsForTrainer(trainer.getId(), fromDate, toDate, traineeName);
    }

    /**
     * Retrieves all {@link TrainerEntity} records from the database.
     *
     * @return a list of all {@link TrainerEntity} records.
     */
    @Transactional
    public List<TrainerEntity> findAll() {
        return trainerRepository.findAll();
    }

    /**
     * Retrieves all assigned {@link TrainerEntity} records for a specific ID.
     *
     * @param id the ID for which assigned trainers need to be retrieved.
     * @return a list of {@link TrainerEntity} records assigned to the given ID.
     */
    @Transactional
    public List<TrainerEntity> findAssignedTrainers(Long id) {
        return trainerRepository.findByTrainees_Id(id);
    }

    /**
     * Retrieves a list of {@link TrainerEntity} records by their unique IDs.
     *
     * @param trainerIds the list of unique trainer IDs to find.
     * @return a list of {@link TrainerEntity} records corresponding to the provided IDs.
     */
    @Transactional
    public List<TrainerEntity> findAllById(List<Long> trainerIds) {
        return trainerRepository.findAllByIdIn(trainerIds);
    }

    /**
     * Finds a {@link TrainerEntity} by its unique ID.
     *
     * @param trainerId the unique ID of the trainer to find.
     * @return an {@link Optional} containing the {@link TrainerEntity} if found.
     */
    @Transactional
    public Optional<TrainerEntity> findById(Long trainerId) {
        return trainerRepository.findById(trainerId);
    }

    /**
     * Finds a {@link TrainerEntity} by the trainer's username.
     *
     * @param trainerUsername the username of the trainer to find.
     * @return the {@link TrainerEntity} if found.
     */
    @Transactional
    public TrainerEntity getTrainer(String trainerUsername) {
        return trainerRepository.findByUsername(trainerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with username: " + trainerUsername));
    }

    public List<TrainerEntity> findByUsernames(List<String> trainerUsernames) {
        return trainerRepository.findAllByUsernameIn(trainerUsernames);
    }
}
