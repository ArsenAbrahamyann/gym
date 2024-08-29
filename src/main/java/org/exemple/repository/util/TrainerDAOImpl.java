package org.exemple.repository.util;

import org.exemple.entity.Trainer;
import org.exemple.repository.TrainerDAO;
import org.exemple.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TrainerDAOImpl implements TrainerDAO {
    private final InMemoryStorage storage;

    @Autowired
    public TrainerDAOImpl(InMemoryStorage storage) {
        this.storage = storage;
    }

    @Override
    public void createTrainer(Trainer trainer) {
        storage.getTrainerStorage().put(trainer.getUserId(), trainer);
    }

    @Override
    public void updateTrainer(String userId, Trainer trainer) {
        storage.getTrainerStorage().put(userId, trainer);
    }

    @Override
    public Trainer getTrainer(String userId) {
        return storage.getTrainerStorage().get(userId);
    }

    @Override
    public void deleteTrainer(String userId) {
        storage.getTrainerStorage().remove(userId);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        return storage.getTrainerStorage().values().stream()
                .filter(Trainer.class::isInstance)
                .map(Trainer.class::cast)
                .collect(Collectors.toList());
    }
}
