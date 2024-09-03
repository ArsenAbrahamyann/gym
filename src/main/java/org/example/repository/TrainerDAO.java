package org.example.repository;

import org.example.entity.TrainerEntity;

import java.util.List;

public interface TrainerDAO {
    void createTrainer(TrainerEntity trainerEntity);
    void updateTrainer(String userId, TrainerEntity trainerEntity);
    TrainerEntity getTrainer(String userId);
    void deleteTrainer(String userId);
    List<TrainerEntity> getAllTrainers();
}
