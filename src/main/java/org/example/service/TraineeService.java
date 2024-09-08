package org.example.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.entity.TraineeEntity;
import org.example.repository.TraineeDao;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for managing trainee entities.
 * <p>
 * This service provides methods for creating, updating, deleting, and retrieving trainee entities.
 * It interacts with the {@link TraineeDao} to handle persistence and retrieval of trainee data.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class TraineeService {
    private final TraineeDao traineeDao;

    /**
     * Creates a new trainee entity in the data store.
     * <p>
     * The provided trainee entity must include all necessary details for creation. This method persists
     * the new trainee entity in the data store.
     * </p>
     *
     * @param traineeEntity the trainee entity to be created
     */
    public void createTrainee(TraineeEntity traineeEntity) {
        traineeDao.createTrainee(traineeEntity);
    }

    /**
     * Updates an existing trainee entity in the data store.
     * <p>
     * The provided trainee entity must include a valid user ID and the new details to be updated.
     * The update is performed using the trainee's ID to locate the existing record.
     * </p>
     *
     * @param traineeEntity the trainee entity with updated details
     */
    public void updateTrainee(TraineeEntity traineeEntity) {
        traineeDao.updateTrainee(traineeEntity.getUserId(), traineeEntity);
    }

    /**
     * Deletes a trainee entity from the data store.
     * <p>
     * This method removes the trainee entity identified by the provided username. It deletes the trainee
     * from both the trainee and user data stores.
     * </p>
     *
     * @param username the ID of the trainee to be deleted
     */
    public void deleteTrainee(String username) {
        traineeDao.deleteTrainee(username);
    }

    /**
     * Retrieves a trainee entity by its ID.
     * <p>
     * This method fetches the trainee entity from the data store using the provided user ID. If no trainee
     * is found with the given ID, the method returns null.
     * </p>
     *
     * @param userId the ID of the trainee to retrieve
     * @return the trainee entity if found, or null if not found
     */
    public TraineeEntity getTrainee(String userId) {
        return traineeDao.getTrainee(userId);
    }

    /**
     * Retrieves all trainee entities from the data store.
     * <p>
     * This method fetches a list of all trainee entities stored in the data store.
     * </p>
     *
     * @return a list of all trainee entities
     */
    public List<TraineeEntity> getAllTrainees() {
        return traineeDao.getAllTrainees();
    }


}
