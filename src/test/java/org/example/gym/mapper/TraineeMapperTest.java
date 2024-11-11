package org.example.gym.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.example.gym.dto.request.TraineeRegistrationRequestDto;
import org.example.gym.dto.response.GetTraineeProfileResponseDto;
import org.example.gym.dto.response.RegistrationResponseDto;
import org.example.gym.dto.response.TrainerListResponseDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
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

    /**
     * Setup method to initialize test data before each test case.
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

        trainerEntity = new TrainerEntity();
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
        RegistrationResponseDto responseDto = traineeMapper.traineeEntityMapToResponseDto(traineeEntity);

        assertThat(responseDto.getUsername()).isEqualTo(userEntity.getUsername());
        assertThat(responseDto.getPassword()).isEqualTo(userEntity.getPassword());
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


}