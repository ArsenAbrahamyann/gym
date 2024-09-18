package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.example.entity.UserEntity;
import org.example.repository.impl.UserRepositoryImpl;
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
public class UserServiceTest {
    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @InjectMocks
    private UserRepositoryImpl userRepositoryImpl;

    private UserEntity userEntity;
    private List<String> usernames;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        usernames = Arrays.asList("user1", "user2");
    }

    @Test
    void testFindByUsernameSuccess() {
        // Given
        Query<UserEntity> query = mock(Query.class);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createQuery("from UserEntity where username = :username", UserEntity.class)).thenReturn(query);
        when(query.setParameter("username", "testUser")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(userEntity);

        // When
        Optional<UserEntity> result = userRepositoryImpl.findByUsername("testUser");

        // Then
        verify(session).createQuery("from UserEntity where username = :username", UserEntity.class);
        verify(query).setParameter("username", "testUser");
        verify(query).uniqueResult();
        assertEquals(userEntity, result.orElse(null));
    }

    @Test
    void testFindByUsernameNotFound() {
        // Given
        Query<UserEntity> query = mock(Query.class);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createQuery("from UserEntity where username = :username", UserEntity.class)).thenReturn(query);
        when(query.setParameter("username", "nonExistingUser")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        // When
        Optional<UserEntity> result = userRepositoryImpl.findByUsername("nonExistingUser");

        // Then
        verify(session).createQuery("from UserEntity where username = :username", UserEntity.class);
        verify(query).setParameter("username", "nonExistingUser");
        verify(query).uniqueResult();
        assertEquals(Optional.empty(), result);
    }

    @Test
    void testFindAllUsernameSuccess() {
        // Given
        Query<String> query = mock(Query.class);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createQuery("select u.username from UserEntity u", String.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(usernames);

        // When
        Optional<List<String>> result = userRepositoryImpl.findAllUsername();

        // Then
        verify(session).createQuery("select u.username from UserEntity u", String.class);
        verify(query).getResultList();
        assertEquals(usernames, result.orElse(null));
    }



    @Test
    void testDeleteByUsernameSuccess() {
        // Given
        Query<UserEntity> query = mock(Query.class);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createQuery("from UserEntity where username = :username", UserEntity.class)).thenReturn(query);
        when(query.setParameter("username", "testUser")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(userEntity);

        // When
        userRepositoryImpl.deleteByUsername("testUser");

        // Then
        verify(session).createQuery("from UserEntity where username = :username", UserEntity.class);
        verify(query).setParameter("username", "testUser");
        verify(query).uniqueResult();
        verify(session).delete(userEntity);
    }

    @Test
    void testDeleteByUsernameNotFound() {
        // Given
        Query<UserEntity> query = mock(Query.class);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createQuery("from UserEntity where username = :username", UserEntity.class)).thenReturn(query);
        when(query.setParameter("username", "nonExistingUser")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        // When
        userRepositoryImpl.deleteByUsername("nonExistingUser");

        // Then
        verify(session).createQuery("from UserEntity where username = :username", UserEntity.class);
        verify(query).setParameter("username", "nonExistingUser");
        verify(query).uniqueResult();
        verify(session, never()).remove(any(UserEntity.class));
    }
}
