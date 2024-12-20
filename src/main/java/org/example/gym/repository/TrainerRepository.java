package org.example.gym.repository;

import java.util.List;
import java.util.Optional;
import org.example.gym.entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link TrainerEntity} operations.
 */
@Repository
public interface TrainerRepository extends JpaRepository<TrainerEntity, Long> {



    /**
     * Finds a {@link TrainerEntity} by the username of the associated user.
     *
     * @param username the username of the trainer's user
     * @return an {@link Optional} containing the found {@link TrainerEntity}, or empty if not found
     */
    Optional<TrainerEntity> findByUser_Username(String username);



    /**
     * Finds all assigned trainers for a specific trainee.
     *
     * @param traineeId the ID of the trainee
     * @return a {@link List} of assigned {@link TrainerEntity}, or an empty list if none found
     */
    @Query("SELECT t FROM TrainerEntity t JOIN t.trainees tr WHERE tr.id = :traineeId")
    List<TrainerEntity> findByTrainees_Id(@Param("traineeId") Long traineeId);


}
