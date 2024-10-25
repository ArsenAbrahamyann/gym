package org.example.gym.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.TraineeTrainingsRequestDto;
import org.example.gym.dto.request.TrainerTrainingRequestDto;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.exeption.TrainingNotFoundException;
import org.example.gym.repository.TrainingRepository;
import org.example.gym.utils.ValidationUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     *
     * @param trainingRepository  the {@link TrainingRepository} used for performing CRUD operations on training entities.
     * @param traineeService      the {@link TraineeService} used for managing trainees. This service is injected lazily
     *                            to prevent circular dependency.
     * @param trainingTypeService the {@link TrainingTypeService} used for managing training type entities. Injected lazily to avoid
     *                            circular dependency.
     * @param trainerService      the {@link TrainerService} used for managing trainers. Injected lazily to prevent circular
     *                            dependency.
     * @param validationUtils     the {@link ValidationUtils} used for performing validation tasks on training data.
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
     */
    @Transactional
    public void addTraining(TrainingEntity training) {

        trainingRepository.save(training);
        log.info("Training added successfully for trainee: {}", training.getTrainee().getUsername());
    }

    /**
     * Retrieves a list of trainings for a specific trainee based on the provided criteria.
     *
     * @return A list of training entities that match the criteria.
     */
    @Transactional
    public List<TrainingEntity> getTrainingsForTrainee(TraineeTrainingsRequestDto requestDto) {

        log.info("Fetching trainings for trainee: {}", requestDto.getTraineeName());

        validationUtils.validateTraineeTrainingsCriteria(requestDto);

        List<TrainingEntity> trainings = trainingRepository.findTrainingsForTrainee(requestDto.getTraineeName(),
                requestDto.getPeriodFrom(), requestDto.getPeriodTo(), requestDto.getTrainerName(),
                requestDto.getTrainingType());

        if (trainings.isEmpty()) {
            log.warn("No trainings found for trainee: {}", requestDto.getTraineeName());
            throw new TrainingNotFoundException("No trainings found for the specified criteria.");
        }

        log.info("Found {} trainings for trainee: {}", trainings.size(), requestDto.getTraineeName());
        return trainings;
    }

    /**
     * Retrieves a list of trainings conducted by a specific trainer based on the provided criteria.
     *
     * @return A list of training entities that match the criteria.
     */
    @Transactional
    public List<TrainingEntity> getTrainingsForTrainer(TrainerTrainingRequestDto requestDto) {

        log.info("Fetching trainings for trainer: {}", requestDto.getTrainerUsername());

        validationUtils.validateTrainerTrainingsCriteria(requestDto);
        TrainerEntity trainer = trainerService.getTrainer(requestDto.getTrainerUsername());

        List<TrainingEntity> trainings = trainingRepository.findTrainingsForTrainer(trainer.getId(),
                requestDto.getPeriodFrom(), requestDto.getPeriodTo(), requestDto.getTraineeName());

        if (trainings.isEmpty()) {
            log.warn("No trainings found for trainer: {}", requestDto.getTrainerUsername());
            throw new TrainingNotFoundException("No trainings found for the specified criteria.");
        }

        log.info("Found {} trainings for trainer: {}", trainings.size(), requestDto.getTrainerUsername());
        return trainings;
    }

}
