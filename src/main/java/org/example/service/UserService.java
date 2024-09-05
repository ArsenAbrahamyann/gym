package org.example.service;

import java.util.List;
import java.util.Optional;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
import org.example.repository.UserDAO;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Creates or updates a UserEntity.
     *
     * @param userEntity the UserEntity to create or update
     */
    public void saveUser(UserEntity userEntity) {
        userDAO.save(userEntity);
    }

    /**
     * Finds a UserEntity by its username.
     *
     * @param username the username of the UserEntity to find
     * @return an Optional containing the UserEntity if found, or empty otherwise
     */
    public Optional<UserEntity> findUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    /**
     * Deletes a UserEntity by its username.
     *
     * @param username the username of the UserEntity to delete
     */
    public void deleteUserByUsername(String username) {
        userDAO.deleteByUsername(username);
    }

    public List<UserEntity> findAllUsers() {
        return userDAO.findAll();
    }
    public void updateUser(String username, UserEntity userEntity) {
        userDAO.updateUser(username, userEntity);
    }
}
