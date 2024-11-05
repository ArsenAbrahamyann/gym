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

    @BeforeEach
    void setUp() {
        registrationDto = new TrainerRegistrationRequestDto("John", "Doe", 1L);
        trainerEntity = new TrainerEntity();
        trainerEntity.setFirstName("John");
        trainerEntity.setLastName("Doe");
        trainerEntity.setSpecialization(trainingType);
        trainerEntity.setIsActive(true);
    }

    @Test
    void testTrainerRegistrationMapToEntity() {
        TrainerEntity result = trainerMapper.trainerRegistrationMapToEntity(registrationDto);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(1L, result.getSpecialization().getId());
        assertTrue(result.getIsActive());
    }

    @Test
    void testTrainerMapToResponse() {
        trainerEntity.setUsername("john_doe");
        trainerEntity.setPassword("securePassword");

        RegistrationResponseDto result = trainerMapper.trainerMapToResponse(trainerEntity);

        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
        assertEquals("securePassword", result.getPassword());
    }

    @Test
    public void testUpdateTrainerProfileMapToResponseDto() {
        TraineeEntity traineeEntity1 = new TraineeEntity();
        traineeEntity1.setUsername("trainee_user_1");
        traineeEntity1.setFirstName("Trainee");
        traineeEntity1.setLastName("One");

        Set<TraineeEntity> trainees = new HashSet<>();
        trainees.add(traineeEntity1);

        trainerEntity.setUsername("trainer_user");
        trainerEntity.setFirstName("Jane");
        trainerEntity.setLastName("Doe");
        trainerEntity.setIsActive(false);
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
        trainee1.setUsername("trainee1");
        trainee1.setFirstName("Alice");
        trainee1.setLastName("Smith");

        TraineeEntity trainee2 = new TraineeEntity();
        trainee2.setUsername("trainee1");
        trainee2.setFirstName("Bob");
        trainee2.setLastName("Johnson");

        Set<TraineeEntity> trainees = new HashSet<>();
        trainees.add(trainee1);
        trainees.add(trainee2);

        trainerEntity.setUsername("trainer_user");
        trainerEntity.setTrainees(trainees);
        trainerEntity.setSpecialization(new TrainingTypeEntity());
        trainerEntity.getSpecialization().setId(3L);

        GetTrainerProfileResponseDto responseDto = trainerMapper.trainerEntityMapToGetResponse(trainerEntity);

        assertNotNull(responseDto);
        assertEquals("John", responseDto.getFirstName());
        assertEquals("Doe", responseDto.getLastName());
        assertEquals(3L, responseDto.getTrainingTypeId());
        assertTrue(responseDto.getTraineeListResponseDtos().size() >= 2);
        assertEquals("trainee1", responseDto.getTraineeListResponseDtos().get(0).getTraineeName());
        assertEquals("trainee1", responseDto.getTraineeListResponseDtos().get(1).getTraineeName());
    }
}
