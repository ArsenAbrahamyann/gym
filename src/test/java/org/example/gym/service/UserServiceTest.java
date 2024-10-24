package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.gym.entity.UserEntity;
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

}
