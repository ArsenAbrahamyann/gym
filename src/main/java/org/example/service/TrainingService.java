package org.example.service;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainingDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.exeption.ResourceNotFoundException;
import org.example.exeption.ValidationException;
import org.example.repository.TraineeRepository;
import org.example.repository.TrainerRepository;
import org.example.repository.TrainingRepository;
import org.example.repository.TrainingTypeRepository;
import org.example.repository.UserRepository;
import org.example.utils.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final TrainingTypeRepository trainingTypeRepository;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final ValidationUtils validationUtils;
    private final ModelMapper modelMapper;

    /**
     * Adds a new training record.
     *
     * @param trainingDto The details of the training to be added.
     * @throws EntityNotFoundException If the specified trainee or trainer is not found.
     */
    @Transactional
    public void addTraining(TrainingDto trainingDto) {
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTrainee(traineeRepository.findById(trainingDto.getTraineeId())
                .orElseThrow(() -> new ValidationException("Trainee not found")));
        trainingEntity.setTrainer(trainerRepository.findById(trainingDto.getTrainerId())
                .orElseThrow(() -> new ValidationException("Trainer not found")));
        trainingEntity.setTrainingName(trainingDto.getTrainingName());
        trainingEntity.setTrainingType(trainingTypeRepository.findById(trainingDto.getTrainingTypeId())
                .orElseThrow(() -> new ValidationException("Training type not found")));
        trainingEntity.setTrainingDuration(trainingDto.getTrainingDuration());

        trainingRepository.save(trainingEntity);
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
    @Transactional
    public List<TrainingEntity> getTrainingsForTrainer(String trainerUsername, LocalDateTime fromDate, LocalDateTime toDate,
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
