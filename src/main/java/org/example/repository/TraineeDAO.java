package org.example.repository;

import org.example.entity.TraineeEntity;

import java.util.List;

public interface TraineeDAO {
    void createTrainee(TraineeEntity traineeEntity);
    void updateTrainee(String userId, TraineeEntity traineeEntity);
    void deleteTrainee(String userId);
    TraineeEntity getTrainee(String userId);
    List<TraineeEntity> getAllTrainees();
}
