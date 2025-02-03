package org.example.gym.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.AddTrainingRequestDto;
import org.example.gym.dto.request.TraineeTrainingsRequestDto;
import org.example.gym.dto.request.TrainerTrainingRequestDto;
import org.example.gym.dto.request.TrainerWorkloadRequest;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.TrainingNotFoundException;
import org.example.gym.mapper.TrainingMapper;
import org.example.gym.repository.TrainerWorkloadClient;
import org.example.gym.repository.TrainingRepository;
import org.example.gym.utils.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final TrainerWorkloadClient workloadClient;
    private final HttpServletRequest request;

    /**
     * Constructs a service for managing training data interactions.
     *
     * @param trainingRepository Repository for CRUD operations on training data.
     * @param traineeService     Service for managing trainee data.
     * @param trainerService     Service for managing trainer data.
     * @param trainingMapper     Mapper to convert between DTOs and entity objects.
     * @param validationUtils    Utility class for validating training data.
     * @param workloadClient     Client for notifying updates to a trainer's workload.
     */
    public TrainingService(TrainingRepository trainingRepository, TraineeService traineeService,
                           TrainerService trainerService, ValidationUtils validationUtils,
                           TrainingMapper trainingMapper, TrainerWorkloadClient workloadClient,
                           HttpServletRequest request) {
        this.trainingMapper = trainingMapper;
        this.trainingRepository = trainingRepository;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.validationUtils = validationUtils;
        this.workloadClient = workloadClient;

        this.request = request;
    }

    /**
     * Retrieves the authorization token from the HTTP request headers.
     * This token is typically used for authentication and authorization purposes
     * in making secure calls to external services.
     *
     * @return A string value representing the authorization token from the HTTP request headers.
     */
    private String getAuthToken() {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || authorization.isEmpty()) {
            throw new RuntimeException("Authorization token is missing");
        }
        return authorization;
    }

    /**
     * Adds a training session to the database with specified details from the request DTO.
     * Notifies the associated trainer's workload upon successful addition of the training session.
     *
     * @param requestDto DTO containing details about the training session to be added.
     */
    @Transactional
    @CircuitBreaker(name = "trainerWorkloadService", fallbackMethod = "fallbackUpdateWorkload")
    public void addTraining(AddTrainingRequestDto requestDto) {

        TraineeEntity trainee = traineeService.getTrainee(requestDto.getTraineeUsername());
        TrainerEntity trainer = trainerService.getTrainer(requestDto.getTrainerUsername());

        TrainingEntity training = trainingMapper.requestDtoMapToTrainingEntity(requestDto, trainee, trainer);
        trainingRepository.save(training);

        UserEntity user = trainer.getUser();
        TrainerWorkloadRequest request = new TrainerWorkloadRequest(user.getUsername(), user.getFirstName(),
                user.getLastName(), user.getIsActive(),
                training.getTrainingDate(), training.getTrainingDuration(), "ADD");
        notifyTrainerWorkloadService(request);
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
        TrainerWorkloadRequest request = new TrainerWorkloadRequest(user.getUsername(), user.getFirstName(),
                user.getLastName(), user.getIsActive(),
                training.getTrainingDate(), training.getTrainingDuration(), "DELETE");
        notifyTrainerWorkloadService(request);
        log.info("Training deleted successfully and trainer's workload has been notified.");
    }

    /**
     * Notifies the external trainer workload service about updates to a trainer's workload.
     * This method is typically called after adding or deleting a training session to update
     * the associated trainer's workload status.
     * The method uses {@code workloadClient} to send the update, carrying an authorization token
     * for secure communication, obtained from the HTTP request headers.
     * Uses the {@link io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker} pattern,
     * ensuring the system's resilience to failures in the external workload update service by
     * facilitating a controlled method of failing.
     *
     * @param request The {@link TrainerWorkloadRequest} containing details such as the trainer's username
     *                and the type of update (ADD or DELETE) reflecting the change in workload.
     * @throws Exception if there are underlying issues with network, authentication, or data processing
     *                   that prevent successful notification.
     */
    @CircuitBreaker(name = "trainerWorkloadService", fallbackMethod = "fallbackUpdateWorkload")
    public void notifyTrainerWorkloadService(TrainerWorkloadRequest request) {
        String authToken = getAuthToken();
        ResponseEntity<String> response = workloadClient.updateWorkload(authToken, request);
        if (! response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to update trainer workload, status: "
                    + response.getStatusCode());
        }
        log.info("Trainer workload updated successfully: {}", response.getBody());
    }

    /**
     * Fallback method for updating workload when an exception occurs.
     * This method is invoked when the normal workflow for updating the workload fails
     * and provides a user-friendly response indicating a service failure.
     *
     * @param request The original {@link TrainerWorkloadRequest} object containing the data
     *                for the workload update request.
     * @param ex      The {@link Throwable} exception that caused the fallback to be triggered,
     *                typically representing an issue such as an external service failure.
     * @return A {@link ResponseEntity} containing the HTTP status and a message indicating that
     *         the service is temporarily unavailable, and the user should try again later.
     */
    public ResponseEntity<String> fallbackUpdateWorkload(TrainerWorkloadRequest request, Throwable ex) {
        log.error("Fallback for updating workload is activated due to {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Service temporarily unavailable. Try again later.");
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

        List<TrainingEntity> trainings = trainingRepository.findTrainingsForTrainer(trainer.getUser().getUsername(),
                requestDto.getPeriodFrom(), requestDto.getPeriodTo(), requestDto.getTraineeName());

        if (trainings.isEmpty()) {
            log.warn("No trainings found for trainer: {}", requestDto.getTrainerUsername());
            throw new TrainingNotFoundException("No trainings found for the specified criteria.");
        }

        log.info("Found {} trainings for trainer: {}", trainings.size(), requestDto.getTrainerUsername());
        return trainings;
    }


}
