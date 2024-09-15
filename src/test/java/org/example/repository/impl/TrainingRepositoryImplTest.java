package org.example.repository.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.example.entity.TrainingEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TraineeEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingRepositoryImplTest {
    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<TrainingEntity> criteriaQuery;

    @Mock
    private Root<TrainingEntity> root;
    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Join<TrainingEntity, TrainerEntity> trainerJoin;

    @Mock
    private Join<TrainingEntity, TraineeEntity> traineeJoin;

    @InjectMocks
    private TrainingRepositoryImpl trainingRepository;

//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    public void testFindByTrainingName() {
        // Arrange
        String trainingName = "Yoga";
        TrainingEntity trainingEntity = new TrainingEntity();
        when(entityManager.createQuery("from TrainingEntity where trainingName = :trainingName", TrainingEntity.class))
                .thenReturn(mock(javax.persistence.TypedQuery.class));
        when(entityManager.createQuery("from TrainingEntity where trainingName = :trainingName", TrainingEntity.class)
                .setParameter("trainingName", trainingName))
                .thenReturn(mock(javax.persistence.TypedQuery.class));
        when(entityManager.createQuery("from TrainingEntity where trainingName = :trainingName", TrainingEntity.class)
                .setParameter("trainingName", trainingName)
                .getSingleResult())
                .thenReturn(trainingEntity);

        // Act
        Optional<TrainingEntity> result = trainingRepository.findByTrainingName(trainingName);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(trainingEntity, result.get());
    }

    @Test
    public void testSave() {
        // Arrange
        TrainingEntity trainingEntity = new TrainingEntity();
        doNothing().when(entityManager).persist(trainingEntity);

        // Act
        trainingRepository.save(trainingEntity);


        // Assert
        verify(entityManager, times(1)).persist(trainingEntity);
    }

    @Test
    public void testFindTrainingsForTrainee() {
        // Arrange
        Long traineeId = 1L;
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "John";
        String trainingType = "Strength";
        List<TrainingEntity> expectedTrainings = new ArrayList<>();
        CriteriaQuery<TrainingEntity> cq = mock(CriteriaQuery.class);
        Root<TrainingEntity> training = mock(Root.class);
        javax.persistence.TypedQuery<TrainingEntity> query = mock(javax.persistence.TypedQuery.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(TrainingEntity.class)).thenReturn(cq);
        when(cq.from(TrainingEntity.class)).thenReturn(training);
        when(entityManager.createQuery(cq)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedTrainings);

        // Mock CriteriaBuilder behaviors
        when(criteriaBuilder.equal(training.get("traineeId"), traineeId)).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.greaterThanOrEqualTo(training.get("trainingDate"), fromDate)).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.lessThanOrEqualTo(training.get("trainingDate"), toDate)).thenReturn(mock(Predicate.class));
        when(training.join("trainer")).thenReturn(any());
        when(trainerJoin.get("name")).thenReturn(mock(javax.persistence.criteria.Path.class));
        when(criteriaBuilder.like(criteriaBuilder.lower(trainerJoin.get("name")), "%" + trainerName.toLowerCase() + "%")).thenReturn(mock(Predicate.class));
        when(training.get("trainingType").get("trainingTypeName")).thenReturn(mock(javax.persistence.criteria.Path.class));
        when(criteriaBuilder.equal(training.get("trainingType").get("trainingTypeName"), trainingType)).thenReturn(mock(Predicate.class));

        // Act
        Optional<List<TrainingEntity>> result = trainingRepository.findTrainingsForTrainee(traineeId, fromDate, toDate, trainerName, trainingType);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedTrainings, result.get());
    }

    @Test
    public void testFindTrainingsForTrainer() {
        // Arrange
        Long trainerId = 1L;
        Date fromDate = new Date();
        Date toDate = new Date();
        String traineeName = "Alice";
        List<TrainingEntity> expectedTrainings = new ArrayList<>();
        CriteriaQuery<TrainingEntity> cq = mock(CriteriaQuery.class);
        Root<TrainingEntity> training = mock(Root.class);
        javax.persistence.TypedQuery<TrainingEntity> query = mock(javax.persistence.TypedQuery.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(TrainingEntity.class)).thenReturn(cq);
        when(cq.from(TrainingEntity.class)).thenReturn(training);
        when(entityManager.createQuery(cq)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedTrainings);

        // Mock CriteriaBuilder behaviors
        when(criteriaBuilder.equal(training.get("trainerId"), trainerId)).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.greaterThanOrEqualTo(training.get("trainingDate"), fromDate)).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.lessThanOrEqualTo(training.get("trainingDate"), toDate)).thenReturn(mock(Predicate.class));
        when(training.join("trainee")).thenReturn(any());
        when(traineeJoin.get("name")).thenReturn(mock(javax.persistence.criteria.Path.class));
        when(criteriaBuilder.like(criteriaBuilder.lower(traineeJoin.get("name")), "%" + traineeName.toLowerCase() + "%")).thenReturn(mock(Predicate.class));

        // Act
        Optional<List<TrainingEntity>> result = trainingRepository.findTrainingsForTrainer(trainerId, fromDate, toDate, traineeName);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedTrainings, result.get());
    }
}
