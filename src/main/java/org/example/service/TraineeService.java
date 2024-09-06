package org.example.service;

import java.util.List;
import org.example.entity.TraineeEntity;
import org.example.repository.TraineeDAO;
import org.example.repository.UserDAO;
import org.springframework.stereotype.Service;

/**
 * Service class for managing trainees.
 * <p>
 * This service provides operations for creating, updating, deleting, and retrieving trainee entities.
 * It interacts with the data access objects (DAOs) for persistence and retrieval of trainee data.
 * </p>
 */
@Service
public class TraineeService {
    private final TraineeDAO traineeDao;
    private final UserDAO userDAO;

    /**
     * Constructs a new {@link TraineeService} instance with the provided DAOs.
     *
     * @param traineeDao the DAO for managing trainee entities
     * @param userDAO    the DAO for managing user entities
     */
    public TraineeService(TraineeDAO traineeDao, UserDAO userDAO) {
        this.traineeDao = traineeDao;
        this.userDAO = userDAO;
    }

    /**
     * Updates an existing trainee entity.
     * <p>
     * This method updates the trainee details in the data store. The trainee entity must contain
     * a valid user ID and the new details to be updated.
     * </p>
     *
     * @param traineeEntity the trainee entity with updated details
     */
    public void updateTrainee(TraineeEntity traineeEntity) {
        traineeDao.updateTrainee(traineeEntity.getUserId(), traineeEntity);
    }

    /**
     * Creates a new trainee entity.
     * <p>
     * This method persists a new trainee entity in the data store. The trainee entity must
     * include all necessary details for creation.
     * </p>
     *
     * @param traineeEntity the trainee entity to be created
     */
    public void createTrainee(TraineeEntity traineeEntity) {
        traineeDao.createTrainee(traineeEntity);
    }

    /**
     * Deletes a trainee entity by its ID.
     * <p>
     * This method removes a trainee entity from both the trainee data store and the user data store.
     * The trainee ID is used to identify the entity to be deleted.
     * </p>
     *
     * @param traineeId the ID of the trainee to be deleted
     */
    public void deleteTrainee(String traineeId) {
        userDAO.deleteByUsername(traineeId);
        traineeDao.deleteTrainee(traineeId);
    }

    /**
     * Retrieves a trainee entity by its ID.
     * <p>
     * This method fetches the trainee entity from the data store based on the provided trainee ID.
     * </p>
     *
     * @param userId the ID of the trainee to retrieve
     * @return the trainee entity if found, or null if not found
     */
    public TraineeEntity getTrainee(String userId) {
        return traineeDao.getTrainee(userId);
    }

    /**
     * Retrieves all trainee entities.
     * <p>
     * This method fetches the list of all trainee entities from the data store.
     * </p>
     *
     * @return a list of all trainee entities
     */
    public List<TraineeEntity> getAllTrainees() {
        return traineeDao.getAllTrainees();
    }

}
