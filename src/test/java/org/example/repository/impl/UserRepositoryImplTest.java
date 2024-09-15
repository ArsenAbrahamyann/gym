package org.example.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
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
public class UserRepositoryImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @InjectMocks
    private UserRepositoryImpl userRepository;

    @BeforeEach
    public void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testFindByUsername_found() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");

        Query<UserEntity> query = mock(Query.class);
        when(session.createQuery("from UserEntity where username = :username", UserEntity.class)).thenReturn(query);
        when(query.setParameter("username", "testuser")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(userEntity);

        Optional<UserEntity> result = userRepository.findByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    public void testFindByUsername_notFound() {
        Query<UserEntity> query = mock(Query.class);
        when(session.createQuery("from UserEntity where username = :username", UserEntity.class)).thenReturn(query);
        when(query.setParameter("username", "nonexistent")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        Optional<UserEntity> result = userRepository.findByUsername("nonexistent");

        assertFalse(result.isPresent());
    }

    @Test
    public void testFindAllUsername() {
        List<String> usernames = List.of("user1", "user2");

        Query<String> query = mock(Query.class);
        when(session.createQuery("select u.username from UserEntity u", String.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(usernames);

        Optional<List<String>> result = userRepository.findAllUsername();

        assertTrue(result.isPresent());
        assertEquals(usernames, result.get());
    }

    @Test
    public void testSave() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");

        userRepository.save(userEntity);

        verify(session).persist(userEntity);
    }

    @Test
    public void testUpdate() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");

        userRepository.update(userEntity);

        verify(session).detach(userEntity);
    }

    @Test
    public void testDeleteByUsername() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");

        Query<UserEntity> query = mock(Query.class);
        when(session.createQuery("from UserEntity where username = :username", UserEntity.class)).thenReturn(query);
        when(query.setParameter("username", "testuser")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(userEntity);

        userRepository.deleteByUsername("testuser");

        verify(session).remove(userEntity);
    }
}
