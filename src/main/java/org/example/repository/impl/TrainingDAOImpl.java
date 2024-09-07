package org.example.repository.impl;

import java.util.ArrayList;
import java.util.List;
import org.example.entity.TrainingEntity;
import org.example.repository.TrainingDAO;
import org.example.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDAOImpl implements TrainingDAO {
    private final InMemoryStorage storage;

    @Autowired
    public TrainingDAOImpl(InMemoryStorage storage) {
        this.storage = storage;
    }

    @Override
    public void createTraining(TrainingEntity trainingEntity) {
        storage.getTrainingStorage().put(trainingEntity.getTrainingName(), trainingEntity);
    }

    @Override
    public TrainingEntity getTraining(String trainingName) {
        return storage.getTrainingStorage().get(trainingName);
    }

    @Override
    public List<TrainingEntity> getAllTrainings() {
        return new ArrayList<>(storage.getTrainingStorage().values());
    }


}
