package org.example.repository;

import java.util.Optional;
import org.example.entity.TraineeEntity;

public interface TraineeRepository {
    Optional<TraineeEntity> findByTraineeFromUsername(String username);

    Optional<TraineeEntity> findById(Long traineeId);

    void save(TraineeEntity trainee);

    void update(TraineeEntity trainee);
}
