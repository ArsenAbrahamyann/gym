package org.example.repository.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.entity.TrainerEntity;
import org.example.repository.TrainerRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TrainerRepositoryImpl implements TrainerRepository {
    private final SessionFactory sessionFactory;

    @Override
    public void save(TrainerEntity trainer) {
        sessionFactory.getCurrentSession().persist(trainer);
    }

    @Override
    public Optional<TrainerEntity> findById(Long trainerId) {
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("from TrainerEntity where id = :trainerId", TrainerEntity.class)
                .setParameter("trainerId", trainerId)
                .uniqueResult());
    }

    @Override
    public Optional<TrainerEntity> findByTrainerFromUsername(String username) {
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("from UserEntity where username = :username", TrainerEntity.class)
                .setParameter("username", username)
                .uniqueResult());
    }

    @Override
    public Optional<List<TrainerEntity>> findAll() {
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("from TrainerEntity", TrainerEntity.class)
                .getResultList());
    }

    @Override
    public Optional<List<TrainerEntity>> findAssignedTrainers(Long traineeId) {
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("SELECT t FROM TrainerEntity t JOIN t.trainees tr WHERE tr.id = :traineeId",
                        TrainerEntity.class)
                .setParameter("traineeId", traineeId)
                .list());
    }

    @Override
    public Optional<List<TrainerEntity>> findAllById(List<Long> trainerIds) {
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("FROM TrainerEntity t WHERE t.id IN :ids", TrainerEntity.class)
                .getResultList());
    }

    @Override
    public void update(TrainerEntity trainer) {
        sessionFactory.getCurrentSession()
                .detach(trainer);
    }

}
