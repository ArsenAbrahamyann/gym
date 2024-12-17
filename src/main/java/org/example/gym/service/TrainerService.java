package org.example.gym.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.ActivateRequestDto;
import org.example.gym.dto.request.UpdateTrainerRequestDto;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.entity.enums.Role;
import org.example.gym.exeption.TrainerNotFoundException;
import org.example.gym.repository.TrainerRepository;
import org.example.gym.utils.UserUtils;
import org.example.gym.utils.ValidationUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing Trainer profiles and related operations.
 */
@Service
@Slf4j
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainingTypeService trainingTypeService;
    private final UserService userService;
    private final ValidationUtils validationUtils;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@link TrainerService} instance, injecting the necessary dependencies for managing trainer operations.
     * This constructor uses Spring's `@Lazy` annotation for injecting some dependencies to avoid circular dependencies.
     *
     * @param trainerRepository   the {@link TrainerRepository} used for CRUD operations on {@link TrainerEntity} objects
     * @param trainingTypeService the {@link TrainingTypeService} instance, injected lazily to manage training types and avoid circular dependencies
     * @param validationUtils     a utility class {@link ValidationUtils} used for performing validation checks on trainer data
     * @param userService           a utility class {@link UserUtils} used for user-related helper methods, such as user generation or formatting
     * @param passwordEncoder   a utility class {@link BCryptPasswordEncoder} used for encode password users
     */
    public TrainerService(TrainerRepository trainerRepository,
                          TrainingTypeService trainingTypeService,
                          ValidationUtils validationUtils,
                          UserService userService,
                          BCryptPasswordEncoder passwordEncoder
    ) {
        this.trainerRepository = trainerRepository;
        this.trainingTypeService = trainingTypeService;
        this.validationUtils = validationUtils;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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

        UserEntity user = trainer.getUser();
        String password = user.getPassword();
        String encode = passwordEncoder.encode(password);
        user.setPassword(encode);
        user.setRole(Role.ROLE_TRAINER);
        userService.save(user);
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
        UserEntity user = trainer.getUser();
        user.setIsActive(requestDto.isActive());
        UserEntity save = userService.save(user);
        trainer.setUser(save);
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
        UserEntity user = trainer.getUser();
        user.setUsername(requestDto.getUsername());
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setIsActive(requestDto.isPublic());
        TrainingTypeEntity trainingType = trainingTypeService.findById(requestDto.getTrainingTypeId());
        trainer.setSpecialization(trainingType);
        UserEntity save = userService.save(user);
        trainer.setUser(save);
        validationUtils.validateUpdateTrainer(trainer);
        TrainerEntity updateTrainer = trainerRepository.save(trainer);
        log.info("Trainee profile updated successfully for {}", user.getUsername());
        return updateTrainer;
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
        return trainerRepository.findByUser_Username(trainerUsername)
                .orElseThrow(() -> {
                    log.error("Trainer not found with username: {}", trainerUsername);
                    return new TrainerNotFoundException("Trainer not found with username: " + trainerUsername);
                });
    }
}
