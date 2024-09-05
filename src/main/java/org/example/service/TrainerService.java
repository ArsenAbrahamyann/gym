package org.example.service;

import org.example.entity.TrainerEntity;
import org.example.repository.TrainerDAO;
import org.example.repository.UserDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing trainers.
 */
@Service
public class TrainerService {
    private final TrainerDAO trainerDAO;
    private final UserDAO userDAO;

    public TrainerService(TrainerDAO trainerDAO,UserDAO userDAO) {
        this.trainerDAO = trainerDAO;
        this.userDAO = userDAO;
    }

    public void createTrainer(TrainerEntity trainerEntity) {
        trainerDAO.createTrainer(trainerEntity);
    }

    public void updateTrainer(String userId,TrainerEntity trainerEntity) {
        trainerDAO.updateTrainer(userId, trainerEntity);
    }

    public void deleteTrainer(String trainerId) {
        userDAO.deleteByUsername(trainerId);
        trainerDAO.deleteTrainer(trainerId);
    }

    public TrainerEntity getTrainer(String userId) {
        return trainerDAO.getTrainer(userId);
    }

    public List<TrainerEntity> getAllTrainers() {
        return trainerDAO.getAllTrainers();
    }
}
