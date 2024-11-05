package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.gym.dto.request.ChangeLoginRequestDto;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.UnauthorizedException;
import org.example.gym.exeption.UserNotFoundException;
import org.example.gym.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TraineeService traineeService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testAuthenticateUser_Success() {
        String username = "testUser";
        String password = "testPassword";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = userService.authenticateUser(username, password);
        assertTrue(result);
        verify(userRepository).findByUsername(username);
    }

    @Test
    public void testAuthenticateUser_Failure_InvalidCredentials() {
        String username = "testUser";
        String password = "wrongPassword";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("testPassword");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(UnauthorizedException.class, () -> userService.authenticateUser(username, password));
        verify(userRepository).findByUsername(username);
    }

    @Test
    public void testAuthenticateUser_UserNotFound() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> userService.authenticateUser(username, "anyPassword"));
        verify(userRepository).findByUsername(username);
    }

    @Test
    public void testFindByUsername_Success() {
        String username = "testUser";
        UserEntity user = new UserEntity();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserEntity foundUser = userService.findByUsername(username);
        verify(userRepository).findByUsername(username);
    }

    @Test
    public void testFindByUsername_UserNotFound() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByUsername(username));
        verify(userRepository).findByUsername(username);
    }

    @Test
    public void testChangePassword_Success() {
        String username = "testUser";
        String newPassword = "newPassword";
        ChangeLoginRequestDto dto = new ChangeLoginRequestDto(username, "oldPassword", newPassword);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("oldPassword");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        userService.changePassword(dto);
        verify(userRepository).save(user);
        assertTrue(user.getPassword().equals(newPassword));
    }

    @Test
    public void testChangePassword_UserNotFound() {
        String username = "nonExistentUser";
        ChangeLoginRequestDto dto = new ChangeLoginRequestDto(username, "oldPassword", "newPassword");

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.changePassword(dto));
        verify(userRepository, never()).save(new UserEntity());
    }

    @Test
    public void testExistsByUsername() {
        String username = "existingUser";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        boolean exists = userService.existsByUsername(username);
        assertTrue(exists);
        verify(userRepository).existsByUsername(username);
    }

    @Test
    public void testFindById_Success() {
        long userId = 1L;
        UserEntity user = new UserEntity();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserEntity foundUser = userService.findById(userId);
        verify(userRepository).findById(userId);
    }

    @Test
    public void testFindById_UserNotFound() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(userId));
        verify(userRepository).findById(userId);
    }
}
