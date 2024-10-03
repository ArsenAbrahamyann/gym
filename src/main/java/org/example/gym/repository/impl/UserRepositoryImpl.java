package org.example.gym.repository.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.entity.UserEntity;
import org.example.gym.repository.UserRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the {@link UserRepository} interface, providing
 * CRUD operations for the {@link UserEntity} using Hibernate SessionFactory.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;


    /**
     * Finds a {@link UserEntity} by its username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the found {@link UserEntity}, or empty if not found
     */
    @Override
    public Optional<UserEntity> findByUsername(String username) {
        log.debug("Finding UserEntity by username: {}", username);
        UserEntity result = sessionFactory.getCurrentSession()
                .createQuery("from UserEntity where username = :username", UserEntity.class)
                .setParameter("username", username)
                .uniqueResult();

        if (result == null) {
            log.info("UserEntity with username: {} not found", username);
        } else {
            log.info("UserEntity with username: {} found", username);
        }

        return Optional.ofNullable(result);
    }

    /**
     * Retrieves all usernames from the database.
     *
     * @return an {@link Optional} containing a {@link List} of usernames, or an empty {@link Optional} if none are found
     */
    @Override
    public List<String> findAllUsername() {
        log.debug("Retrieving all usernames from the database");
        List<String> usernames = sessionFactory.getCurrentSession()
                .createQuery("select u.username from UserEntity u", String.class)
                .getResultList();

        if (usernames.isEmpty()) {
            log.info("No usernames found");
            return Collections.emptyList();
        } else {
            log.info("Retrieved {} usernames", usernames.size());
        }

        return usernames;
    }

    /**
     * Saves a new {@link UserEntity} to the database.
     *
     * @param user the {@link UserEntity} to be saved
     */
    @Override
    public UserEntity save(UserEntity user) {
        log.debug("Saving UserEntity with ID: {}", user.getId());
        if (user.getId() == null) {
            sessionFactory.getCurrentSession().persist(user);
            log.info("UserEntity with ID: {} persisted", user.getId());
        } else {
            UserEntity updatedUser = (UserEntity) sessionFactory.getCurrentSession().merge(user);
            log.info("UserEntity with ID: {} merged", user.getId());
            return updatedUser;
        }
        return user;
    }

    /**
     * Updates an existing {@link UserEntity} in the database.
     * The method detaches the entity from the session to ensure it can be modified.
     *
     * @param user the {@link UserEntity} to be updated
     */
    @Override
    public void update(UserEntity user) {
        log.debug("Updating UserEntity with ID: {}", user.getId());
        sessionFactory.getCurrentSession().update(user);
        log.info("UserEntity with ID: {} updated", user.getId());
    }

    /**
     * Deletes a {@link UserEntity} from the database by its username.
     *
     * @param username the username of the {@link UserEntity} to be deleted
     */
    @Override
    public void deleteByUsername(String username) {
        log.debug("Deleting UserEntity by username: {}", username);
        Optional<UserEntity> user = findByUsername(username);
        if (user.isPresent()) {
            sessionFactory.getCurrentSession().delete(user.get());
            log.info("UserEntity with username: {} deleted", username);
        } else {
            log.info("No UserEntity found with username: {}", username);
        }
    }
}
