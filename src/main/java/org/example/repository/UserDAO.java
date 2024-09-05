package org.example.repository;

import java.util.List;
import java.util.Optional;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;

/**
 * Data Access Object (DAO) interface for managing UserEntity persistence.
 */
public interface UserDAO {
    /**
     * Saves the given UserEntity to the database.
     *
     * @param userEntity the UserEntity to save
     */
    void save(UserEntity userEntity);

    /**
     * Finds a UserEntity by its username.
     *
     * @param username the username of the UserEntity to find
     * @return an Optional containing the UserEntity if found, or empty otherwise
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Deletes the UserEntity by its username.
     *
     * @param username the username of the UserEntity to delete
     */
    void deleteByUsername(String username);

    List<UserEntity> findAll();
    void updateUser(String username, UserEntity userEntity);

}

