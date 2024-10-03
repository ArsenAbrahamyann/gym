package org.example.gym.repository.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.repository.TraineeRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the {@link TraineeRepository} interface, providing
 * CRUD operations and custom query methods for {@link TraineeEntity} using Hibernate.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class TraineeRepositoryImpl implements TraineeRepository {

    private final SessionFactory sessionFactory;

    /**
     * Finds a {@link TraineeEntity} by the username of the user associated with the trainee.
     *
     * @param username the username of the trainee
     * @return an {@link Optional} containing the found {@link TraineeEntity}, or empty if not found
     */
    @Override
    public Optional<TraineeEntity> findByTraineeFromUsername(String username) {
        log.debug("Finding TraineeEntity by username: {}", username);
        TraineeEntity trainee = sessionFactory.getCurrentSession()
                .createQuery("from TraineeEntity te where te.user.username = :username", TraineeEntity.class)
                .setParameter("username", username)
                .uniqueResult();

        if (trainee == null) {
            log.info("TraineeEntity with username: {} not found", username);
        } else {
            log.info("TraineeEntity with username: {} found", username);
        }

        return Optional.ofNullable(trainee);
    }

    /**
     * Finds a {@link TraineeEntity} by its ID.
     *
     * @param traineeId the ID of the trainee
     * @return an {@link Optional} containing the found {@link TraineeEntity}, or empty if not found
     */
    @Override
    public Optional<TraineeEntity> findById(Long traineeId) {
        log.debug("Finding TraineeEntity by ID: {}", traineeId);
        TraineeEntity trainee = sessionFactory.getCurrentSession()
                .createQuery("from TraineeEntity where id = :traineeId", TraineeEntity.class)
                .setParameter("traineeId", traineeId)
                .uniqueResult();

        if (trainee == null) {
            log.info("TraineeEntity with ID: {} not found", traineeId);
        } else {
            log.info("TraineeEntity with ID: {} found", traineeId);
        }

        return Optional.ofNullable(trainee);
    }

    /**
     * Saves a new {@link TraineeEntity} to the database.
     *
     * @param trainee the {@link TraineeEntity} to be saved
     */
    @Override
    public void save(TraineeEntity trainee) {
        log.debug("Saving TraineeEntity: {}", trainee);
        sessionFactory.getCurrentSession().merge(trainee);
        log.info("Successfully saved TraineeEntity: {}", trainee);

    }

    /**
     * Updates the given {@link TraineeEntity}.
     * This method uses `detach` to remove the entity from the session to prevent any automatic updates.
     *
     * @param trainee the {@link TraineeEntity} to be updated
     */
    @Override
    public void update(TraineeEntity trainee) {
        log.debug("Updating TraineeEntity: {}", trainee);
        sessionFactory.getCurrentSession().saveOrUpdate(trainee);
        log.info("Successfully updated TraineeEntity: {}", trainee);

    }

    @Override
    public void delete(TraineeEntity trainee) {
        log.debug("Deleting TraineeEntity: {}", trainee);
        sessionFactory.getCurrentSession().delete(trainee);
        log.info("Successfully deleted TraineeEntity: {}", trainee);

    }
}
