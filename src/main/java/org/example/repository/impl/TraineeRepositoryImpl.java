package org.example.repository.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.entity.TraineeEntity;
import org.example.entity.UserEntity;
import org.example.repository.TraineeRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the {@link TraineeRepository} interface, providing
 * CRUD operations and custom query methods for {@link TraineeEntity} using Hibernate.
 */
@Repository
@RequiredArgsConstructor
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
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("from TraineeEntity te where te.user.username = :username", TraineeEntity.class)
                .setParameter("username", username)
                .uniqueResult());
    }

    /**
     * Finds a {@link TraineeEntity} by its ID.
     *
     * @param traineeId the ID of the trainee
     * @return an {@link Optional} containing the found {@link TraineeEntity}, or empty if not found
     */
    @Override
    public Optional<TraineeEntity> findById(Long traineeId) {
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("from TraineeEntity where id = :traineeId", TraineeEntity.class)
                .setParameter("traineeId", traineeId)
                .uniqueResult());
    }

    /**
     * Saves a new {@link TraineeEntity} to the database.
     *
     * @param trainee the {@link TraineeEntity} to be saved
     */
    @Override
    public void save(TraineeEntity trainee) {
        sessionFactory.getCurrentSession().merge(trainee);
    }

    /**
     * Updates the given {@link TraineeEntity}.
     * This method uses `detach` to remove the entity from the session to prevent any automatic updates.
     *
     * @param trainee the {@link TraineeEntity} to be updated
     */
    @Override
    public void update(TraineeEntity trainee) {
        sessionFactory.getCurrentSession()
                .saveOrUpdate(trainee);
    }

    @Override
    public void delete(TraineeEntity trainee) {
        if (trainee != null) {
            Session session = sessionFactory.getCurrentSession();
            if (!session.contains(trainee)) {
                trainee = (TraineeEntity) session.merge(trainee);
            }
            session.delete(trainee);
        }
    }
}
