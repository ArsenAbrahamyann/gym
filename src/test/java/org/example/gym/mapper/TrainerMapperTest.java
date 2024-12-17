package org.example.gym.mapper;


import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
import org.example.gym.utils.UserUtils;
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
    private UserUtils userUtils;

    @Mock
    private TrainingTypeEntity trainingType;

    private TrainerRegistrationRequestDto registrationDto;
    private TrainerEntity trainerEntity;
    private UserEntity user;

    /**
     * Initializes the test data before each test.
     * <p>
     * This method sets up the necessary objects and data for the test cases, ensuring that
     * each test runs with a fresh set of objects. The setup includes the creation of
     * a {@link TrainerRegistrationRequestDto}, a {@link TrainerEntity}, and a {@link UserEntity}
     * with predefined values. This ensures that the tests can focus on specific behavior
     * and are not affected by previous tests' states.
     * </p>
     */
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
    public void testUpdateTrainerProfileMapToResponseDto() {
        TraineeEntity traineeEntity1 = new TraineeEntity();
        UserEntity traineeUser1 = new UserEntity();
        traineeUser1.setUsername("trainee_user_1");
        traineeUser1.setFirstName("Trainee");
        traineeUser1.setLastName("One");
        traineeEntity1.setUser(traineeUser1);

        Set<TraineeEntity> trainees = new HashSet<>();
        trainees.add(traineeEntity1);

        user.setUsername("trainer_user");
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setIsActive(false);
        trainerEntity.setUser(user);
        trainerEntity.setTrainees(trainees);
        trainerEntity.setSpecialization(new TrainingTypeEntity());
        trainerEntity.getSpecialization().setId(2L);

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
        UserEntity traineeUser1 = new UserEntity();
        traineeUser1.setUsername("trainee1");
        traineeUser1.setFirstName("Alice");
        traineeUser1.setLastName("Smith");
        trainee1.setUser(traineeUser1);

        TraineeEntity trainee2 = new TraineeEntity();
        UserEntity traineeUser2 = new UserEntity();
        traineeUser2.setUsername("trainee2");
        traineeUser2.setFirstName("Bob");
        traineeUser2.setLastName("Johnson");
        trainee2.setUser(traineeUser2);

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
        assertEquals("John", responseDto.getFirstName());
        assertEquals("Doe", responseDto.getLastName());
        assertEquals(3L, responseDto.getTrainingTypeId());
        assertTrue(responseDto.getTraineeListResponseDtos().size() >= 2);
    }

    @Test
    void testTrainerRegistrationMapToEntity() {
        // Mocking the UserUtils to return a fixed username and password
        String mockedUsername = "mockedUsername";
        String mockedPassword = "mockedPassword";
        when(userUtils.generateUsername(anyString(), anyString())).thenReturn(mockedUsername);
        when(userUtils.generatePassword()).thenReturn(mockedPassword);

        TrainerEntity entity = trainerMapper.trainerRegistrationMapToEntity(registrationDto);

        assertNotNull(entity);
        assertNotNull(entity.getUser());
        assertEquals("John", entity.getUser().getFirstName());
        assertEquals("Doe", entity.getUser().getLastName());
        assertEquals(mockedUsername, entity.getUser().getUsername());
        assertEquals(mockedPassword, entity.getUser().getPassword());
        assertTrue(entity.getUser().getIsActive());
        assertNotNull(entity.getSpecialization());
        assertEquals(1L, entity.getSpecialization().getId());
    }

    @Test
    void testTrainerMapToResponse() {
        // Mocking a password for testing the response
        String password = "testPassword";

        RegistrationResponseDto responseDto = trainerMapper.trainerMapToResponse(trainerEntity, password);

        assertNotNull(responseDto);
        assertEquals(null, responseDto.getUsername()); // Assuming concatenation of first and last name for username
        assertEquals(password, responseDto.getPassword());
    }

    // Additional edge cases
    @Test
    void testUpdateTrainerProfileMapToResponseDtoWithNoTrainees() {
        trainerEntity.setTrainees(new HashSet<>()); // Empty trainees set

        UpdateTrainerProfileResponseDto responseDto = trainerMapper.updateTrainerProfileMapToResponseDto(trainerEntity);

        assertNotNull(responseDto);
        assertEquals(0, responseDto.getTrainerResponseDtos().size());
    }

    @Test
    void testTrainerEntityMapToGetResponseWithEmptyTrainees() {
        trainerEntity.setTrainees(new HashSet<>()); // Empty trainees set

        GetTrainerProfileResponseDto responseDto = trainerMapper.trainerEntityMapToGetResponse(trainerEntity);

        assertNotNull(responseDto);
        assertEquals(0, responseDto.getTraineeListResponseDtos().size());
    }
}