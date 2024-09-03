package org.example.service;

import org.example.entity.TraineeEntity;
import org.example.repository.TraineeDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing trainees.
 */
@Service
public class TraineeService {
    private final TraineeDAO traineeDao;

    public TraineeService(TraineeDAO traineeDao) {
        this.traineeDao = traineeDao;
    }

    public void updateTrainee(TraineeEntity traineeEntity) {
        traineeDao.updateTrainee(traineeEntity.getUserId(), traineeEntity);
    }

    public void createTrainee(TraineeEntity traineeEntity) {
        traineeDao.createTrainee(traineeEntity);
    }

    public void deleteTrainee(String userId) {
        traineeDao.deleteTrainee(userId);
    }

    public TraineeEntity getTrainee(String userId) {
        return traineeDao.getTrainee(userId);
    }

    public List<TraineeEntity> getAllTrainees() {
        return traineeDao.getAllTrainees();
    }

}
