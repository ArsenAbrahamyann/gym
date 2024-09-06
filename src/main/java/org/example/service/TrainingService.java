package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingEntity;
import org.example.repository.TrainingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing training entities.
 * <p>
 * This service provides operations for creating, updating, deleting, and retrieving training entities.
 * It interacts with the data access object (DAO) for persistence and retrieval of training data.
 * </p>
 */
@Service
@Slf4j
public class TrainingService {
    private final TrainingDAO trainingDao;

    /**
     * Constructs a new {@link TrainingService} instance with the provided DAO.
     *
     * @param trainingDao the DAO for managing training entities
     */
    public TrainingService(TrainingDAO trainingDao) {
        this.trainingDao = trainingDao;
    }

    /**
     * Creates a new training entity.
     * <p>
     * This method persists a new training entity in the data store. The training entity must
     * include all necessary details for creation.
     * </p>
     *
     * @param trainingEntity the training entity to be created
     */
    public void createTraining(TrainingEntity trainingEntity) {
        trainingDao.createTraining(trainingEntity);
    }

    /**
     * Updates an existing training entity.
     * <p>
     * This method updates the details of an existing training entity in the data store.
     * The training entity must include the updated details, and the training name is used
     * to identify which entity to update.
     * </p>
     *
     * @param trainingName the name of the training to be updated
     * @param existingTrainingEntity the training entity with updated details
     */
    public void updateTraining(String trainingName, TrainingEntity existingTrainingEntity) {
        trainingDao.updateTraining(trainingName, existingTrainingEntity);
    }

    /**
     * Deletes a training entity by its name.
     * <p>
     * This method removes a training entity from the data store based on the provided training name.
     * </p>
     *
     * @param trainingName the name of the training to be deleted
     */
    public void deleteTraining(String trainingName) {
        trainingDao.deleteTraining(trainingName);
    }

    /**
     * Retrieves a training entity by its name.
     * <p>
     * This method fetches a training entity from the data store based on the provided training name.
     * </p>
     *
     * @param trainingName the name of the training to retrieve
     * @return the training entity if found, or null if not found
     */
    public TrainingEntity getTraining(String trainingName) {
        return trainingDao.getTraining(trainingName);
    }

    /**
     * Retrieves all training entities.
     * <p>
     * This method fetches the list of all training entities from the data store.
     * </p>
     *
     * @return a list of all training entities
     */
    public List<TrainingEntity> getAllTrainings() {
        return trainingDao.getAllTrainings();
    }


}
