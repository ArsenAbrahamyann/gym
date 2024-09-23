package org.example.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingTypeEntity;
import org.example.repository.TrainingTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    @Transactional
    public void save(TrainingTypeEntity trainingType) {
        trainingTypeRepository.save(trainingType);
    }

    @Transactional
    public Optional<TrainingTypeEntity> findById(Long trainingTypeId) {
        return trainingTypeRepository.findById(trainingTypeId);
    }
}
