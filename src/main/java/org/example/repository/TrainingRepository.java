package org.example.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.example.entity.TrainingEntity;


/**
 * Repository interface for managing {@link TrainingEntity} operations.
 */
public interface TrainingRepository {

    /**
     * Finds a {@link TrainingEntity} by its training name.
     *
     * @param trainingName the name of the training
     * @return an {@link Optional} containing the found {@link TrainingEntity}, or empty if not found
     */
    Optional<TrainingEntity> findByTrainingName(String trainingName);

    /**
     * Saves a new {@link TrainingEntity} to the database.
     *
     * @param training the {@link TrainingEntity} to be saved
     */
    void save(TrainingEntity training);

    /**
     * Finds trainings for a specific trainee based on optional criteria such as date range, trainer name, and training type.
     *
     * @param traineeId the ID of the trainee
     * @param fromDate the starting date of the training period
     * @param toDate the ending date of the training period
     * @param trainerName the name of the trainer (optional)
     * @param trainingType the type of training (optional)
     * @return an {@link Optional} containing a list of matching {@link TrainingEntity}, or empty if none found
     */
    Optional<List<TrainingEntity>> findTrainingsForTrainee(Long traineeId, LocalDateTime fromDate,
                                                         LocalDateTime toDate, String trainerName, String trainingType);

    /**
     * Finds trainings for a specific trainer based on optional criteria such as date range and trainee name.
     *
     * @param trainerId the ID of the trainer
     * @param fromDate the starting date of the training period
     * @param toDate the ending date of the training period
     * @param traineeName the name of the trainee (optional)
     * @return an {@link Optional} containing a list of matching {@link TrainingEntity}, or empty if none found
     */
    Optional<List<TrainingEntity>> findTrainingsForTrainer(Long trainerId, LocalDateTime fromDate,
                                                           LocalDateTime toDate, String traineeName);
}
