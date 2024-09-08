package org.example.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.entity.TrainerEntity;
import org.example.repository.TrainerDao;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for managing trainer entities.
 * <p>
 * This service provides methods for creating, updating, deleting, and retrieving trainer entities.
 * It interacts with the {@link TrainerDao} to handle persistence and retrieval of trainer data.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerDao trainerDao;

    /**
     * Creates a new trainer entity in the data store.
     * <p>
     * This method persists the provided trainer entity. The entity must include all necessary details
     * required for creation. It is stored in the data store for future retrieval and management.
     * </p>
     *
     * @param trainerEntity the trainer entity to be created
     */
    public void createTrainer(TrainerEntity trainerEntity) {
        trainerDao.createTrainer(trainerEntity);
    }

    /**
     * Updates an existing trainer entity in the data store.
     * <p>
     * This method updates the details of an existing trainer entity. The trainer entity must include a valid
     * user ID and the new details to be updated. The update is performed using the provided user ID to locate
     * the existing record.
     * </p>
     *
     * @param userId        the ID of the trainer to be updated
     * @param trainerEntity the trainer entity with updated details
     */
    public void updateTrainer(String userId, TrainerEntity trainerEntity) {
        trainerDao.updateTrainer(userId, trainerEntity);
    }

    /**
     * Retrieves a trainer entity by its ID.
     * <p>
     * This method fetches the trainer entity from the data store using the provided user ID. If no trainer is
     * found with the given ID, the method returns null.
     * </p>
     *
     * @param userId the ID of the trainer to retrieve
     * @return the trainer entity if found, or null if not found
     */
    public TrainerEntity getTrainer(String userId) {
        return trainerDao.getTrainer(userId);
    }

    /**
     * Retrieves all trainer entities from the data store.
     * <p>
     * This method fetches a list of all trainer entities currently stored in the data store.
     * </p>
     *
     * @return a list of all trainer entities
     */
    public List<TrainerEntity> getAllTrainers() {
        return trainerDao.getAllTrainers();
    }


}
