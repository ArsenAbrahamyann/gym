package org.example.gym.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.ActivateRequestDto;
import org.example.gym.dto.request.UpdateTrainerRequestDto;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.exeption.TrainerNotFoundException;
import org.example.gym.repository.TrainerRepository;
import org.example.gym.utils.UserUtils;
import org.example.gym.utils.ValidationUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing Trainer profiles and related operations.
 */
@Service
@Slf4j
@Lazy
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainingService trainingService; // TODO
    private final TrainingTypeService trainingTypeService;
    private final UserService userService; // TODO
    private final UserUtils userUtils;
    private final ValidationUtils validationUtils;

    /**
     * Constructs a new {@link TrainerService} instance, injecting the necessary dependencies for managing trainer operations.
     * This constructor uses Spring's `@Lazy` annotation for injecting some dependencies to avoid circular dependencies.
     *
     * @param trainerRepository   the {@link TrainerRepository} used for CRUD operations on {@link TrainerEntity} objects
     * @param trainingService     the {@link TrainingService} instance, injected lazily to manage training sessions and avoid circular dependencies
     * @param trainingTypeService the {@link TrainingTypeService} instance, injected lazily to manage training types and avoid circular dependencies
     * @param userService         the {@link UserService} instance, injected lazily to handle user-related operations such as user registration and authentication
     * @param validationUtils     a utility class {@link ValidationUtils} used for performing validation checks on trainer data
     * @param userUtils           a utility class {@link UserUtils} used for user-related helper methods, such as user generation or formatting
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
     * @param trainer The TrainerEntity object to be created.
     * @return The created TrainerEntity object.
     */
    @Transactional
    public TrainerEntity createTrainerProfile(TrainerEntity trainer) {
        log.info("Creating trainer profile ");

        String generateUsername = userUtils.generateUsername(trainer.getFirstName(), trainer.getLastName());
        trainer.setUsername(generateUsername);

        String generatePassword = userUtils.generatePassword();
        trainer.setPassword(generatePassword);

        validationUtils.validateTrainer(trainer);

        TrainingTypeEntity trainingType = trainer.getSpecialization();
        if (trainingType != null) { // TODO Can we have a trainer without specialization?
            trainingType = trainingTypeService.findById(trainingType.getId());

            trainer.setSpecialization(trainingType);
        }

        trainerRepository.save(trainer);

        log.debug("Trainer profile created: {}", trainer);
        return trainer;
    }

    /**
     * Toggles the active status of a trainer.
     *
     * @param requestDto The request DTO containing the username of the trainer.
     */
    @Transactional
    public void toggleTrainerStatus(ActivateRequestDto requestDto) {
        log.info("Toggling trainer status for {}", requestDto.getUsername());
        TrainerEntity trainer = getTrainer(requestDto.getUsername());
        trainer.setIsActive(requestDto.isActive());
        trainerRepository.save(trainer);
        log.info("Trainer status toggled successfully for {}", requestDto.getUsername());
    }

    /**
     * Updates the profile of the trainer.
     *
     * @param requestDto The trainer entity with updated information.
     * @return The updated TrainerEntity.
     */
    @Transactional
    public TrainerEntity updateTrainerProfile(UpdateTrainerRequestDto requestDto) {
        log.info("Updating trainer profile for {}", requestDto.getUsername());
        TrainerEntity trainer = getTrainer(requestDto.getUsername());
        trainer.setUsername(requestDto.getUsername());
        trainer.setFirstName(requestDto.getFirstName());
        trainer.setLastName(requestDto.getLastName());
        trainer.setIsActive(requestDto.isPublic());
        TrainingTypeEntity trainingType = trainingTypeService.findById(requestDto.getTrainingTypeId());
        trainer.setSpecialization(trainingType);
        validationUtils.validateUpdateTrainer(trainer);
        TrainerEntity save = trainerRepository.save(trainer);
        log.info("Trainee profile updated successfully for {}", save.getUsername());
        return save;
    }

    /**
     * Retrieves all {@link TrainerEntity} records from the database.
     *
     * @return a list of all {@link TrainerEntity} records.
     */
    @Transactional
    public List<TrainerEntity> findAll() {
        log.info("Retrieving all trainers");
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
        log.info("Retrieving assigned trainers for trainee ID {}", id);
        return trainerRepository.findByTrainees_Id(id);
    }


    /**
     * Finds a {@link TrainerEntity} by the trainer's username.
     *
     * @param trainerUsername the username of the trainer to find.
     * @return the {@link TrainerEntity} if found.
     */
    @Transactional
    public TrainerEntity getTrainer(String trainerUsername) {
        log.info("Retrieving trainer by username {}", trainerUsername);
        return trainerRepository.findTrainerByUsername(trainerUsername)
                .orElseThrow(() -> {
                    log.error("Trainer not found with username: {}", trainerUsername);
                    return new TrainerNotFoundException("Trainer not found with username: " + trainerUsername);
                });
    }

    /**
     * Finds trainers by a list of usernames.
     *
     * @param trainerUsernames the list of usernames of trainers to find.
     * @return a list of {@link TrainerEntity} records corresponding to the provided usernames.
     */
    public List<TrainerEntity> findByUsernames(List<String> trainerUsernames) {
        log.info("Finding trainers by usernames: {}", trainerUsernames);
        return trainerRepository.findAllByUsernameIn(trainerUsernames);
    }
}
