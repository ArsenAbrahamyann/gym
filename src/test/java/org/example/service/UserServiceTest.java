package org.example.service;

import org.example.entity.UserEntity;
import org.example.repository.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity(
            "John",
            "Doe",
            "john.doe",
            "password123",
            true
        );
    }

    @Test
    void testSaveUser() {
        userService.saveUser(user);

        verify(userDAO, times(1)).save(user);
    }

    @Test
    void testFindUserByUsername() {
        String username = "john.doe";
        when(userDAO.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<UserEntity> result = userService.findUserByUsername(username);

        assertThat(result).isPresent().contains(user);
        verify(userDAO, times(1)).findByUsername(username);
    }

    @Test
    void testFindUserByUsername_NotFound() {
        String username = "nonexistent.user";
        when(userDAO.findByUsername(username)).thenReturn(Optional.empty());

        Optional<UserEntity> result = userService.findUserByUsername(username);

        assertThat(result).isNotPresent();
        verify(userDAO, times(1)).findByUsername(username);
    }

    @Test
    void testDeleteUserByUsername() {
        String username = "john.doe";

        userService.deleteUserByUsername(username);

        verify(userDAO, times(1)).deleteByUsername(username);
    }

    @Test
    void testFindAllUsers() {
        List<UserEntity> users = Arrays.asList(
            new UserEntity("John", "Doe", "john.doe", "password123", true),
            new UserEntity("Jane", "Doe", "jane.doe", "password456", true)
        );
        when(userDAO.findAll()).thenReturn(users);

        List<UserEntity> result = userService.findAllUsers();

        assertThat(result).hasSize(2).containsExactlyElementsOf(users);
        verify(userDAO, times(1)).findAll();
    }

    @Test
    void testUpdateUser() {
        userService.updateUser(user);

        verify(userDAO, times(1)).updateUser(user);
    }
}
