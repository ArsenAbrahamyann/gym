package org.example.repository;

import java.util.Optional;
import org.example.entity.TraineeEntity;
import org.example.entity.UserEntity;

/**
 * Repository interface for managing {@link TraineeEntity} operations.
 */
public interface TraineeRepository {

    /**
     * Finds a {@link TraineeEntity} by the username of the associated user.
     *
     * @param username the username of the trainee's user
     * @return an {@link Optional} containing the found {@link TraineeEntity}, or empty if not found
     */
    Optional<TraineeEntity> findByTraineeFromUsername(String username);

    /**
     * Finds a {@link TraineeEntity} by its ID.
     *
     * @param traineeId the ID of the trainee
     * @return an {@link Optional} containing the found {@link TraineeEntity}, or empty if not found
     */
    Optional<TraineeEntity> findById(Long traineeId);

    /**
     * Saves a new {@link TraineeEntity} to the database.
     *
     * @param trainee the {@link TraineeEntity} to be saved
     */
    void save(TraineeEntity trainee);

    /**
     * Updates an existing {@link TraineeEntity}.
     *
     * @param trainee the {@link TraineeEntity} to be updated
     */
    void update(TraineeEntity trainee);
}
