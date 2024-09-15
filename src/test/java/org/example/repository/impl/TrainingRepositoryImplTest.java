package org.example.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.entity.TrainingEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingRepositoryImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<TrainingEntity> query;

    @InjectMocks
    private TrainingRepositoryImpl trainingDao;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void testSave() {
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTrainingName("Yoga");

        trainingDao.save(trainingEntity);

        verify(session).persist(trainingEntity);
    }

    @Test
    void testFindByTrainingNameWhenTrainingExists() {
        String trainingName = "Yoga";
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTrainingName(trainingName);

        when(session.createQuery("from TrainingEntity where trainingName = :trainingName", TrainingEntity.class))
                .thenReturn(query);
        when(query.setParameter("trainingName", trainingName)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainingEntity);

        Optional<TrainingEntity> result = trainingDao.findByTrainingName(trainingName);

        assertEquals(Optional.of(trainingEntity), result);
    }

    @Test
    void testFindByTrainingNameWhenTrainingDoesNotExist() {
        String trainingName = "NonExistentTraining";

        when(session.createQuery("from TrainingEntity where trainingName = :trainingName", TrainingEntity.class))
                .thenReturn(query);
        when(query.setParameter("trainingName", trainingName)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        Optional<TrainingEntity> result = trainingDao.findByTrainingName(trainingName);

        assertEquals(Optional.empty(), result);
    }
}
