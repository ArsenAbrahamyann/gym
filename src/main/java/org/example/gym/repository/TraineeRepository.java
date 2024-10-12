package org.example.gym.repository;

import java.util.Optional;
import org.example.gym.entity.TraineeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link TraineeEntity} operations.
 * Provides methods to perform CRUD operations on Trainee entities.
 */
@Repository
public interface TraineeRepository extends JpaRepository<TraineeEntity, Long> {

    /**
     * Finds a {@link TraineeEntity} by the username of the associated user.
     *
     * @param username the username of the trainee's user
     * @return an {@link Optional} containing the found {@link TraineeEntity},
     *         or empty if not found
     */
    Optional<TraineeEntity> findByUsername(String username);
}
