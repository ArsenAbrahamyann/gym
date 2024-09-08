package org.example.repository;

import java.util.List;
import org.example.entity.TrainingEntity;

public interface TrainingDao {
    void createTraining(TrainingEntity trainingEntity);

    TrainingEntity getTraining(String trainingName);

    List<TrainingEntity> getAllTrainings();
}
