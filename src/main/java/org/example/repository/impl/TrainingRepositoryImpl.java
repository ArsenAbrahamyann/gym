package org.example.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.repository.TrainingRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TrainingRepositoryImpl implements TrainingRepository {

    private final SessionFactory sessionFactory;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TrainingEntity> findByTrainingName(String trainingName) {
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("from TrainingEntity where trainingName = :trainingName", TrainingEntity.class)
                .setParameter("trainingName", trainingName)
                .uniqueResult());
    }

    @Override
    public void save(TrainingEntity training) {
        sessionFactory.getCurrentSession()
                .persist(training);
    }

    @Override
    public Optional<List<TrainingEntity>> findTrainingsForTrainee(Long traineeId, Date fromDate, Date toDate, String trainerName,
                                                        String trainingType) {
        // Get the CriteriaBuilder
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrainingEntity> cq = cb.createQuery(TrainingEntity.class);
        Root<TrainingEntity> training = cq.from(TrainingEntity.class);

        // Initialize predicates (filters)
        List<Predicate> predicates = new ArrayList<>();

        // Filter by traineeId
        if (traineeId
                != null) {
            predicates.add(cb.equal(training.get("traineeId"), traineeId));
        }

        // Filter by date range (fromDate and toDate)
        if (fromDate
                != null) {
            predicates.add(cb.greaterThanOrEqualTo(training.get("trainingDate"), fromDate));
        }
        if (toDate
                != null) {
            predicates.add(cb.lessThanOrEqualTo(training.get("trainingDate"), toDate));
        }

        // Filter by trainerName
        if (trainerName
                != null
                && ! trainerName.isEmpty()) {
            Join<TrainingEntity, TrainerEntity> trainer = training.join("trainer");
            predicates.add(cb.like(cb.lower(trainer.get("name")), "%"
                    + trainerName.toLowerCase()
                    + "%"));
        }

        // Filter by trainingType
        if (trainingType
                != null
                && ! trainingType.isEmpty()) {
            predicates.add(cb.equal(training.get("trainingType").get("trainingTypeName"), trainingType));
        }

        // Add all the predicates to the CriteriaQuery
        cq.where(predicates.toArray(new Predicate[0]));

        // Execute the query
        return Optional.ofNullable(entityManager.createQuery(cq).getResultList());
    }

    @Override
    public Optional<List<TrainingEntity>> findTrainingsForTrainer(Long trainerId, Date fromDate, Date toDate,
                                                        String traineeName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrainingEntity> cq = cb.createQuery(TrainingEntity.class);
        Root<TrainingEntity> training = cq.from(TrainingEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (trainerId
                != null) {
            predicates.add(cb.equal(training.get("trainerId"), trainerId));
        }

        if (fromDate
                != null) {
            predicates.add(cb.greaterThanOrEqualTo(training.get("trainingDate"), fromDate));
        }

        if (toDate
                != null) {
            predicates.add(cb.lessThanOrEqualTo(training.get("trainingDate"), toDate));
        }

        if (traineeName
                != null
                && ! traineeName.isEmpty()) {
            Join<TrainingEntity, TraineeEntity> trainee = training.join("trainee");
            predicates.add(cb.like(cb.lower(trainee.get("name")), "%"
                    + traineeName.toLowerCase()
                    + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return Optional.ofNullable(entityManager.createQuery(cq).getResultList());

    }
}
