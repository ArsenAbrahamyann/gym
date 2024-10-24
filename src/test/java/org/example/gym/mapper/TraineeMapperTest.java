package org.example.gym.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Set;
import org.example.gym.dto.request.TraineeRegistrationRequestDto;
import org.example.gym.dto.request.UpdateTraineeRequestDto;
import org.example.gym.dto.response.RegistrationResponseDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.service.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class TraineeMapperTest {

    @Mock
    private TraineeService traineeService;

    @InjectMocks
    private TraineeMapper traineeMapper;

    private TraineeEntity traineeEntity;
    private TrainerEntity trainerEntity;

    /**
     * Sets up mock data for the tests.
     */
    @BeforeEach
    public void setUp() {
        // Initialize mock trainee entity
        traineeEntity = new TraineeEntity();
        traineeEntity.setUsername("trainee123");
        traineeEntity.setFirstName("John");
        traineeEntity.setLastName("Doe");
        traineeEntity.setAddress("123 Fitness Street");
        traineeEntity.setDateOfBirth(LocalDateTime.of(1990, 5, 20, 0, 0));
        traineeEntity.setIsActive(true);

        // Initialize mock trainer entity
        trainerEntity = new TrainerEntity();
        trainerEntity.setUsername("trainer.123");
        trainerEntity.setFirstName("trainer");
        trainerEntity.setLastName("123");
        trainerEntity.setSpecialization(new TrainingTypeEntity(1L, "Cardio"));
        traineeEntity.setTrainers(Set.of(trainerEntity));
    }


    @Test
    public void testTraineeEntityMapToResponseDto() {
        // Act
        RegistrationResponseDto responseDto = traineeMapper.traineeEntityMapToResponseDto(traineeEntity);

        // Assert
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getUsername()).isEqualTo("trainee123");
        assertThat(responseDto.getPassword()).isNull(); // Password is not set in the setup method
    }


    @Test
    public void testTraineeRegistrationMapToEntity() {
        // Arrange
        TraineeRegistrationRequestDto requestDto = new TraineeRegistrationRequestDto();
        requestDto.setFirsName("John");
        requestDto.setLastName("Doe");
        requestDto.setAddress("123 Fitness Street");
        requestDto.setDateOfBrith(LocalDateTime.of(1990, 5, 20, 10, 10));

        // Act
        TraineeEntity mappedTrainee = traineeMapper.traineeRegistrationMapToEntity(requestDto);

        // Assert
        assertThat(mappedTrainee).isNotNull();
        assertThat(mappedTrainee.getFirstName()).isEqualTo("John");
        assertThat(mappedTrainee.getLastName()).isEqualTo("Doe");
        assertThat(mappedTrainee.getAddress()).isEqualTo("123 Fitness Street");
        assertThat(mappedTrainee.getDateOfBirth()).isEqualTo(LocalDateTime.of(1990, 5,
                20, 10, 10));
        assertThat(mappedTrainee.getIsActive()).isTrue();
    }


    @Test
    public void testUpdateDtoMapToTraineeEntity() {
        // Arrange
        UpdateTraineeRequestDto updateRequest = new UpdateTraineeRequestDto();
        updateRequest.setUsername("trainee123");
        updateRequest.setFirstName("Johnny");
        updateRequest.setLastName("Doe");
        updateRequest.setAddress("456 Fitness Lane");
        updateRequest.setDateOfBirth("1990-05-20T00:00:00");
        updateRequest.setPublic(true);

        when(traineeService.getTrainee("trainee123")).thenReturn(traineeEntity);

        // Act
        TraineeEntity updatedTrainee = traineeMapper.updateDtoMapToTraineeEntity(updateRequest);

        // Assert
        assertThat(updatedTrainee).isNotNull();
        assertThat(updatedTrainee.getFirstName()).isEqualTo("Johnny");
        assertThat(updatedTrainee.getLastName()).isEqualTo("Doe");
        assertThat(updatedTrainee.getAddress()).isEqualTo("456 Fitness Lane");
        assertThat(updatedTrainee.getIsActive()).isTrue();
    }

}
