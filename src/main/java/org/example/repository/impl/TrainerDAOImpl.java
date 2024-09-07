package org.example.repository.impl;

import java.util.ArrayList;
import java.util.List;
import org.example.entity.TrainerEntity;
import org.example.repository.TrainerDAO;
import org.example.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDAOImpl implements TrainerDAO {
    private final InMemoryStorage storage;

    @Autowired
    public TrainerDAOImpl(InMemoryStorage storage) {
        this.storage = storage;
    }

    @Override
    public void createTrainer(TrainerEntity trainerEntity) {
        storage.getTrainerStorage().put(trainerEntity.getUserId(), trainerEntity);
    }

    @Override
    public void updateTrainer(String userId, TrainerEntity trainerEntity) {
        storage.getTrainerStorage().put(userId, trainerEntity);
    }

    @Override
    public TrainerEntity getTrainer(String userId) {
        return storage.getTrainerStorage().get(userId);
    }

    @Override
    public List<TrainerEntity> getAllTrainers() {
        return new ArrayList<>(storage.getTrainerStorage().values());
    }
}
