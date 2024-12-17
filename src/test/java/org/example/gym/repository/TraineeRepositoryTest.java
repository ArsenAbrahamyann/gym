package org.example.gym.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class TraineeRepositoryTest {

    @Mock
    private TraineeRepository traineeRepository;

    private TraineeEntity mockTrainee;
    private UserEntity mockUser;

    /**
     * Setup method to initialize mock objects before each test.
     * Creates a mock {@link TraineeEntity} to be used in the tests.
     */
    @BeforeEach
    public void setup() {
        mockTrainee = new TraineeEntity();
        mockTrainee.setId(1L);
        mockUser = new UserEntity();
        mockUser.setUsername("john_doe");
        mockTrainee.setUser(mockUser);
    }

    @Test
    public void testFindByUsername_TraineeExists() {
        // Arrange: Mocking the behavior of the repository to return an optional of the mock trainee
        when(traineeRepository.findByUser_Username(anyString())).thenReturn(Optional.of(mockTrainee));

        // Act: Calling the findByUsername method
        Optional<TraineeEntity> result = traineeRepository.findByUser_Username("john_doe");

        // Assert: Verifying the result
        assertThat(result).isPresent();
        assertThat(result.get().getUser().getUsername()).isEqualTo("john_doe");
    }

    @Test
    public void testFindByUsername_TraineeDoesNotExist() {
        // Arrange: Mocking the behavior of the repository to return an empty optional
        when(traineeRepository.findByUser_Username(anyString())).thenReturn(Optional.empty());

        // Act: Calling the findByUsername method
        Optional<TraineeEntity> result = traineeRepository.findByUser_Username("non_existing_user");

        // Assert: Verifying the result
        assertThat(result).isEmpty();
    }
}
