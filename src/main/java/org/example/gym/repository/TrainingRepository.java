package org.example.gym.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.example.gym.entity.TrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link TrainingEntity} operations.
 */
@Repository
public interface TrainingRepository extends JpaRepository<TrainingEntity, Long> {

    /**
     * Finds trainings for a specific trainee based on optional criteria such as date range, trainer name, and training type.
     *
     * @param traineeName the ID of the trainee
     * @param fromDate  the starting date of the training period
     * @param toDate    the ending date of the training period
     * @param trainerName the name of the trainer (optional)
     * @param trainingType the type of training (optional)
     * @return a list of matching {@link TrainingEntity}, or empty if none found
     */
    @Query("SELECT t FROM TrainingEntity t WHERE t.trainee.username = :traineeName "
            + "AND t.trainingDate BETWEEN :fromDate AND :toDate"
            + " AND (:trainerName IS NULL OR t.trainer.username = :trainerName)"
            + " AND (:trainingType IS NULL OR t.trainingType.trainingTypeName = :trainingType)")
    List<TrainingEntity> findTrainingsForTrainee(@Param("traineeName") String traineeName,
                                                 @Param("fromDate") LocalDateTime fromDate,
                                                 @Param("toDate") LocalDateTime toDate,
                                                 @Param("trainerName") String trainerName,
                                                 @Param("trainingType") String trainingType);

    /**
     * Finds trainings for a specific trainer based on optional criteria such as date range and trainee name.
     *
     * @param trainerId   the ID of the trainer
     * @param fromDate    the starting date of the training period
     * @param toDate      the ending date of the training period
     * @param traineeName the name of the trainee (optional)
     * @return a list of matching {@link TrainingEntity}, or empty if none found
     */
    @Query("SELECT t FROM TrainingEntity t WHERE t.trainer.id = :trainerId"
            + " AND t.trainingDate BETWEEN :fromDate AND :toDate"
            + " AND (:traineeName IS NULL OR t.trainee.username = :traineeName)")
    List<TrainingEntity> findTrainingsForTrainer(@Param("trainerId") Long trainerId,
                                                 @Param("fromDate") LocalDateTime fromDate,
                                                 @Param("toDate") LocalDateTime toDate,
                                                 @Param("traineeName") String traineeName);
}
