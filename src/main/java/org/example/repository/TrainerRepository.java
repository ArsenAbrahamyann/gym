package org.example.repository;

import java.util.List;
import java.util.Optional;
import org.example.entity.TrainerEntity;

public interface TrainerRepository {
    void save(TrainerEntity trainer);

    Optional<TrainerEntity> findById(Long trainerId);

    Optional<TrainerEntity> findByTrainerFromUsername(String username);

    Optional<List<TrainerEntity>> findAll();

    Optional<List<TrainerEntity>> findAssignedTrainers(Long id);

    Optional<List<TrainerEntity>> findAllById(List<Long> trainerIds);
}
