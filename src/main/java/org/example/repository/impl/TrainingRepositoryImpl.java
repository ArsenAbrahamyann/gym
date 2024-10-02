package org.example.repository.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.UserEntity;
import org.example.repository.TrainingRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * Implementation of the {@link TrainingRepository} interface, providing
 * CRUD operations and custom query methods for {@link TrainingEntity} using Hibernate.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
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
        log.debug("Finding TrainingEntity by training name: {}", trainingName);
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
        log.debug("Saving TrainingEntity with ID: {}", training.getId());
        sessionFactory.getCurrentSession()
                .persist(training);
        log.info("TrainingEntity with ID: {} saved successfully", training.getId());

    }

    /**
     * Finds a list of {@link TrainingEntity} for a given trainee based on criteria.
     *
     * @param traineeId    the ID of the trainee
     * @param fromDate     the starting date of the training period
     * @param toDate       the ending date of the training period
     * @param trainerName  the name of the trainer (optional)
     * @param trainingType the type of training (optional)
     * @return an {@link Optional} containing a list of {@link TrainingEntity}, or empty if no trainings match the criteria
     */
    @Override
    public List<TrainingEntity> findTrainingsForTrainee(Long traineeId, LocalDateTime fromDate,
                                                                  LocalDateTime toDate, String trainerName,
                                                                  String trainingType) {
        log.debug("Finding trainings for trainee ID: {} with criteria [fromDate: {},"
                        + " toDate: {}, trainerName: {}, trainingType: {}]",
                traineeId, fromDate, toDate, trainerName, trainingType);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrainingEntity> cq = cb.createQuery(TrainingEntity.class);
        Root<TrainingEntity> training = cq.from(TrainingEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (traineeId != null) {
            predicates.add(cb.equal(training.get("trainee").get("id"), traineeId));
        }

        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(training.get("trainingDate"), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(training.get("trainingDate"), toDate));
        }

        if (trainerName != null && ! trainerName.isEmpty()) {
            Join<TrainingEntity, TrainerEntity> trainerJoin = training.join("trainer");
        }

        if (trainingType != null && ! trainingType.isEmpty()) {
            predicates.add(cb.equal(training.get("trainingType").get("trainingTypeName"), trainingType));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        List<TrainingEntity> resultList = entityManager.createQuery(cq).getResultList();
        if (resultList.isEmpty()) {
            log.info("No trainings found for trainee ID: {}", traineeId);
        } else {
            log.info("Found {} trainings for trainee ID: {}", resultList.size(), traineeId);
        }
        return resultList;
    }

    /**
     * Finds a list of {@link TrainingEntity} for a given trainer based on criteria.
     *
     * @param trainerId   the ID of the trainer
     * @param fromDate    the starting date of the training period
     * @param toDate      the ending date of the training period
     * @param traineeName the name of the trainee (optional)
     * @return an {@link Optional} containing a list of {@link TrainingEntity}, or empty if no trainings match the criteria
     */
    @Override
    public List<TrainingEntity> findTrainingsForTrainer(Long trainerId, LocalDateTime fromDate,
                                                        LocalDateTime toDate,
                                                        String traineeName) {
        log.debug("Finding trainings for trainer ID: {} with criteria [fromDate: {}, toDate: {}, traineeName: {}]",
                trainerId, fromDate, toDate, traineeName);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrainingEntity> cq = cb.createQuery(TrainingEntity.class);
        Root<TrainingEntity> training = cq.from(TrainingEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (trainerId != null) {
            predicates.add(cb.equal(training.get("trainer").get("id"), trainerId));
        }

        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(training.get("trainingDate"), fromDate));
        }

        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(training.get("trainingDate"), toDate));
        }

        if (traineeName != null && ! traineeName.isEmpty()) {
            Join<TrainingEntity, TraineeEntity> trainee = training.join("trainee");
            Join<TraineeEntity, UserEntity> user = trainee.join("user");

            Predicate usernamePredicate = cb.like(cb.lower(user.get("username")), "%"
                    + traineeName.toLowerCase()
                    + "%");

            predicates.add(usernamePredicate);
        }

        cq.where(predicates.toArray(new Predicate[0]));

        List<TrainingEntity> resultList = entityManager.createQuery(cq).getResultList();
        if (resultList.isEmpty()) {
            log.info("No trainings found for trainer ID: {}", trainerId);
        } else {
            log.info("Found {} trainings for trainer ID: {}", resultList.size(), trainerId);
        }
        return resultList;
    }
}
