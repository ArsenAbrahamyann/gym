package org.example.gym.repository;

import java.util.List;
import java.util.Optional;
import org.example.gym.entity.UserEntity;

/**
 * Repository interface for managing {@link UserEntity} operations.
 */
public interface UserRepository {

    /**
     * Finds a {@link UserEntity} by its username.
     *
     * @param username the username of the user
     * @return an {@link Optional} containing the found {@link UserEntity}, or empty if not found
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Finds all usernames of the users in the system.
     *
     * @return an {@link Optional} containing a list of usernames, or empty if none found
     */
    List<String> findAllUsername();

    /**
     * Saves a new {@link UserEntity} to the database.
     *
     * @param user the {@link UserEntity} to be saved
     */
    UserEntity save(UserEntity user);

    /**
     * Updates an existing {@link UserEntity}.
     *
     * @param user the {@link UserEntity} to be updated
     */
    void update(UserEntity user);

    /**
     * Deletes a {@link UserEntity} by its username.
     *
     * @param username the username of the user to be deleted
     */
    void deleteByUsername(String username);
}
