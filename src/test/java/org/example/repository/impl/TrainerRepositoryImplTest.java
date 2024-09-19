package org.example.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
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
public class TrainerRepositoryImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @InjectMocks
    private TrainerRepositoryImpl trainerRepository;

    private TrainerEntity trainerEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        // Setup UserEntity
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Jane");
        userEntity.setLastName("Doe");
        userEntity.setUsername("janedoe");
        userEntity.setPassword("password123");
        userEntity.setIsActive(true);

        // Setup TrainerEntity
        trainerEntity = new TrainerEntity();
        trainerEntity.setId(1L);
        trainerEntity.setUser(userEntity);

        // Mock the session factory to return a session
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void save_ShouldPersistTrainerEntity() {
        // Act
        trainerRepository.save(trainerEntity);

        // Assert that the session's saveOrUpdate method was called
        verify(session).saveOrUpdate(trainerEntity);
    }

    @Test
    void findById_ShouldReturnTrainerEntity_WhenIdExists() {
        // Arrange
        Long trainerId = 1L;
        Query<TrainerEntity> query = mock(Query.class);
        when(session.createQuery("from TrainerEntity where id = :trainerId", TrainerEntity.class)).thenReturn(query);
        when(query.setParameter("trainerId", trainerId)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainerEntity);

        // Act
        Optional<TrainerEntity> result = trainerRepository.findById(trainerId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(trainerId);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenIdDoesNotExist() {
        // Arrange
        Long trainerId = 2L;
        Query<TrainerEntity> query = mock(Query.class);
        when(session.createQuery("from TrainerEntity where id = :trainerId", TrainerEntity.class)).thenReturn(query);
        when(query.setParameter("trainerId", trainerId)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        // Act
        Optional<TrainerEntity> result = trainerRepository.findById(trainerId);

        // Assert
        assertThat(result).isNotPresent();
    }

    @Test
    void findByTrainerFromUsername_ShouldReturnTrainerEntity_WhenUsernameExists() {
        // Arrange
        String username = "janedoe";
        Query<TrainerEntity> query = mock(Query.class);
        when(session.createQuery("from TrainerEntity te where te.user.username = :username", TrainerEntity.class))
                .thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainerEntity);

        // Act
        Optional<TrainerEntity> result = trainerRepository.findByTrainerFromUsername(username);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getUser().getUsername()).isEqualTo(username);
    }

    @Test
    void findByTrainerFromUsername_ShouldReturnEmpty_WhenUsernameDoesNotExist() {
        // Arrange
        String username = "non_existing_user";
        Query<TrainerEntity> query = mock(Query.class);
        when(session.createQuery("from TrainerEntity te where te.user.username = :username", TrainerEntity.class))
                .thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        // Act
        Optional<TrainerEntity> result = trainerRepository.findByTrainerFromUsername(username);

        // Assert
        assertThat(result).isNotPresent();
    }

    @Test
    void findAll_ShouldReturnListOfTrainers() {
        // Arrange
        Query<TrainerEntity> query = mock(Query.class);
        when(session.createQuery("from TrainerEntity", TrainerEntity.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(trainerEntity));

        // Act
        Optional<List<TrainerEntity>> result = trainerRepository.findAll();

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).contains(trainerEntity);
    }

    @Test
    void findAssignedTrainers_ShouldReturnListOfAssignedTrainers() {
        // Arrange
        Long traineeId = 1L;
        Query<TrainerEntity> query = mock(Query.class);
        when(session.createQuery("SELECT t FROM TrainerEntity t JOIN t.trainees tr WHERE tr.id = :traineeId",
                TrainerEntity.class))
                .thenReturn(query);
        when(query.setParameter("traineeId", traineeId)).thenReturn(query);

        // Act
        Optional<List<TrainerEntity>> result = trainerRepository.findAssignedTrainers(traineeId);

        // Assert
        assertThat(result).isPresent();
    }

    @Test
    void findAllById_ShouldReturnListOfTrainers_WhenIdsAreGiven() {
        // Arrange
        List<Long> trainerIds = Arrays.asList(1L, 2L);
        Query<TrainerEntity> query = mock(Query.class);
        when(session.createQuery("FROM TrainerEntity t WHERE t.id IN :ids", TrainerEntity.class)).thenReturn(query);
        when(query.setParameter("ids", trainerIds)).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(trainerEntity));

        // Act
        Optional<List<TrainerEntity>> result = trainerRepository.findAllById(trainerIds);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).contains(trainerEntity);
    }

    @Test
    void update_ShouldSaveOrUpdateTrainerEntity() {
        // Act
        trainerRepository.update(trainerEntity);

        // Assert
        verify(session).saveOrUpdate(trainerEntity);
    }
}
