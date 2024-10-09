package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.gym.dto.request.ChangeLoginRequestDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.ResourceNotFoundException;
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

    /**
     * Set up method to initialize mocks before each test case.
     * This method is run before each test to ensure that the test environment
     * is prepared correctly.
     */
    @BeforeEach
    public void setUp() {
        // This method will be called before each test method to set up the test environment.
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
    public void testAuthenticateUser_Failure() {
        String username = "testUser";
        String password = "wrongPassword";

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("testPassword");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = userService.authenticateUser(username, password);
        assertFalse(result);
        verify(userRepository).findByUsername(username);
    }

    @Test
    public void testAuthenticateUser_UserNotFound() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.authenticateUser(username, "anyPassword"));
        verify(userRepository).findByUsername(username);
    }

    @Test
    public void testChangePassword_Trainee() {
        ChangeLoginRequestDto changeLoginRequestDto = new ChangeLoginRequestDto();
        changeLoginRequestDto.setUsername("traineeUser");
        changeLoginRequestDto.setNewPassword("newPassword");

        TraineeEntity trainee = new TraineeEntity();
        trainee.setUsername("traineeUser");

        when(traineeService.getTrainee("traineeUser")).thenReturn(trainee);

        userService.changePassword(changeLoginRequestDto);

        assertEquals("newPassword", trainee.getPassword());
        verify(traineeService).updateTraineeProfile(trainee);
    }

    @Test
    public void testChangePassword_Trainer() {
        ChangeLoginRequestDto changeLoginRequestDto = new ChangeLoginRequestDto();
        changeLoginRequestDto.setUsername("trainerUser");
        changeLoginRequestDto.setNewPassword("newTrainerPassword");

        TrainerEntity trainer = new TrainerEntity();
        trainer.setUsername("trainerUser");

        when(trainerService.getTrainer("trainerUser")).thenReturn(trainer);

        userService.changePassword(changeLoginRequestDto);

        assertEquals("newTrainerPassword", trainer.getPassword());
        verify(trainerService).updateTrainerProfile(trainer);
    }

}
