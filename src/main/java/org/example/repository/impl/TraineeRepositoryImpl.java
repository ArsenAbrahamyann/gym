package org.example.repository.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.entity.TraineeEntity;
import org.example.repository.TraineeRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TraineeRepositoryImpl implements TraineeRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Optional<TraineeEntity> findByTraineeFromUsername(String username) {
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("from UserEntity where username = :username", TraineeEntity.class)
                .setParameter("username", username)
                .uniqueResult());
    }

    @Override
    public Optional<TraineeEntity> findById(Long traineeId) {
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("from TraineeEntity where id = :traineeId", TraineeEntity.class)
                .setParameter("traineeId", traineeId)
                .uniqueResult());
    }

    @Override
    public void save(TraineeEntity trainee) {
        sessionFactory.getCurrentSession().persist(trainee);
    }

    @Override
    public void update(TraineeEntity trainee) {
        sessionFactory.getCurrentSession()
                .detach(trainee);
    }
}
