package org.example.repository;

import org.example.entity.Trainee;

import java.util.List;

public interface TraineeDAO {
    void createTrainee(Trainee trainee);
    void updateTrainee(String userId, Trainee trainee);
    void deleteTrainee(String userId);
    Trainee getTrainee(String userId);
    List<Trainee> getAllTrainees();
}
