package org.example.gym.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.example.gym.dto.request.TraineeRegistrationRequestDto;
import org.example.gym.dto.response.GetTraineeProfileResponseDto;
import org.example.gym.dto.response.RegistrationResponseDto;
import org.example.gym.dto.response.TrainerListResponseDto;
import org.example.gym.dto.response.TrainerResponseDto;
import org.example.gym.dto.response.UpdateTraineeResponseDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class TraineeMapperTest {

    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private TraineeMapper traineeMapper;

    private TraineeRegistrationRequestDto requestDto;
    private TraineeEntity traineeEntity;
    private UserEntity userEntity;
    private TrainerEntity trainerEntity;
    private TrainingTypeEntity trainingTypeEntity;

    /**
     * Initializes the test data before each test.
     * <p>
     * This method sets up the necessary objects and data for the test cases. It ensures that
     * each test runs with a fresh set of objects, initialized with predefined values. The setup
     * includes the creation of a {@link TraineeRegistrationRequestDto}, a {@link UserEntity}, a
     * {@link TraineeEntity}, a {@link TrainingTypeEntity}, and a {@link TrainerEntity}. These
     * objects represent the various entities used in the tests, and their fields are set with
     * test-specific values. This ensures the tests are isolated and unaffected by the state of
     * other tests.
     * </p>
     */
    @BeforeEach
    void setup() {
        requestDto = new TraineeRegistrationRequestDto();
        requestDto.setAddress("Test Address");
        requestDto.setDateOfBrith(LocalDateTime.now());
        requestDto.setLastName("Doe");
        requestDto.setFirsName("John");

        userEntity = new UserEntity();
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setUsername("johndoe");
        userEntity.setPassword("password123");
        userEntity.setIsActive(true);

        traineeEntity = new TraineeEntity();
        traineeEntity.setUser(userEntity);
        traineeEntity.setAddress("Test Address");
        traineeEntity.setDateOfBirth(LocalDateTime.now());

        trainingTypeEntity = new TrainingTypeEntity();
        trainingTypeEntity.setId(2L);


        trainerEntity = new TrainerEntity();
        trainerEntity.setSpecialization(trainingTypeEntity);
        trainerEntity.setUser(userEntity);
    }

    @Test
    void testTraineeRegistrationMapToEntity() {
        when(userUtils.generateUsername(requestDto.getFirsName(), requestDto.getLastName())).thenReturn("johndoe");
        when(userUtils.generatePassword()).thenReturn("password123");

        TraineeEntity trainee = traineeMapper.traineeRegistrationMapToEntity(requestDto);

        assertThat(trainee.getAddress()).isEqualTo(requestDto.getAddress());
        assertThat(trainee.getDateOfBirth()).isEqualTo(requestDto.getDateOfBrith());
        assertThat(trainee.getUser().getFirstName()).isEqualTo(requestDto.getFirsName());
        assertThat(trainee.getUser().getLastName()).isEqualTo(requestDto.getLastName());
        assertThat(trainee.getUser().getUsername()).isEqualTo("johndoe");
        assertThat(trainee.getUser().getPassword()).isEqualTo("password123");
    }

    @Test
    void testTraineeEntityMapToResponseDto() {
        String password = "password123";
        RegistrationResponseDto responseDto = traineeMapper.traineeEntityMapToResponseDto(traineeEntity, password);

        assertThat(responseDto.getUsername()).isEqualTo(userEntity.getUsername());
        assertThat(responseDto.getPassword()).isEqualTo(password);
    }

    @Test
    void testTraineeEntityMapToGetResponseTraineeDto() {
        Set<TrainerEntity> trainers = new HashSet<>();
        trainers.add(trainerEntity);
        traineeEntity.setTrainers(trainers);

        GetTraineeProfileResponseDto profileResponseDto = traineeMapper
                .traineeEntityMapToGetResponseTraineeDto(traineeEntity);

        assertThat(profileResponseDto.getAddress()).isEqualTo(traineeEntity.getAddress());
        assertThat(profileResponseDto.isActive()).isEqualTo(userEntity.getIsActive());
        assertThat(profileResponseDto.getDateOfBride()).isEqualTo(traineeEntity.getDateOfBirth());
        assertThat(profileResponseDto.getTrainerList()).hasSize(1);

        TrainerListResponseDto trainerResponseDto = profileResponseDto.getTrainerList().iterator().next();
        assertThat(trainerResponseDto.getTrainerName()).isEqualTo(userEntity.getUsername());
        assertThat(trainerResponseDto.getFirstName()).isEqualTo(userEntity.getFirstName());
        assertThat(trainerResponseDto.getLastName()).isEqualTo(userEntity.getLastName());
    }

    @Test
    void testTraineeEntityMapToUpdateResponse() {
        Set<TrainerEntity> trainers = new HashSet<>();
        trainers.add(trainerEntity);
        traineeEntity.setTrainers(trainers);

        UpdateTraineeResponseDto updateResponseDto = traineeMapper.traineeEntityMapToUpdateResponse(traineeEntity);

        assertThat(updateResponseDto.getUsername()).isEqualTo(userEntity.getUsername());
        assertThat(updateResponseDto.getFirstName()).isEqualTo(userEntity.getFirstName());
        assertThat(updateResponseDto.getLastName()).isEqualTo(userEntity.getLastName());
        assertThat(updateResponseDto.getDateOfBirth()).isEqualTo(traineeEntity.getDateOfBirth());
        assertThat(updateResponseDto.getTrainerList()).hasSize(1);
    }

    @Test
    void testMapToTrainerResponse() {
        List<TrainerEntity> trainers = List.of(trainerEntity);
        List<TrainerResponseDto> trainerResponseDtos = traineeMapper.mapToTrainerResponse(trainers);

        assertThat(trainerResponseDtos).hasSize(1);
        assertThat(trainerResponseDtos.get(0).getTrainerName()).isEqualTo(userEntity.getUsername());
    }

    @Test
    void testUpdateTraineeTrainerListMapToTrainerResponse() {
        Set<TrainerEntity> trainers = new HashSet<>();
        trainers.add(trainerEntity);
        traineeEntity.setTrainers(trainers);

        List<TrainerResponseDto> trainerResponseDtos = traineeMapper
                .updateTraineeTrainerListMapToTrainerResponse(traineeEntity);

        assertThat(trainerResponseDtos).hasSize(1);
        assertThat(trainerResponseDtos.get(0).getTrainerName()).isEqualTo(userEntity.getUsername());
    }

    @Test
    void testUpdateTraineeTrainerListMapToTrainerResponseWithNoTrainers() {
        traineeEntity.setTrainers(new HashSet<>());
        List<TrainerResponseDto> trainerResponseDtos = traineeMapper
                .updateTraineeTrainerListMapToTrainerResponse(traineeEntity);

        assertThat(trainerResponseDtos).isEmpty();
    }


}