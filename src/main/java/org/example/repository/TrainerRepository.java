package org.example.repository;

import java.util.List;
import java.util.Optional;
import org.example.entity.TrainerEntity;

/**
 * Repository interface for managing {@link TrainerEntity} operations.
 */
public interface TrainerRepository {

    /**
     * Saves a new {@link TrainerEntity} to the database.
     *
     * @param trainer the {@link TrainerEntity} to be saved
     */
    void save(TrainerEntity trainer);

    /**
     * Finds a {@link TrainerEntity} by its ID.
     *
     * @param trainerId the ID of the trainer
     * @return an {@link Optional} containing the found {@link TrainerEntity}, or empty if not found
     */
    Optional<TrainerEntity> findById(Long trainerId);

    /**
     * Finds a {@link TrainerEntity} by the username of the associated user.
     *
     * @param username the username of the trainer's user
     * @return an {@link Optional} containing the found {@link TrainerEntity}, or empty if not found
     */
    Optional<TrainerEntity> findByTrainerFromUsername(String username);

    /**
     * Finds all {@link TrainerEntity} records in the database.
     *
     * @return an {@link Optional} containing a list of {@link TrainerEntity}, or empty if none found
     */
    List<TrainerEntity> findAll();

    /**
     * Finds all assigned trainers for a specific trainee.
     *
     * @param id the ID of the trainee
     * @return an {@link Optional} containing a list of assigned {@link TrainerEntity}, or empty if none found
     */
    List<TrainerEntity> findAssignedTrainers(Long id);

    /**
     * Finds all {@link TrainerEntity} records by a list of trainer IDs.
     *
     * @param trainerIds the list of trainer IDs
     * @return an {@link Optional} containing a list of {@link TrainerEntity}, or empty if none found
     */
    List<TrainerEntity> findAllById(List<Long> trainerIds);

    /**
     * Updates an existing {@link TrainerEntity}.
     *
     * @param trainer the {@link TrainerEntity} to be updated
     */
    void update(TrainerEntity trainer);

    List<TrainerEntity> findByUsernames(List<String> trainerUsernames);
}
