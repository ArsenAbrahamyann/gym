package org.example.repository.util;

import org.example.entity.TraineeEntity;
import org.example.repository.TraineeDAO;
import org.example.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TraineeDAOImpl implements TraineeDAO {
    private final InMemoryStorage storage;

    @Autowired
    public TraineeDAOImpl(InMemoryStorage storage) {
        this.storage = storage;
    }

    @Override
    public void createTrainee(TraineeEntity traineeEntity) {
        storage.getTraineeStorage().put(traineeEntity.getUserId(), traineeEntity);
    }

    @Override
    public void updateTrainee(String userId, TraineeEntity traineeEntity) {
        storage.getTraineeStorage().put(userId, traineeEntity);
    }

    @Override
    public void deleteTrainee(String userId) {
        storage.getTraineeStorage().remove(userId);
    }

    @Override
    public TraineeEntity getTrainee(String userId) {
        return storage.getTraineeStorage().get(userId);
    }

    @Override
    public List<TraineeEntity> getAllTrainees() {
        return storage.getTraineeStorage().values().stream()
                .filter(TraineeEntity.class::isInstance)
                .map(TraineeEntity.class::cast)
                .collect(Collectors.toList());
    }
}
