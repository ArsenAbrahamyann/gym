package org.example.repository;

import java.util.List;
import org.example.entity.TraineeEntity;

public interface TraineeDAO {
    void createTrainee(TraineeEntity traineeEntity);

    void updateTrainee(String userId, TraineeEntity traineeEntity);

    void deleteTrainee(String userId);

    TraineeEntity getTrainee(String userId);

    List<TraineeEntity> getAllTrainees();
}
