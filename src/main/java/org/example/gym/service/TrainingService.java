package org.example.gym.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.AddTrainingRequestDto;
import org.example.gym.dto.request.TraineeTrainingsRequestDto;
import org.example.gym.dto.request.TrainerTrainingRequestDto;
import org.example.gym.dto.request.TrainerWorkloadRequestDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.TrainingNotFoundException;
import org.example.gym.mapper.TrainingMapper;
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
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingMapper trainingMapper;
    private final ValidationUtils validationUtils;
    private final JmsProducerService jmsProducerService;


    /**
     * Constructs a service for managing training data interactions.
     *
     * @param trainingRepository Repository for CRUD operations on training data.
     * @param traineeService     Service for managing trainee data.
     * @param trainerService     Service for managing trainer data.
     * @param trainingMapper     Mapper to convert between DTOs and entity objects.
     * @param validationUtils    Utility class for validating training data.
     */
    public TrainingService(TrainingRepository trainingRepository, TraineeService traineeService,
                           TrainerService trainerService, ValidationUtils validationUtils,
                           TrainingMapper trainingMapper, @Lazy JmsProducerService jmsProducerService
    ) {
        this.trainingMapper = trainingMapper;
        this.trainingRepository = trainingRepository;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.validationUtils = validationUtils;
        this.jmsProducerService = jmsProducerService;
    }



    /**
     * Adds a training session to the database with specified details from the request DTO.
     * Notifies the associated trainer's workload upon successful addition of the training session.
     *
     * @param requestDto DTO containing details about the training session to be added.
     */
    @Transactional
    public void addTraining(AddTrainingRequestDto requestDto) {

        TraineeEntity trainee = traineeService.getTrainee(requestDto.getTraineeUsername());
        TrainerEntity trainer = trainerService.getTrainer(requestDto.getTrainerUsername());

        TrainingEntity training = trainingMapper.requestDtoMapToTrainingEntity(requestDto, trainee, trainer);
        trainingRepository.save(training);

        UserEntity user = trainer.getUser();
        TrainerWorkloadRequestDto request = new TrainerWorkloadRequestDto(user.getUsername(), user.getFirstName(),
                user.getLastName(), user.getIsActive(),
                training.getTrainingDate(), training.getTrainingDuration(), "ADD");

        jmsProducerService.sendTrainingUpdate(request);
        log.info("Training added and notification sent to trainer workload service.");

    }

    /**
     * Deletes a training session based on the specified training ID.
     *
     * @param trainingId the ID of the training session to be deleted.
     */
    @Transactional
    public void deleteTraining(Long trainingId) {

        TrainingEntity training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new TrainingNotFoundException("Training not found with ID: "
                        + trainingId));
        trainingRepository.delete(training);
        UserEntity user = training.getTrainer().getUser();
        TrainerWorkloadRequestDto request = new TrainerWorkloadRequestDto(user.getUsername(), user.getFirstName(),
                user.getLastName(), user.getIsActive(),
                training.getTrainingDate(), training.getTrainingDuration(), "DELETE");
        jmsProducerService.sendTrainingUpdate(request);
        log.info("Training deleted successfully and trainer's workload has been notified.");
    }

    /**
     * Retrieves a list of trainings for a specific trainee based on the provided criteria.
     *
     * @return A list of training entities that match the criteria.
     */
    @Transactional
    public List<TrainingEntity> getTrainingsForTrainee(TraineeTrainingsRequestDto requestDto) {

        log.info("Fetching trainings for traineeName!");

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

        log.info("Fetching trainings for trainer! ");

        validationUtils.validateTrainerTrainingsCriteria(requestDto);
        TrainerEntity trainer = trainerService.getTrainer(requestDto.getTrainerUsername());

        List<TrainingEntity> trainings = trainingRepository.findTrainingsForTrainer(trainer.getUser().getUsername(),
                requestDto.getPeriodFrom(), requestDto.getPeriodTo(), requestDto.getTraineeName());

        if (trainings.isEmpty()) {
            log.warn("No trainings found for trainer ID: {}", trainer.getId());
            throw new TrainingNotFoundException("No trainings found for the specified criteria.");
        }

        log.info("Found {} trainings for trainer: {}", trainings.size(), trainer.getId());
        return trainings;
    }
}
