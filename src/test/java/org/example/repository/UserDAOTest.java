package org.example.repository;

import org.example.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDAOTest {

    @Mock
    private UserDAO userDAO;

    private UserEntity user1;
    private UserEntity user2;

    @BeforeEach
    void setUp() {
        user1 = new UserEntity("John", "Doe", "john.doe", "password123", true);
        user2 = new UserEntity("Jane", "Doe", "jane.doe", "password456", true);
    }

    @Test
    void testSaveUser() {
        doNothing().when(userDAO).save(any(UserEntity.class));

        userDAO.save(user1);
        verify(userDAO, times(1)).save(user1);
    }

    @Test
    void testFindByUsername() {
        when(userDAO.findByUsername(anyString())).thenReturn(Optional.of(user1));

        Optional<UserEntity> result = userDAO.findByUsername("john.doe");
        assertThat(result).isPresent().contains(user1);
        verify(userDAO, times(1)).findByUsername("john.doe");
    }

    @Test
    void testDeleteByUsername() {
        doNothing().when(userDAO).deleteByUsername(anyString());

        userDAO.deleteByUsername("john.doe");
        verify(userDAO, times(1)).deleteByUsername("john.doe");
    }

    @Test
    void testFindAllUsers() {
        when(userDAO.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UserEntity> result = userDAO.findAll();
        assertThat(result).hasSize(2).containsExactly(user1, user2);
        verify(userDAO, times(1)).findAll();
    }

    @Test
    void testUpdateUser() {
        doNothing().when(userDAO).updateUser(any(UserEntity.class));

        userDAO.updateUser(user1);
        verify(userDAO, times(1)).updateUser(user1);
    }
}
