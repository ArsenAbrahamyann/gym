package org.example.repository.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.repository.TrainingTypeRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the {@link org.example.entity.TrainingTypeEntity} interface, providing
 * CRUD operations and custom query methods for {@link org.example.entity.TrainingTypeEntity} using Hibernate.
 */
@Repository
@RequiredArgsConstructor
public class TrainingTypeRepositoryImpl implements TrainingTypeRepository {

    private final SessionFactory sessionFactory;

    @Override
    public void save(TrainingTypeEntity trainingTypeEntity) {
       sessionFactory.getCurrentSession().persist(trainingTypeEntity);
    }

    @Override
    public Optional<TrainingTypeEntity> findById(Long trainingTypeId) {
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("from TrainingTypeEntity where id = :trainingTypeId", TrainingTypeEntity.class)
                .setParameter("trainingTypeId", trainingTypeId)
                .uniqueResult());
    }
}
