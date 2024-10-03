package org.example.gym.repository.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.repository.TrainingTypeRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the {@link org.example.entity.TrainingTypeEntity} interface, providing
 * CRUD operations and custom query methods for {@link org.example.entity.TrainingTypeEntity} using Hibernate.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class TrainingTypeRepositoryImpl implements TrainingTypeRepository {

    private final SessionFactory sessionFactory;

    /**
     * Saves a new {@link TrainingTypeEntity} to the database.
     *
     * @param trainingTypeEntity the {@link TrainingTypeEntity} to be saved
     */
    @Override
    public void save(TrainingTypeEntity trainingTypeEntity) {
        log.debug("Saving TrainingTypeEntity with ID: {}", trainingTypeEntity.getId());
        sessionFactory.getCurrentSession().persist(trainingTypeEntity);
        log.info("TrainingTypeEntity with ID: {} saved successfully", trainingTypeEntity.getId());
    }

    /**
     * Finds a {@link TrainingTypeEntity} by its ID.
     *
     * @param trainingTypeId the ID of the training type to search for
     * @return an {@link Optional} containing the found {@link TrainingTypeEntity}, or empty if not found
     */
    @Override
    public Optional<TrainingTypeEntity> findById(Long trainingTypeId) {
        log.debug("Finding TrainingTypeEntity by ID: {}", trainingTypeId);
        TrainingTypeEntity result = sessionFactory.getCurrentSession()
                .createQuery("from TrainingTypeEntity where id = :trainingTypeId", TrainingTypeEntity.class)
                .setParameter("trainingTypeId", trainingTypeId)
                .uniqueResult();

        if (result == null) {
            log.info("TrainingTypeEntity with ID: {} not found", trainingTypeId);
        } else {
            log.info("TrainingTypeEntity with ID: {} found", trainingTypeId);
        }

        return Optional.ofNullable(result);
    }

    @Override
    public List<TrainingTypeEntity> findAll() {
        log.debug("finding All TrainingType.");
        List<TrainingTypeEntity> resultList = sessionFactory.getCurrentSession()
                .createQuery("SELECT * from TrainingTypeEntity", TrainingTypeEntity.class)
                .getResultList();
        return resultList;
    }
}
