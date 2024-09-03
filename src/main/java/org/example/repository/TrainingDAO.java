package org.example.repository;

import org.example.entity.TrainingEntity;

import java.util.List;

public interface TrainingDAO {
    void createTraining(TrainingEntity trainingEntity);
    void updateTraining(String trainingName, TrainingEntity trainingEntity);
    void deleteTraining(String trainingName);
    TrainingEntity getTraining(String trainingName);
    List<TrainingEntity> getAllTrainings();
}
