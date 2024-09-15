package org.example.repository.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
public class TrainerRepositoryImpl implements TrainerRepository {

    private final SessionFactory sessionFactory;

    /**
     * Saves a new {@link TrainerEntity} to the database.
     *
     * @param trainer the {@link TrainerEntity} to be saved
     */
    @Override
    public void save(TrainerEntity trainer) {
        sessionFactory.getCurrentSession().persist(trainer);
    }

    /**
     * Finds a {@link TrainerEntity} by its ID.
     *
     * @param trainerId the ID of the trainer
     * @return an {@link Optional} containing the found {@link TrainerEntity}, or empty if not found
     */
    @Override
    public Optional<TrainerEntity> findById(Long trainerId) {
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("from TrainerEntity where id = :trainerId", TrainerEntity.class)
                .setParameter("trainerId", trainerId)
                .uniqueResult());
    }

    /**
     * Finds a {@link TrainerEntity} by the username of the user associated with the trainer.
     *
     * @param username the username of the trainer
     * @return an {@link Optional} containing the found {@link TrainerEntity}, or empty if not found
     */
    @Override
    public Optional<TrainerEntity> findByTrainerFromUsername(String username) {
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("from UserEntity where username = :username", TrainerEntity.class)
                .setParameter("username", username)
                .uniqueResult());
    }

    /**
     * Finds all trainers.
     *
     * @return an {@link Optional} containing a list of {@link TrainerEntity}, or empty if none found
     */
    @Override
    public Optional<List<TrainerEntity>> findAll() {
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("from TrainerEntity", TrainerEntity.class)
                .getResultList());
    }

    /**
     * Finds all trainers assigned to a specific trainee.
     *
     * @param traineeId the ID of the trainee
     * @return an {@link Optional} containing a list of assigned {@link TrainerEntity}, or empty if none found
     */
    @Override
    public Optional<List<TrainerEntity>> findAssignedTrainers(Long traineeId) {
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("SELECT t FROM TrainerEntity t JOIN t.trainees tr WHERE tr.id = :traineeId",
                        TrainerEntity.class)
                .setParameter("traineeId", traineeId)
                .list());
    }

    /**
     * Finds all trainers by a list of IDs.
     *
     * @param trainerIds the list of trainer IDs
     * @return an {@link Optional} containing a list of {@link TrainerEntity}, or empty if none found
     */
    @Override
    public Optional<List<TrainerEntity>> findAllById(List<Long> trainerIds) {
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("FROM TrainerEntity t WHERE t.id IN :ids", TrainerEntity.class)
                .getResultList());
    }

    /**
     * Updates the given {@link TrainerEntity}.
     * This method uses `detach` instead of `merge` to avoid reattaching the entity.
     *
     * @param trainer the {@link TrainerEntity} to be updated
     */
    @Override
    public void update(TrainerEntity trainer) {
        sessionFactory.getCurrentSession()
                .detach(trainer);
    }
}
