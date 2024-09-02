package org.example.repository;

import org.example.entity.Training;

import java.util.List;

public interface TrainingDAO {
    void createTraining(Training training);
    void updateTraining(String trainingName, Training training);
    void deleteTraining(String trainingName);
    Training getTraining(String trainingName);
    List<Training> getAllTrainings();
}
