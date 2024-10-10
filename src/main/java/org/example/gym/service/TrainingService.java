package org.example.gym.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.TraineeTrainingsRequestDto;
import org.example.gym.dto.request.TrainerTrainingRequestDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.exeption.ResourceNotFoundException;
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
     * @throws ResourceNotFoundException If the specified trainee or trainer is not found.
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
     * @throws ResourceNotFoundException If no trainings are found that match the criteria.
     */
    @Transactional
    public List<TrainingEntity> getTrainingsForTrainee(TraineeTrainingsRequestDto requestDto) {

        log.info("Fetching trainings for trainee: {}", requestDto.getTraineeName());

        validationUtils.validateTraineeTrainingsCriteria(requestDto);
        TraineeEntity trainee = traineeService.getTrainee(requestDto.getTraineeName());

        TrainingTypeEntity byTrainingTypeName = trainingTypeService.findByTrainingTypeName(
                        requestDto.getTrainingType())
                .orElseThrow(() -> new ResourceNotFoundException("TrainingTypeEntity not found."));

        List<TrainingEntity> trainings = trainingRepository.findTrainingsForTrainee(trainee.getId(),
                requestDto.getPeriodFrom(), requestDto.getPeriodTo(), requestDto.getTrainerName(),
                byTrainingTypeName);

        if (trainings.isEmpty()) {
            log.warn("No trainings found for trainee: {}", requestDto.getTraineeName());
            throw new ResourceNotFoundException("No trainings found for the specified criteria.");
        }

        log.info("Found {} trainings for trainee: {}", trainings.size(), requestDto.getTraineeName());
        return trainings;
    }

    /**
     * Retrieves a list of trainings conducted by a specific trainer based on the provided criteria.
     *
     * @return A list of training entities that match the criteria.
     * @throws ResourceNotFoundException If no trainings are found that match the criteria.
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
            throw new ResourceNotFoundException("No trainings found for the specified criteria.");
        }

        log.info("Found {} trainings for trainer: {}", trainings.size(), requestDto.getTrainerUsername());
        return trainings;
    }

    /**
     * Retrieves a list of {@link TrainingEntity} records for a specific trainer within a
     * given date range and trainee name.
     *
     * @param id          the unique ID of the trainer for whom the trainings are to be found.
     * @param fromDate    the start of the date range for retrieving the trainings.
     * @param toDate      the end of the date range for retrieving the trainings.
     * @param traineeName the name of the trainee to filter the trainings.
     * @return a list of {@link TrainingEntity} records that match the criteria.
     */
    @Transactional
    public List<TrainingEntity> findTrainingsForTrainer(Long id, LocalDateTime fromDate, LocalDateTime toDate,
                                                        String traineeName) {
        log.info("Fetching trainings for trainer ID: {} from {} to {} for trainee: {}", id, fromDate, toDate,
                traineeName);

        List<TrainingEntity> trainings = trainingRepository.findTrainingsForTrainer(id, fromDate, toDate, traineeName);
        if (trainings.isEmpty()) {
            log.warn("No trainings found for trainer ID: {}", id);
            throw new ResourceNotFoundException("No trainings found for the specified criteria.");
        }
        return trainings;
    }
}
