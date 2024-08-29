package org.exemple.repository.util;

import org.exemple.entity.Training;
import org.exemple.repository.TrainingDAO;
import org.exemple.storage.InMemoryStorage;
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
    public void createTraining(Training training) {
        storage.getTrainingStorage().put(training.getTrainingName(), training);
    }

    @Override
    public void updateTraining(String trainingName, Training training) {
        if (storage.getTrainingStorage().containsKey(trainingName)) {
            storage.getTrainingStorage().put(trainingName, training);
        } else {
            throw new NoSuchElementException("Training with name " + trainingName + " not found.");
        }
    }

    @Override
    public void deleteTraining(String trainingName) {
        storage.getTrainingStorage().remove(trainingName);
    }

    @Override
    public Training getTraining(String trainingName) {
        return  storage.getTrainingStorage().get(trainingName);
    }

    @Override
    public List<Training> getAllTrainings() {
        return new ArrayList<>(storage.getTrainingStorage().values());
    }


}
