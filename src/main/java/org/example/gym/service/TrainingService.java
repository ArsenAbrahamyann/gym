package org.example.gym.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.exeption.ResourceNotFoundException;
import org.example.gym.repository.TrainingRepository;
import org.example.gym.utils.ValidationUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

/**
 * Service class for managing training records.
 * Provides methods to add new trainings and retrieve training records for trainees and trainers.
 */
@Service
@Slf4j
@Lazy
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TraineeService traineeService;
    private final TrainingTypeService trainingTypeService;
    private final TrainerService trainerService;
    private final ValidationUtils validationUtils;

    /**
     * Constructs a new {@code TrainingService} instance with dependencies injected for handling training-related operations.
     * Several services are injected lazily to avoid circular dependency issues during initialization. This constructor
     * initializes the required repositories, services, and validation utilities for managing training sessions.
     *
     * @param trainingRepository   the {@link TrainingRepository} used for performing CRUD operations on training entities.
     * @param traineeService       the {@link TraineeService} used for managing trainees. This service is injected lazily
     *                             to prevent circular dependency.
     * @param trainingTypeService  the {@link TrainingTypeService} used for managing training type entities. Injected lazily to avoid
     *                             circular dependency.
     * @param trainerService       the {@link TrainerService} used for managing trainers. Injected lazily to prevent circular
     *                             dependency.
     * @param validationUtils      the {@link ValidationUtils} used for performing validation tasks on training data.
     */
    public TrainingService(TrainingRepository trainingRepository, @Lazy TraineeService traineeService,
                           @Lazy TrainingTypeService trainingTypeService, @Lazy TrainerService trainerService,
                           ValidationUtils validationUtils) {
        this.trainingRepository = trainingRepository;
        this.traineeService = traineeService;
        this.trainingTypeService = trainingTypeService;
        this.trainerService = trainerService;
        this.validationUtils = validationUtils;
    }

    /**
     * Adds a new training record.
     *
     * @param training The details of the training to be added.
     * @throws EntityNotFoundException If the specified trainee or trainer is not found.
     */
    @Transactional
    public void addTraining(TrainingEntity training) {
        try {
            trainingRepository.save(training);
            log.info("Training added successfully for trainee: {}", training.getTrainee().getUsername());
        } catch (EntityNotFoundException e) {
            log.error("Error adding training: {}", e.getMessage());
        } catch (Exception e) {
            log.error("An unexpected error occurred while adding training: {}", e.getMessage());
        }
    }

    /**
     * Retrieves a list of trainings for a specific trainee based on the provided criteria.
     *
     * @param traineeName The username of the trainee.
     * @param fromDate    The start date of the training period.
     * @param toDate      The end date of the training period.
     * @param trainerName The name of the trainer (optional).
     * @param trainingType The type of training (optional).
     * @return A list of training entities that match the criteria.
     * @throws EntityNotFoundException If the specified trainee is not found.
     * @throws ResourceNotFoundException If no trainings are found that match the criteria.
     */
    @Transactional
    public List<TrainingEntity> getTrainingsForTrainee(String traineeName, LocalDateTime fromDate, LocalDateTime toDate,
                                                       String trainerName, String trainingType) {

        log.info("Fetching trainings for trainee: {}", traineeName);

        List<TrainingEntity> trainings = new ArrayList<>();
        try {
            validationUtils.validateTraineeTrainingsCriteria(traineeName, fromDate, toDate, trainerName, trainingType);

            TraineeEntity trainee = traineeService.getTrainee(traineeName);

            trainings = trainingRepository.findTrainingsForTrainee(trainee.getId(),
                            fromDate, toDate, trainerName, trainingType);
            if (trainings == null) {
                trainings = Collections.emptyList();
            }
        } catch (Exception e) {
            log.error("Error fetching trainings: {}", e.getMessage());
            return new ArrayList<>();
        }

        log.info("Found {} trainings for trainee: {}", trainings.size(), traineeName);
        return trainings;
    }

    /**
     * Retrieves a list of trainings conducted by a specific trainer based on the provided criteria.
     *
     * @param trainerUsername The username of the trainer.
     * @param fromDate        The start date of the training period.
     * @param toDate          The end date of the training period.
     * @param traineeName     The name of the trainee (optional).
     * @return A list of training entities that match the criteria.
     * @throws EntityNotFoundException If the specified trainer is not found.
     * @throws ResourceNotFoundException If no trainings are found that match the criteria.
     */
    @Transactional
    public List<TrainingEntity> getTrainingsForTrainer(String trainerUsername, LocalDateTime fromDate,
                                                       LocalDateTime toDate, String traineeName) {

        log.info("Fetching trainings for trainer: {}", trainerUsername);
        List<TrainingEntity> trainings = new ArrayList<>();
        try {
            validationUtils.validateTrainerTrainingsCriteria(trainerUsername, fromDate, toDate, traineeName);
            TrainerEntity trainer = trainerService.getTrainer(trainerUsername);
            trainings = trainingRepository.findTrainingsForTrainer(trainer.getId(), fromDate, toDate, traineeName);
            if (trainings == null) {
                log.info("No trainings found for the specified criteria");
            }
        } catch (EntityNotFoundException e) {
            log.error("Error fetching trainings: {}", e.getMessage());
        } catch (Exception e) {
            log.error("An unexpected error occurred while fetching trainings: {}", e.getMessage());
        }
        return trainings;
    }

    /**
     * Retrieves a list of {@link TrainingEntity} records for a specific trainer within a given date range and trainee name.
     * This method is transactional, ensuring that the database operations are
     * executed within a single transaction.
     *
     * @param id          the unique ID of the trainer for whom the trainings are to be found.
     * @param fromDate    the start of the date range for retrieving the trainings.
     * @param toDate      the end of the date range for retrieving the trainings.
     * @param traineeName the name of the trainee to filter the trainings.
     * @return an {@link Optional} containing a list of {@link TrainingEntity} records that match
     *         the criteria (trainer ID, date range, and trainee name), or an empty {@link Optional}
     *         if no trainings are found.
     */
    @Transactional
    public List<TrainingEntity> findTrainingsForTrainer(Long id, LocalDateTime fromDate, LocalDateTime toDate,
                                                    String traineeName) {
        log.info("Fetching trainings for trainer ID: {} from {} to {} for trainee: {}", id,
                fromDate, toDate, traineeName);
        try {
            return trainingRepository.findTrainingsForTrainer(id, fromDate, toDate, traineeName);
        } catch (Exception e) {
            log.error("Error fetching trainings for trainer ID {}: {}", id, e.getMessage());
            return Collections.emptyList();
        }
    }
}
