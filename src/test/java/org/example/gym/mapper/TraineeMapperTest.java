package org.example.gym.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.example.gym.dto.request.TraineeRegistrationRequestDto;
import org.example.gym.dto.response.GetTraineeProfileResponseDto;
import org.example.gym.dto.response.RegistrationResponseDto;
import org.example.gym.dto.response.TrainerResponseDto;
import org.example.gym.dto.response.UpdateTraineeResponseDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class TraineeMapperTest {

    @InjectMocks
    private TraineeMapper traineeMapper;

    @Mock
    private TrainerEntity trainerEntity;

    private TraineeRegistrationRequestDto registrationRequestDto;
    private TraineeEntity traineeEntity;

    /**
     * Sets up the necessary preconditions for each test case in this test class.
     * This method is annotated with {@link BeforeEach} to ensure that it is
     * executed before each test method.
     *
     * <p>The setup includes creating a new instance of
     * {@link TraineeRegistrationRequestDto} with sample data for a trainee
     * registration request, and initializing a {@link TraineeEntity}
     * with corresponding fields such as username, password, first name,
     * last name, date of birth, address, activity status, and an empty
     * set of trainers.</p>
     *
     * <p>This method is designed to ensure that each test starts with
     * a consistent and known state of the objects involved, facilitating
     * reliable and repeatable testing.</p>
     */
    @BeforeEach
    public void setUp() {
        registrationRequestDto = new TraineeRegistrationRequestDto("John", "Doe",
                LocalDateTime.now(), "123 Street");
        traineeEntity = new TraineeEntity();
        traineeEntity.setUsername("john_doe");
        traineeEntity.setPassword("password");
        traineeEntity.setFirstName("John");
        traineeEntity.setLastName("Doe");
        traineeEntity.setDateOfBirth(LocalDateTime.now());
        traineeEntity.setAddress("123 Street");
        traineeEntity.setIsActive(true);
        traineeEntity.setTrainers(new HashSet<>());
    }

    @Test
    public void testTraineeRegistrationMapToEntity() {
        TraineeEntity result = traineeMapper.traineeRegistrationMapToEntity(registrationRequestDto);

        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("123 Street", result.getAddress());
        assertEquals(true, result.getIsActive());
    }

    @Test
    public void testTraineeEntityMapToResponseDto() {
        RegistrationResponseDto responseDto = traineeMapper.traineeEntityMapToResponseDto(traineeEntity);

        assertEquals("john_doe", responseDto.getUsername());
        assertEquals("password", responseDto.getPassword());
    }

    @Test
    public void testTraineeEntityMapToGetResponseTraineeDto() {
        trainerEntity.setUsername("trainer_user");
        trainerEntity.setFirstName("Trainer");
        trainerEntity.setLastName("One");

        Set<TrainerEntity> trainers = new HashSet<>();
        trainers.add(trainerEntity);
        traineeEntity.setTrainers(trainers);

        GetTraineeProfileResponseDto profileResponseDto = traineeMapper
                .traineeEntityMapToGetResponseTraineeDto(traineeEntity);

        assertEquals("John", profileResponseDto.getFirstName());
        assertEquals("Doe", profileResponseDto.getLastName());
        assertEquals("123 Street", profileResponseDto.getAddress());
        assertEquals(true, profileResponseDto.isActive());
        assertEquals(1, profileResponseDto.getTrainerList().size());
    }


    @Test
    public void testTraineeEntityMapToUpdateResponse() {
        TrainerEntity trainerEntity1 = new TrainerEntity();
        trainerEntity1.setUsername("trainer_user_1");
        trainerEntity1.setFirstName("Trainer");
        trainerEntity1.setLastName("One");
        trainerEntity1.setSpecialization(new TrainingTypeEntity());
        trainerEntity1.getSpecialization().setId(1L);

        TrainerEntity trainerEntity2 = new TrainerEntity();
        trainerEntity2.setUsername("trainer_user_2");
        trainerEntity2.setFirstName("Trainer");
        trainerEntity2.setLastName("Two");
        trainerEntity2.setSpecialization(new TrainingTypeEntity());
        trainerEntity2.getSpecialization().setId(2L);

        Set<TrainerEntity> trainers = new HashSet<>();
        trainers.add(trainerEntity1);
        trainers.add(trainerEntity2);
        traineeEntity.setTrainers(trainers);
        traineeEntity.setDateOfBirth(LocalDateTime.now());

        UpdateTraineeResponseDto responseDto = traineeMapper.traineeEntityMapToUpdateResponse(traineeEntity);

        assertEquals("john_doe", responseDto.getUsername());
        assertEquals("John", responseDto.getFirstName());
        assertEquals("Doe", responseDto.getLastName());
        assertEquals(2, responseDto.getTrainerList().size());
        assertEquals("trainer_user_1", responseDto.getTrainerList().iterator().next().getTrainerName());
    }

    @Test
    public void testMapToTrainerResponse() {
        TrainerEntity trainerEntity1 = new TrainerEntity();
        trainerEntity1.setUsername("trainer_user_1");
        trainerEntity1.setFirstName("Trainer");
        trainerEntity1.setLastName("One");
        trainerEntity1.setSpecialization(new TrainingTypeEntity());
        trainerEntity1.getSpecialization().setId(1L);

        TrainerEntity trainerEntity2 = new TrainerEntity();
        trainerEntity2.setUsername("trainer_user_2");
        trainerEntity2.setFirstName("Trainer");
        trainerEntity2.setLastName("Two");
        trainerEntity2.setSpecialization(new TrainingTypeEntity());
        trainerEntity2.getSpecialization().setId(2L);

        List<TrainerEntity> trainers = List.of(trainerEntity1, trainerEntity2);

        List<TrainerResponseDto> responseDtos = traineeMapper.mapToTrainerResponse(trainers);

        assertEquals(2, responseDtos.size());
        assertEquals("trainer_user_1", responseDtos.get(0).getTrainerName());
        assertEquals(1L, responseDtos.get(0).getTrainingTypeId());
    }

    @Test
    public void testUpdateTraineeTrainerListMapToTrainerResponse() {
        TrainerEntity trainerEntity1 = new TrainerEntity();
        trainerEntity1.setUsername("trainer_user_1");
        trainerEntity1.setFirstName("Trainer");
        trainerEntity1.setLastName("One");
        trainerEntity1.setSpecialization(new TrainingTypeEntity());
        trainerEntity1.getSpecialization().setId(1L);

        Set<TrainerEntity> trainers = new HashSet<>();
        trainers.add(trainerEntity1);
        traineeEntity.setTrainers(trainers);

        List<TrainerResponseDto> responseDtos = traineeMapper
                .updateTraineeTrainerListMapToTrainerResponse(traineeEntity);

        assertEquals(1, responseDtos.size());
        assertEquals("trainer_user_1", responseDtos.get(0).getTrainerName());
        assertEquals(1L, responseDtos.get(0).getTrainingTypeId());
    }

}