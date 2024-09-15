package org.example.repository.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the {@link UserRepository} interface, providing
 * CRUD operations for the {@link UserEntity} using Hibernate SessionFactory.
 */
@Repository
@RequiredArgsConstructor
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
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("from UserEntity where username = :username", UserEntity.class)
                .setParameter("username", username)
                .uniqueResult());
    }

    /**
     * Retrieves all usernames from the database.
     *
     * @return an {@link Optional} containing a {@link List} of usernames, or an empty {@link Optional} if none are found
     */
    @Override
    public Optional<List<String>> findAllUsername() {
        List<String> usernames = sessionFactory.getCurrentSession()
                .createQuery("select u.username from UserEntity u", String.class)
                .getResultList();
        return Optional.of(usernames);
    }

    /**
     * Saves a new {@link UserEntity} to the database.
     *
     * @param user the {@link UserEntity} to be saved
     */
    @Override
    public void save(UserEntity user) {
        sessionFactory.getCurrentSession().persist(user);
    }

    /**
     * Updates an existing {@link UserEntity} in the database.
     * The method detaches the entity from the session to ensure it can be modified.
     *
     * @param user the {@link UserEntity} to be updated
     */
    @Override
    public void update(UserEntity user) {
        sessionFactory.getCurrentSession().detach(user);
    }

    /**
     * Deletes a {@link UserEntity} from the database by its username.
     *
     * @param username the username of the {@link UserEntity} to be deleted
     */
    @Override
    public void deleteByUsername(String username) {
        Optional<UserEntity> user = findByUsername(username);
        if (user.isPresent()) {
            UserEntity userEntity = user.get();
            sessionFactory.getCurrentSession().remove(userEntity);
        }
    }
}
