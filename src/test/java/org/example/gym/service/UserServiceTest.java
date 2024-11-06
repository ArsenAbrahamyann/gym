package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.gym.dto.request.ChangeLoginRequestDto;
import org.example.gym.entity.UserEntity;
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
    public void testChangePassword_UserNotFound() {
        String username = "nonExistentUser";
        ChangeLoginRequestDto dto = new ChangeLoginRequestDto(username, "oldPassword", "newPassword");

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.changePassword(dto));
        verify(userRepository, never()).save(new UserEntity());
    }
}
