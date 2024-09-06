package org.example.service;

import java.util.List;
import java.util.Optional;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
import org.example.repository.UserDAO;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user entities.
 * <p>
 * This service provides operations for creating, updating, deleting, and retrieving user entities.
 * It interacts with the data access object (DAO) for persistence and retrieval of user data.
 * </p>
 */
@Service
public class UserService {
    private final UserDAO userDAO;

    /**
     * Constructs a new {@link UserService} instance with the provided DAO.
     *
     * @param userDAO the DAO for managing user entities
     */
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Creates or updates a user entity.
     * <p>
     * This method saves the provided user entity. If the user entity already exists in the data store,
     * it will be updated; otherwise, a new user entity will be created.
     * </p>
     *
     * @param userEntity the user entity to create or update
     */
    public void saveUser(UserEntity userEntity) {
        userDAO.save(userEntity);
    }

    /**
     * Finds a user entity by its username.
     * <p>
     * This method retrieves a user entity from the data store based on the provided username.
     * If a user entity with the given username exists, it is returned wrapped in an {@link Optional}.
     * If not, an empty {@link Optional} is returned.
     * </p>
     *
     * @param username the username of the user entity to find
     * @return an {@link Optional} containing the user entity if found, or empty otherwise
     */
    public Optional<UserEntity> findUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    /**
     * Finds a user entity by its username.
     * <p>
     * This method retrieves a user entity from the data store based on the provided username.
     * If a user entity with the given username exists, it is returned wrapped in an {@link Optional}.
     * If not, an empty {@link Optional} is returned.
     * </p>
     *
     * @param username the username of the user entity to find
     * @return an {@link Optional} containing the user entity if found, or empty otherwise
     */
    public void deleteUserByUsername(String username) {
        userDAO.deleteByUsername(username);
    }

    /**
     * Retrieves all user entities.
     * <p>
     * This method fetches the list of all user entities from the data store.
     * </p>
     *
     * @return a list of all user entities
     */
    public List<UserEntity> findAllUsers() {
        return userDAO.findAll();
    }

    /**
     * Updates an existing user entity.
     * <p>
     * This method updates the details of an existing user entity in the data store.
     * The user entity must include the updated details.
     * </p>
     *
     * @param userEntity the user entity with updated details
     */
    public void updateUser(UserEntity userEntity) {
        userDAO.updateUser(userEntity);
    }
}
