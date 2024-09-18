package org.example.repository;

import java.util.Optional;
import org.example.entity.TrainingTypeEntity;

/**
 * Repository interface for managing {@link org.example.entity.TrainingTypeEntity} operations.
 */
public interface TrainingTypeRepository {

    /**
     * Saves a new {@link org.example.entity.TrainingTypeEntity} to the database.
     *
     * @param trainingTypeEntity the {@link org.example.entity.TrainingTypeEntity} to be saved
     */
    void save(TrainingTypeEntity trainingTypeEntity);

    /**
     * find by id {@param trainingTypeId}
     * @return TrainingTypeEntity
     */
    Optional<TrainingTypeEntity> findById(Long trainingTypeId);
}
