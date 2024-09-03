package org.example.service;

import org.example.entity.TrainerEntity;
import org.example.repository.TrainerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing trainers.
 */
@Service
public class TrainerService {
    private final TrainerDAO trainerDao;

    public TrainerService(TrainerDAO trainerDao) {
        this.trainerDao = trainerDao;
    }

    public void createTrainer(TrainerEntity trainerEntity) {
        trainerDao.createTrainer(trainerEntity);
    }

    public void updateTrainer(TrainerEntity trainerEntity) {
        trainerDao.updateTrainer(trainerEntity.getUserId(), trainerEntity);
    }

    public void deleteTrainer(String userId) {
        trainerDao.deleteTrainer(userId);
    }

    public TrainerEntity getTrainer(String userId) {
        return trainerDao.getTrainer(userId);
    }

    public List<TrainerEntity> getAllTrainers() {
        return trainerDao.getAllTrainers();
    }
}
