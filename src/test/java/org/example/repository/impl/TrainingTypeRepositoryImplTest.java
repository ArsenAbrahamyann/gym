package org.example.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.entity.TrainingTypeEntity;
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
public class TrainingTypeRepositoryImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<TrainingTypeEntity> query;

    @InjectMocks
    private TrainingTypeRepositoryImpl trainingTypeRepository;
    private TrainingTypeEntity trainingTypeEntity;

    /**
     * Set up the mock environment before each test.
     */
    @BeforeEach
    public void setUp() {
        // Initialize a sample TrainingTypeEntity
        trainingTypeEntity = new TrainingTypeEntity();
        trainingTypeEntity.setId(1L);
        trainingTypeEntity.setTrainingTypeName("Yoga");

        // Mock session factory to return a session when requested
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testSave() {
        // Arrange
        doNothing().when(session).persist(any(TrainingTypeEntity.class));

        // Act
        trainingTypeRepository.save(trainingTypeEntity);

        // Assert
        verify(sessionFactory, times(1)).getCurrentSession();
        verify(session, times(1)).persist(trainingTypeEntity);
    }

    @Test
    void testFindById_Found() {
        Long trainingTypeId = 1L;
        TrainingTypeEntity expectedEntity = new TrainingTypeEntity();
        expectedEntity.setId(trainingTypeId);

        // Mock the createQuery method to return the query mock
        when(session.createQuery("from TrainingTypeEntity where id = :trainingTypeId", TrainingTypeEntity.class))
                .thenReturn(query);
        // Mock the setParameter and uniqueResult methods
        when(query.setParameter("trainingTypeId", trainingTypeId)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(expectedEntity);

        // Call the method to test
        Optional<TrainingTypeEntity> result = trainingTypeRepository.findById(trainingTypeId);

        // Verify the results
        assertTrue(result.isPresent());
        assertEquals(trainingTypeId, result.get().getId());
    }

    @Test
    void testFindById_NotFound() {
        Long trainingTypeId = 1L;

        // Mock the createQuery method to return the query mock
        when(session.createQuery("from TrainingTypeEntity where id = :trainingTypeId", TrainingTypeEntity.class))
                .thenReturn(query);
        // Mock the setParameter and uniqueResult methods
        when(query.setParameter("trainingTypeId", trainingTypeId)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        // Call the method to test
        Optional<TrainingTypeEntity> result = trainingTypeRepository.findById(trainingTypeId);

        // Verify the results
        assertFalse(result.isPresent());
    }
}
