package org.example.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.example.entity.TrainingEntity;

public interface TrainingRepository {
    Optional<TrainingEntity> findByTrainingName(String trainingName);

    void save(TrainingEntity training);

    List<TrainingEntity> findTrainingsForTrainee(Long traineeId, Date fromDate, Date toDate, String trainerName,
                                                 String trainingType);

    List<TrainingEntity> findTrainingsForTrainer(Long trainerId, Date fromDate, Date toDate, String traineeName);
}
