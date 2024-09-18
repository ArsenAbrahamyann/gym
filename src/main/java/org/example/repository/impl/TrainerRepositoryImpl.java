package org.example.repository.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainerEntity;
import org.example.repository.TrainerRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the {@link TrainerRepository} interface, providing
 * CRUD operations and custom query methods for {@link TrainerEntity} using Hibernate.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class TrainerRepositoryImpl implements TrainerRepository {

    private final SessionFactory sessionFactory;

    /**
     * Saves or updates a {@link TrainerEntity} in the database.
     * If the entity is new, it will be inserted. If it exists, it will be updated.
     *
     * @param trainer the {@link TrainerEntity} to be saved or updated
     */
    @Override
    public void save(TrainerEntity trainer) {
        log.debug("Saving or updating TrainerEntity with ID: {}", trainer.getId());
        sessionFactory.getCurrentSession().saveOrUpdate(trainer);
        log.info("TrainerEntity with ID: {} saved or updated successfully", trainer.getId());
    }

    /**
     * Finds a {@link TrainerEntity} by its ID.
     *
     * @param trainerId the ID of the trainer
     * @return an {@link Optional} containing the found {@link TrainerEntity}, or empty if not found
     */
    @Override
    public Optional<TrainerEntity> findById(Long trainerId) {
        log.debug("Finding TrainerEntity with ID: {}", trainerId);
        TrainerEntity trainer = sessionFactory.getCurrentSession()
                .createQuery("from TrainerEntity where id = :trainerId", TrainerEntity.class)
                .setParameter("trainerId", trainerId)
                .uniqueResult();
        if (trainer != null) {
            log.info("TrainerEntity with ID: {} found", trainerId);
        } else {
            log.warn("TrainerEntity with ID: {} not found", trainerId);
        }
        return Optional.ofNullable(trainer);
    }

    /**
     * Finds a {@link TrainerEntity} by the username of the user associated with the trainer.
     *
     * @param username the username of the trainer
     * @return an {@link Optional} containing the found {@link TrainerEntity}, or empty if not found
     */
    @Override
    public Optional<TrainerEntity> findByTrainerFromUsername(String username) {
        log.debug("Finding TrainerEntity by username: {}", username);
        TrainerEntity trainer = sessionFactory.getCurrentSession()
                .createQuery("from TrainerEntity te where te.user.username = :username", TrainerEntity.class)
                .setParameter("username", username)
                .uniqueResult();
        if (trainer != null) {
            log.info("TrainerEntity for username: {} found", username);
        } else {
            log.warn("TrainerEntity for username: {} not found", username);
        }
        return Optional.ofNullable(trainer);
    }

    /**
     * Finds all trainers.
     *
     * @return an {@link Optional} containing a list of {@link TrainerEntity}, or empty if none found
     */
    @Override
    public Optional<List<TrainerEntity>> findAll() {
        log.debug("Finding all TrainerEntity instances");
        List<TrainerEntity> trainers = sessionFactory.getCurrentSession()
                .createQuery("from TrainerEntity", TrainerEntity.class)
                .getResultList();
        if (trainers.isEmpty()) {
            log.info("No TrainerEntity instances found");
        } else {
            log.info("Found {} TrainerEntity instances", trainers.size());
        }
        return Optional.ofNullable(trainers);
    }

    /**
     * Finds all trainers assigned to a specific trainee.
     *
     * @param traineeId the ID of the trainee
     * @return an {@link Optional} containing a list of assigned {@link TrainerEntity}, or empty if none found
     */
    @Override
    public Optional<List<TrainerEntity>> findAssignedTrainers(Long traineeId) {
        log.debug("Finding trainers assigned to trainee with ID: {}", traineeId);
        List<TrainerEntity> trainers = sessionFactory.getCurrentSession()
                .createQuery("SELECT t FROM TrainerEntity t JOIN t.trainees tr WHERE tr.id = :traineeId",
                        TrainerEntity.class)
                .setParameter("traineeId", traineeId)
                .list();
        if (trainers.isEmpty()) {
            log.info("No trainers assigned to trainee with ID: {}", traineeId);
        } else {
            log.info("Found {} trainers assigned to trainee with ID: {}", trainers.size(), traineeId);
        }
        return Optional.of(trainers);
    }

    /**
     * Finds all trainers by a list of IDs.
     *
     * @param trainerIds the list of trainer IDs
     * @return an {@link Optional} containing a list of {@link TrainerEntity}, or empty if none found
     */
    @Override
    public Optional<List<TrainerEntity>> findAllById(List<Long> trainerIds) {
        log.debug("Finding trainers by IDs: {}", trainerIds);
        List<TrainerEntity> trainers = sessionFactory.getCurrentSession()
                .createQuery("FROM TrainerEntity t WHERE t.id IN :ids", TrainerEntity.class)
                .setParameter("ids", trainerIds)
                .getResultList();
        if (trainers.isEmpty()) {
            log.info("No trainers found for IDs: {}", trainerIds);
        } else {
            log.info("Found {} trainers for IDs: {}", trainers.size(), trainerIds);
        }
        return Optional.ofNullable(trainers);
    }

    /**
     * Updates the given {@link TrainerEntity}.
     * This method uses `detach` instead of `merge` to avoid reattaching the entity.
     *
     * @param trainer the {@link TrainerEntity} to be updated
     */
    @Override
    public void update(TrainerEntity trainer) {
        log.debug("Updating TrainerEntity with ID: {}", trainer.getId());
        sessionFactory.getCurrentSession().saveOrUpdate(trainer);
        log.info("TrainerEntity with ID: {} updated successfully", trainer.getId());
    }
}
