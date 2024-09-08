package org.example.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.entity.TrainingEntity;
import org.example.repository.TrainingDao;
import org.springframework.stereotype.Service;

/**
 * Service class for managing training entities.
 * <p>
 * This service provides methods for creating, updating, deleting, and retrieving training entities.
 * It interacts with the {@link TrainingDao} to handle persistence and retrieval of training data.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class TrainingService {
    private final TrainingDao trainingDao;

    /**
     * Creates a new training entity in the data store.
     * <p>
     * This method persists the provided training entity. The entity must include all necessary details
     * required for creation. Once created, the training entity is stored in the data store for future retrieval.
     * </p>
     *
     * @param trainingEntity the training entity to be created
     */
    public void createTraining(TrainingEntity trainingEntity) {
        trainingDao.createTraining(trainingEntity);
    }

    /**
     * Retrieves a training entity by its name.
     * <p>
     * This method fetches the training entity from the data store based on the provided training name.
     * If no training entity is found with the given name, the method returns null.
     * </p>
     *
     * @param trainingName the name of the training to retrieve
     * @return the training entity if found, or null if not found
     */
    public TrainingEntity getTraining(String trainingName) {
        return trainingDao.getTraining(trainingName);
    }

    /**
     * Retrieves all training entities from the data store.
     * <p>
     * This method fetches a list of all training entities currently stored in the data store.
     * </p>
     *
     * @return a list of all training entities
     */
    public List<TrainingEntity> getAllTrainings() {
        return trainingDao.getAllTrainings();
    }


}
