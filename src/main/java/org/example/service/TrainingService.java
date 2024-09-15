package org.example.service;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainingDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.TraineeRepository;
import org.example.repository.TrainerRepository;
import org.example.repository.TrainingRepository;
import org.example.utils.ValidationUtils;
import org.springframework.stereotype.Service;

/**
 * Service class for managing training records.
 * Provides methods to add new trainings and retrieve training records for trainees and trainers.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final ValidationUtils validationUtils;

    /**
     * Adds a new training record.
     *
     * @param trainingDto The details of the training to be added.
     * @throws EntityNotFoundException If the specified trainee or trainer is not found.
     */
    public void addTraining(TrainingDto trainingDto) {
        log.info("Adding new training for trainee: {}", trainingDto.getTrainee().getId());
        TraineeEntity trainee = traineeRepository.findById(trainingDto.getTrainee().getId())
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found"));
        TrainerEntity trainer = trainerRepository.findById(trainingDto.getTrainer().getId())
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found"));

        TrainingEntity training = new TrainingEntity();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(trainingDto.getTrainingName());
        training.setTrainingType(trainingDto.getTrainingType());
        training.setTrainingDate(trainingDto.getTrainingDate());
        training.setTrainingDuration(trainingDto.getTrainingDuration());

        validationUtils.validateTraining(training);

        trainingRepository.save(training);
        log.info("Training added successfully for trainee: {}", trainingDto.getTrainee().getId());
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
    public List<TrainingEntity> getTrainingsForTrainee(String traineeName, Date fromDate, Date toDate,
                                                       String trainerName, String trainingType) {

        log.info("Fetching trainings for trainee: {}", traineeName);

        validationUtils.validateTraineeTrainingsCriteria(traineeName, fromDate, toDate, trainerName, trainingType);

        TraineeEntity trainee = traineeRepository.findByTraineeFromUsername(traineeName)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found"));

        List<TrainingEntity> trainings = trainingRepository.findTrainingsForTrainee(trainee.getId(), fromDate, toDate,
                        trainerName, trainingType)
                .orElseThrow(() -> new ResourceNotFoundException("Trainings not found"));

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
    public List<TrainingEntity> getTrainingsForTrainer(String trainerUsername, Date fromDate, Date toDate,
                                                       String traineeName) {

        log.info("Fetching trainings for trainer: {}", trainerUsername);

        validationUtils.validateTrainerTrainingsCriteria(trainerUsername, fromDate, toDate, traineeName);

        TrainerEntity trainer = trainerRepository.findByTrainerFromUsername(trainerUsername)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found"));

        List<TrainingEntity> trainings = trainingRepository.findTrainingsForTrainer(trainer.getId(), fromDate, toDate,
                        traineeName)
                .orElseThrow(() -> new ResourceNotFoundException("Trainings not found"));

        return trainings;
    }
}
