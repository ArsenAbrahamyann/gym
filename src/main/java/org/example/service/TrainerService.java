package org.example.service;

import org.example.entity.TrainerEntity;
import org.example.repository.TrainerDAO;
import org.example.repository.UserDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing trainers.
 * <p>
 * This service provides operations for creating, updating, deleting, and retrieving trainer entities.
 * It interacts with the data access objects (DAOs) for persistence and retrieval of trainer data.
 * </p>
 */
@Service
public class TrainerService {

    private final TrainerDAO trainerDAO;
    private final UserDAO userDAO;

    /**
     * Constructs a new {@link TrainerService} instance with the provided DAOs.
     *
     * @param trainerDAO the DAO for managing trainer entities
     * @param userDAO the DAO for managing user entities
     */
    public TrainerService(TrainerDAO trainerDAO, UserDAO userDAO) {
        this.trainerDAO = trainerDAO;
        this.userDAO = userDAO;
    }

    /**
     * Creates a new trainer entity.
     * <p>
     * This method persists a new trainer entity in the data store. The trainer entity must
     * include all necessary details for creation.
     * </p>
     *
     * @param trainerEntity the trainer entity to be created
     */
    public void createTrainer(TrainerEntity trainerEntity) {
        trainerDAO.createTrainer(trainerEntity);
    }

    /**
     * Updates an existing trainer entity.
     * <p>
     * This method updates the trainer details in the data store. The trainer entity must contain
     * a valid user ID and the new details to be updated.
     * </p>
     *
     * @param userId the ID of the trainer to be updated
     * @param trainerEntity the trainer entity with updated details
     */
    public void updateTrainer(String userId, TrainerEntity trainerEntity) {
        trainerDAO.updateTrainer(userId, trainerEntity);
    }

    /**
     * Deletes a trainer entity by its ID.
     * <p>
     * This method removes a trainer entity from both the trainer data store and the user data store.
     * The trainer ID is used to identify the entity to be deleted.
     * </p>
     *
     * @param trainerId the ID of the trainer to be deleted
     */
    public void deleteTrainer(String trainerId) {
        userDAO.deleteByUsername(trainerId);
        trainerDAO.deleteTrainer(trainerId);
    }

    /**
     * Retrieves a trainer entity by its ID.
     * <p>
     * This method fetches the trainer entity from the data store based on the provided trainer ID.
     * </p>
     *
     * @param userId the ID of the trainer to retrieve
     * @return the trainer entity if found, or null if not found
     */
    public TrainerEntity getTrainer(String userId) {
        return trainerDAO.getTrainer(userId);
    }

    /**
     * Retrieves all trainer entities.
     * <p>
     * This method fetches the list of all trainer entities from the data store.
     * </p>
     *
     * @return a list of all trainer entities
     */
    public List<TrainerEntity> getAllTrainers() {
        return trainerDAO.getAllTrainers();
    }
}
