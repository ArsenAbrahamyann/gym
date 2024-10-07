package org.example.repository.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.example.entity.TraineeEntity;
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
public class TraineeRepositoryImplTest {
    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @InjectMocks
    private TraineeRepositoryImpl traineeRepository;

    private TraineeEntity traineeEntity;
    private TrainerEntity trainerEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        // Setup UserEntity
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setUsername("johndoe");
        userEntity.setPassword("password123");
        userEntity.setIsActive(true);

        // Setup TrainerEntity
        trainerEntity = new TrainerEntity();
        trainerEntity.setId(1L);
        trainerEntity.setUser(userEntity);

        // Setup TraineeEntity
        traineeEntity = new TraineeEntity();
        traineeEntity.setId(1L);
        traineeEntity.setAddress("123 Main St");
        traineeEntity.setUser(userEntity);

        Set<TrainerEntity> trainers = new HashSet<>();
        trainers.add(trainerEntity);
        traineeEntity.setTrainers(trainers);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void findByTraineeFromUsername_ShouldReturnTraineeEntity_WhenUsernameExists() {
        // Arrange
        String username = "johndoe";
        Query<TraineeEntity> query = mock(Query.class);
        when(session.createQuery("from TraineeEntity te where te.user.username = :username", TraineeEntity.class))
                .thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(traineeEntity);

        // Act
        Optional<TraineeEntity> result = traineeRepository.findByTraineeFromUsername(username);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getUser().getUsername()).isEqualTo(username);
    }

    @Test
    void findByTraineeFromUsername_ShouldReturnEmpty_WhenUsernameDoesNotExist() {
        // Arrange
        String username = "non_existing_user";
        Query<TraineeEntity> query = mock(Query.class);
        when(session.createQuery("from TraineeEntity te where te.user.username = :username", TraineeEntity.class))
                .thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        // Act
        Optional<TraineeEntity> result = traineeRepository.findByTraineeFromUsername(username);

        // Assert
        assertThat(result).isNotPresent();
    }

    @Test
    void findById_ShouldReturnTraineeEntity_WhenIdExists() {
        // Arrange
        Long traineeId = 1L;
        Query<TraineeEntity> query = mock(Query.class);
        when(session.createQuery("from TraineeEntity where id = :traineeId", TraineeEntity.class))
                .thenReturn(query);
        when(query.setParameter("traineeId", traineeId)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(traineeEntity);

        // Act
        Optional<TraineeEntity> result = traineeRepository.findById(traineeId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(traineeId);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenIdDoesNotExist() {
        // Arrange
        Long traineeId = 2L;
        Query<TraineeEntity> query = mock(Query.class);
        when(session.createQuery("from TraineeEntity where id = :traineeId", TraineeEntity.class))
                .thenReturn(query);
        when(query.setParameter("traineeId", traineeId)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        // Act
        Optional<TraineeEntity> result = traineeRepository.findById(traineeId);

        // Assert
        assertThat(result).isNotPresent();
    }

    @Test
    void save_ShouldMergeTraineeEntity() {
        // Act
        traineeRepository.save(traineeEntity);

        // Assert
        verify(session).merge(traineeEntity); // Verify merge is used instead of persist
    }

    @Test
    void update_ShouldSaveOrUpdateTraineeEntity() {
        // Act
        traineeRepository.update(traineeEntity);

        // Assert
        verify(session).saveOrUpdate(traineeEntity); // Verify saveOrUpdate is called
    }

    @Test
    void delete_ShouldRemoveTraineeEntity() {
        // Act
        traineeRepository.delete(traineeEntity);

        // Assert
        verify(session).delete(traineeEntity); // Verify delete is called
    }
}
