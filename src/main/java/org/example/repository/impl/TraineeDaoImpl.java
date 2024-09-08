package org.example.repository.impl;

import java.util.ArrayList;
import java.util.List;
import org.example.entity.TraineeEntity;
import org.example.repository.TraineeDao;
import org.example.storage.InMemoryStorage;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeDaoImpl implements TraineeDao {
    private final InMemoryStorage storage;

    public TraineeDaoImpl(InMemoryStorage storage) {
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
        return new ArrayList<>(storage.getTraineeStorage().values());
    }


}
