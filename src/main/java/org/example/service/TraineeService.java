package org.example.service;

import org.example.entity.TraineeEntity;
import org.example.repository.TraineeDAO;
import org.example.repository.UserDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing trainees.
 */
@Service
public class TraineeService {
    private final TraineeDAO traineeDao;
    private final UserDAO userDAO;

    public TraineeService(TraineeDAO traineeDao,UserDAO userDAO) {
        this.traineeDao = traineeDao;
        this.userDAO = userDAO;
    }

    public void updateTrainee(TraineeEntity traineeEntity) {
        traineeDao.updateTrainee(traineeEntity.getUserId(), traineeEntity);
    }

    public void createTrainee(TraineeEntity traineeEntity) {
        traineeDao.createTrainee(traineeEntity);
    }

    public void deleteTrainee(String traineeId) {
        userDAO.deleteByUsername(traineeId);
        traineeDao.deleteTrainee(traineeId);
    }

    public TraineeEntity getTrainee(String userId) {
        return traineeDao.getTrainee(userId);
    }

    public List<TraineeEntity> getAllTrainees() {
        return traineeDao.getAllTrainees();
    }

}
