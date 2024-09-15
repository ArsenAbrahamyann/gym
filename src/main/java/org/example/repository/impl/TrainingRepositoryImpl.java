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

/**
 * Implementation of the {@link TrainingRepository} interface, providing
 * CRUD operations and custom query methods for {@link TrainingEntity} using Hibernate.
 */
@Repository
@RequiredArgsConstructor
public class TrainingRepositoryImpl implements TrainingRepository {

    private final SessionFactory sessionFactory;
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Finds a {@link TrainingEntity} by its training name.
     *
     * @param trainingName the name of the training to search for
     * @return an {@link Optional} containing the found {@link TrainingEntity}, or empty if not found
     */
    @Override
    public Optional<TrainingEntity> findByTrainingName(String trainingName) {
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("from TrainingEntity where trainingName = :trainingName", TrainingEntity.class)
                .setParameter("trainingName", trainingName)
                .uniqueResult());
    }

    /**
     * Saves a new {@link TrainingEntity} to the database.
     *
     * @param training the {@link TrainingEntity} to be saved
     */
    @Override
    public void save(TrainingEntity training) {
        sessionFactory.getCurrentSession()
                .persist(training);
    }

    /**
     * Finds a list of {@link TrainingEntity} for a given trainee based on criteria.
     *
     * @param traineeId the ID of the trainee
     * @param fromDate the starting date of the training period
     * @param toDate the ending date of the training period
     * @param trainerName the name of the trainer (optional)
     * @param trainingType the type of training (optional)
     * @return an {@link Optional} containing a list of {@link TrainingEntity}, or empty if no trainings match the criteria
     */
    @Override
    public Optional<List<TrainingEntity>> findTrainingsForTrainee(Long traineeId, Date fromDate,
                                                          Date toDate, String trainerName, String trainingType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrainingEntity> cq = cb.createQuery(TrainingEntity.class);
        Root<TrainingEntity> training = cq.from(TrainingEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (traineeId != null) {
            predicates.add(cb.equal(training.get("traineeId"), traineeId));
        }

        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(training.get("trainingDate"), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(training.get("trainingDate"), toDate));
        }

        if (trainerName != null && !trainerName.isEmpty()) {
            Join<TrainingEntity, TrainerEntity> trainer = training.join("trainer");
            predicates.add(cb.like(cb.lower(trainer.get("name")), "%" + trainerName.toLowerCase() + "%"));
        }

        if (trainingType != null && !trainingType.isEmpty()) {
            predicates.add(cb.equal(training.get("trainingType").get("trainingTypeName"), trainingType));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return Optional.ofNullable(entityManager.createQuery(cq).getResultList());
    }

    /**
     * Finds a list of {@link TrainingEntity} for a given trainer based on criteria.
     *
     * @param trainerId the ID of the trainer
     * @param fromDate the starting date of the training period
     * @param toDate the ending date of the training period
     * @param traineeName the name of the trainee (optional)
     * @return an {@link Optional} containing a list of {@link TrainingEntity}, or empty if no trainings match the criteria
     */
    @Override
    public Optional<List<TrainingEntity>> findTrainingsForTrainer(Long trainerId, Date fromDate, Date toDate,
                                                        String traineeName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrainingEntity> cq = cb.createQuery(TrainingEntity.class);
        Root<TrainingEntity> training = cq.from(TrainingEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (trainerId != null) {
            predicates.add(cb.equal(training.get("trainerId"), trainerId));
        }

        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(training.get("trainingDate"), fromDate));
        }

        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(training.get("trainingDate"), toDate));
        }

        if (traineeName != null && ! traineeName.isEmpty()) {
            Join<TrainingEntity, TraineeEntity> trainee = training.join("trainee");
            predicates.add(cb.like(cb.lower(trainee.get("name")), "%"
                    + traineeName.toLowerCase()
                    + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return Optional.ofNullable(entityManager.createQuery(cq).getResultList());
    }
}
