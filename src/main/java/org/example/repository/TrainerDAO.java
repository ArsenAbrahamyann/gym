package org.example.repository;

import org.example.entity.Trainer;

import java.util.List;

public interface TrainerDAO {
    void createTrainer(Trainer trainer);
    void updateTrainer(String userId, Trainer trainer);
    Trainer getTrainer(String userId);
    void deleteTrainer(String userId);
    List<Trainer> getAllTrainers();
}
