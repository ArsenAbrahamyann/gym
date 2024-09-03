package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingEntity;
import org.example.repository.TrainingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing trainings.
 */
@Service
@Slf4j
public class TrainingService {
    private final TrainingDAO trainingDao;

    @Autowired
    public TrainingService(TrainingDAO trainingDao) {
        this.trainingDao = trainingDao;
    }

    public void createTraining(TrainingEntity trainingEntity) {
        trainingDao.createTraining(trainingEntity);
    }

    public void updateTraining(String trainingName, TrainingEntity existingTrainingEntity) {
        trainingDao.updateTraining(trainingName, existingTrainingEntity);
    }

    public void deleteTraining(String trainingName) {
        trainingDao.deleteTraining(trainingName);
    }


    public TrainingEntity getTraining(String trainingName) {
        return trainingDao.getTraining(trainingName);
    }

    public List<TrainingEntity> getAllTrainings() {
        return trainingDao.getAllTrainings();
    }


}
