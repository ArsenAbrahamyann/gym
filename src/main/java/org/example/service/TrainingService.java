package org.example.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final ValidationUtils validationUtils;

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

    public List<TrainingEntity> getTrainingsForTrainee(String traineeName, Date fromDate, Date toDate,
                                                    String trainerName, String trainingType) {

        log.info("Fetching trainings for trainee: {}", traineeName);

        validationUtils.validateTraineeTrainingsCriteria(traineeName, fromDate, toDate, trainerName, trainingType);

        TraineeEntity trainee = traineeRepository.findByTraineeFromUsername(traineeName)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found"));

        // Fetch and filter trainings based on criteria
        List<TrainingEntity> trainings = trainingRepository.findTrainingsForTrainee(trainee.getId(), fromDate, toDate,
                trainerName, trainingType)
                .orElseThrow(() -> new ResourceNotFoundException("Trainings not found"));

        return trainings;
    }

    public List<TrainingEntity> getTrainingsForTrainer(String trainerUsername, Date fromDate, Date toDate,
                                                    String traineeName) {

        log.info("Fetching trainings for trainer: {}", trainerUsername);

        validationUtils.validateTrainerTrainingsCriteria(trainerUsername, fromDate, toDate, traineeName);

        TrainerEntity trainer = trainerRepository.findByTrainerFromUsername(trainerUsername)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found"));

        // Fetch and filter trainings based on criteria
        List<TrainingEntity> trainings = trainingRepository.findTrainingsForTrainer(trainer.getId(), fromDate, toDate,
                traineeName)
                .orElseThrow(() -> new ResourceNotFoundException("Trainings not found"));

        return trainings;
    }

}
