package org.example.repository;

import java.util.List;
import org.example.entity.TrainerEntity;

public interface TrainerDao {
    void createTrainer(TrainerEntity trainerEntity);

    void updateTrainer(String userId, TrainerEntity trainerEntity);

    TrainerEntity getTrainer(String userId);

    List<TrainerEntity> getAllTrainers();
}
