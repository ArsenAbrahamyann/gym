package org.example.repository;

import java.util.List;
import org.example.entity.TrainerEntity;

public interface TrainerDAO {
    void createTrainer(TrainerEntity trainerEntity);

    void updateTrainer(String userId, TrainerEntity trainerEntity);

    TrainerEntity getTrainer(String userId);

    List<TrainerEntity> getAllTrainers();
}
