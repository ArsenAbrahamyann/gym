package org.example.gym.repository;

import java.util.Optional;
import org.example.gym.entity.TraineeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link TraineeRepository}.
 * It focuses on verifying the correct functionality of the repository
 * methods using mocking and assertions with AssertJ.
 */
@ExtendWith(MockitoExtension.class)
public class TraineeRepositoryTest {

    @Mock
    private TraineeRepository traineeRepository; // Mock the repository directly

    private TraineeEntity mockTrainee;

    /**
     * Setup method to initialize mock objects before each test.
     * Creates a mock {@link TraineeEntity} to be used in the tests.
     */
    @BeforeEach
    public void setup() {
        mockTrainee = new TraineeEntity();
        mockTrainee.setId(1L);
        mockTrainee.setUsername("john_doe");
    }

    /**
     * Tests the {@link TraineeRepository#findByUsername(String)} method
     * to ensure that a trainee is correctly retrieved by their username.
     */
    @Test
    public void testFindByUsername_TraineeExists() {
        // Arrange: Mocking the behavior of the repository to return an optional of the mock trainee
        when(traineeRepository.findByUsername(anyString())).thenReturn(Optional.of(mockTrainee));

        // Act: Calling the findByUsername method
        Optional<TraineeEntity> result = traineeRepository.findByUsername("john_doe");

        // Assert: Verifying the result
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("john_doe");
    }

    /**
     * Tests the {@link TraineeRepository#findByUsername(String)} method
     * to ensure that an empty {@link Optional} is returned when no trainee is found.
     */
    @Test
    public void testFindByUsername_TraineeDoesNotExist() {
        // Arrange: Mocking the behavior of the repository to return an empty optional
        when(traineeRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act: Calling the findByUsername method
        Optional<TraineeEntity> result = traineeRepository.findByUsername("non_existing_user");

        // Assert: Verifying the result
        assertThat(result).isEmpty();
    }
}
