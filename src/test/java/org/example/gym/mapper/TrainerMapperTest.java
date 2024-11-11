package org.example.gym.mapper;


import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.example.gym.dto.request.TrainerRegistrationRequestDto;
import org.example.gym.dto.response.GetTrainerProfileResponseDto;
import org.example.gym.dto.response.RegistrationResponseDto;
import org.example.gym.dto.response.UpdateTrainerProfileResponseDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainerMapperTest {

    @InjectMocks
    private TrainerMapper trainerMapper;

    @Mock
    private TrainingTypeEntity trainingType;

    private TrainerRegistrationRequestDto registrationDto;
    private TrainerEntity trainerEntity;
    private UserEntity user;


    @BeforeEach
    void setUp() {
        registrationDto = new TrainerRegistrationRequestDto("John", "Doe", 1L);
        trainerEntity = new TrainerEntity();
        user = new UserEntity();
        user.setFirstName("John");
        user.setLastName("Doe");
        trainerEntity.setSpecialization(trainingType);
        user.setIsActive(true);
        trainerEntity.setUser(user);
    }


    @Test
    void testTrainerMapToResponse() {
        user.setUsername("john_doe");
        user.setPassword("securePassword");

        RegistrationResponseDto result = trainerMapper.trainerMapToResponse(trainerEntity);

        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
        assertEquals("securePassword", result.getPassword());
    }

    @Test
    public void testUpdateTrainerProfileMapToResponseDto() {
        TraineeEntity traineeEntity1 = new TraineeEntity();
        user.setUsername("trainee_user_1");
        user.setFirstName("Trainee");
        user.setLastName("One");
        traineeEntity1.setUser(user);

        Set<TraineeEntity> trainees = new HashSet<>();
        trainees.add(traineeEntity1);

        user.setUsername("trainer_user");
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setIsActive(false);
        trainerEntity.setUser(user);
        trainerEntity.setSpecialization(new TrainingTypeEntity());
        trainerEntity.getSpecialization().setId(2L);
        trainerEntity.setTrainees(trainees);

        UpdateTrainerProfileResponseDto responseDto = trainerMapper.updateTrainerProfileMapToResponseDto(trainerEntity);

        assertEquals("trainer_user", responseDto.getUsername());
        assertEquals("Jane", responseDto.getFirstName());
        assertEquals("Doe", responseDto.getLastName());
        assertFalse(responseDto.isPublic());
        assertEquals(1, responseDto.getTrainerResponseDtos().size());
    }

    @Test
    void testTrainerEntityMapToGetResponse() {
        TraineeEntity trainee1 = new TraineeEntity();
        user.setUsername("trainee1");
        user.setFirstName("Alice");
        user.setLastName("Smith");
        trainee1.setUser(user);

        TraineeEntity trainee2 = new TraineeEntity();
        user.setUsername("trainee1");
        user.setFirstName("Bob");
        user.setLastName("Johnson");
        trainee2.setUser(user);

        Set<TraineeEntity> trainees = new HashSet<>();
        trainees.add(trainee1);
        trainees.add(trainee2);

        user.setUsername("trainer_user");
        trainerEntity.setUser(user);
        trainerEntity.setTrainees(trainees);
        trainerEntity.setSpecialization(new TrainingTypeEntity());
        trainerEntity.getSpecialization().setId(3L);

        GetTrainerProfileResponseDto responseDto = trainerMapper.trainerEntityMapToGetResponse(trainerEntity);

        assertNotNull(responseDto);
        assertEquals("Bob", responseDto.getFirstName());
        assertEquals("Johnson", responseDto.getLastName());
        assertEquals(3L, responseDto.getTrainingTypeId());
        assertTrue(responseDto.getTraineeListResponseDtos().size() >= 2);
    }
}