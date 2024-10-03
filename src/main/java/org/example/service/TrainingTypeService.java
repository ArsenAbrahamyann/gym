package org.example.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingTypeEntity;
import org.example.repository.TrainingTypeRepository;
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
        try {
            trainingTypeRepository.save(trainingType);
            log.info("Successfully saved training type: {}", trainingType);
        } catch (Exception e) {
            log.error("Failed to save training type: {}, error: {}", trainingType, e.getMessage());
        }
    }

    /**
     * Finds a {@link TrainingTypeEntity} by its ID.
     * <p>
     * This method is transactional and retrieves the training type from the database based on its unique ID.
     * </p>
     *
     * @param trainingTypeId the unique ID of the {@link TrainingTypeEntity}.
     * @return an {@link Optional} containing the found {@link TrainingTypeEntity}, or an empty {@link Optional} if not found.
     */
    @Transactional
    public TrainingTypeEntity findById(Long trainingTypeId) {
        try {
            Optional<TrainingTypeEntity> trainingType = trainingTypeRepository.findById(trainingTypeId);
            if (trainingType.isPresent()) {
                log.info("Found training type with ID {}: {}", trainingTypeId, trainingType.get());
                return trainingType.get();
            } else {
                log.warn("No training type found with ID {}", trainingTypeId);
            }
        } catch (Exception e) {
            log.error("Error while fetching training type with ID {}: {}", trainingTypeId, e.getMessage());
        }
        return null;
    }

    public List<TrainingTypeEntity> findAll() {

        try {
            List<TrainingTypeEntity> all = trainingTypeRepository.findAll();
            if (all == null) {
                log.info("TrainingType not found");
            }
            return all;
        }catch (Exception e) {
            log.error("Error while fetching training type.", e.getMessage());
            return Collections.emptyList();
        }
    }
}
