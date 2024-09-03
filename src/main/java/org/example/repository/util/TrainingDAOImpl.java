package org.example.repository.util;

import org.example.entity.TrainingEntity;
import org.example.repository.TrainingDAO;
import org.example.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
    public void updateTraining(String trainingName, TrainingEntity trainingEntity) {
        if (storage.getTrainingStorage().containsKey(trainingName)) {
            storage.getTrainingStorage().put(trainingName, trainingEntity);
        } else {
            throw new NoSuchElementException("TrainingEntity with name " + trainingName + " not found.");
        }
    }

    @Override
    public void deleteTraining(String trainingName) {
        storage.getTrainingStorage().remove(trainingName);
    }

    @Override
    public TrainingEntity getTraining(String trainingName) {
        return  storage.getTrainingStorage().get(trainingName);
    }

    @Override
    public List<TrainingEntity> getAllTrainings() {
        return new ArrayList<>(storage.getTrainingStorage().values());
    }


}
