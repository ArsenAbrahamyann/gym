package org.example.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.repository.UserDAO;
import org.example.storage.InMemoryStorage;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDAOImpl implements UserDAO {
    private final InMemoryStorage inMemoryStorage;

    @Override
    public List<String> findAllUsernames() {
        Collection<TraineeEntity> values = inMemoryStorage.getTraineeStorage().values();
        Collection<TrainerEntity> values1 = inMemoryStorage.getTrainerStorage().values();
        List<String> getAllUsernames = new ArrayList<>();
        for (TraineeEntity traineeEntity : values) {
            getAllUsernames.add(traineeEntity.getUserId());
        }
        for (TrainerEntity trainerEntity : values1) {
            getAllUsernames.add(trainerEntity.getUserId());
        }

        return getAllUsernames;
    }
}
