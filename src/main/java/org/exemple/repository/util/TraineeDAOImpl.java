package org.exemple.repository.util;

import org.exemple.entity.Trainee;
import org.exemple.repository.TraineeDAO;
import org.exemple.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository

public class TraineeDAOImpl implements TraineeDAO {
    private final InMemoryStorage storage;

    @Autowired
    public TraineeDAOImpl(InMemoryStorage storage) {
        this.storage = storage;
    }


    @Override
    public void createTrainee(Trainee trainee) {
        storage.getTraineeStorage().put(trainee.getUserId(), trainee);
    }

    @Override
    public void updateTrainee(String userId, Trainee trainee) {
        storage.getTraineeStorage().put(userId, trainee);
    }

    @Override
    public void deleteTrainee(String userId) {
        storage.getTraineeStorage().remove(userId);
    }

    @Override
    public Trainee getTrainee(String userId) {
        return  storage.getTraineeStorage().get(userId);
    }

    @Override
    public List<Trainee> getAllTrainees() {
        return storage.getTraineeStorage().values().stream()
                .filter(Trainee.class::isInstance)
                .map(Trainee.class::cast)
                .collect(Collectors.toList());
    }
}
