package org.example.gym.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.repository.TrainingTypeRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for handling operations related to {@link TrainingTypeEntity}.
 * <p>
 * This service provides methods to save and retrieve training types. It uses the {@link TrainingTypeRepository}
 * for interacting with the database and performs operations within transactions.
 * </p>
 * <p>
 * The {@code @Service} annotation marks this class as a Spring service, and {@code @Slf4j} enables logging.
 * The {@code @RequiredArgsConstructor} annotation generates a constructor for the final fields, injecting the required dependencies.
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    /**
     * Saves a {@link TrainingTypeEntity} to the database.
     * <p>
     * This method is transactional, ensuring that the save operation is executed within a single transaction.
     * </p>
     *
     * @param trainingType the {@link TrainingTypeEntity} to be saved in the database.
     */
    @Transactional
    public void save(TrainingTypeEntity trainingType) {
        trainingTypeRepository.save(trainingType);
        log.info("Successfully saved training type: {}", trainingType);
    }

    /**
     * Finds a {@link TrainingTypeEntity} by its ID.
     * <p>
     * This method is transactional and retrieves the training type from the database based on its unique ID.
     * </p>
     *
     * @param trainingTypeId the unique ID of the {@link TrainingTypeEntity}.
     * @return the found {@link TrainingTypeEntity}.
     * @throws ResourceNotFoundException if the training type is not found.
     */
    @Transactional
    public TrainingTypeEntity findById(Long trainingTypeId) {
        return trainingTypeRepository.findById(trainingTypeId)
                .orElse(null);
//                .orElseThrow(() -> {
//                    log.warn("No training type found with ID {}", trainingTypeId);
////                    return new ResourceNotFoundException("TrainingType not found for ID: " + trainingTypeId);
//                });
    }

    /**
     * Retrieves all {@link TrainingTypeEntity} from the database.
     * <p>
     * This method is transactional and retrieves all training types from the database.
     * </p>
     *
     * @return a list of all {@link TrainingTypeEntity}.
     */
    @Transactional(readOnly = true)
    public List<TrainingTypeEntity> findAll() {
        List<TrainingTypeEntity> all = trainingTypeRepository.findAll();
        if (all.isEmpty()) {
            log.info("No TrainingType found.");
        } else {
            log.info("Found {} training types.", all.size());
        }
        return all;
    }
}
