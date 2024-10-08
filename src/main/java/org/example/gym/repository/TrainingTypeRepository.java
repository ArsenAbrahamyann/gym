package org.example.gym.repository;

import java.util.Optional;
import org.example.gym.entity.TrainingTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 */
@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingTypeEntity, Long> {

    /**
     * Finds a {@link TrainingTypeEntity} by its ID.
     *
     * @param trainingTypeId the ID of the training type to find
     * @return an {@link Optional} containing the matching {@link TrainingTypeEntity}, or empty if none found
     */
    Optional<TrainingTypeEntity> findById(Long trainingTypeId);

    /**
     * Finds a {@link TrainingTypeEntity} by its training name.
     *
     * @param trainingTypeName the name of the training
     * @return an {@link Optional} containing the found {@link TrainingTypeEntity}, or empty if not found
     */
    Optional<TrainingTypeEntity> findByTrainingTypeName(String trainingTypeName);
}
