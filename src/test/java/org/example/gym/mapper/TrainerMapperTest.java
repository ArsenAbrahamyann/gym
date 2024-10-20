package org.example.gym.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.HashSet;
import org.example.gym.dto.request.TrainerRegistrationRequestDto;
import org.example.gym.dto.request.UpdateTrainerRequestDto;
import org.example.gym.dto.response.GetTrainerProfileResponseDto;
import org.example.gym.dto.response.RegistrationResponseDto;
import org.example.gym.dto.response.TraineeListResponseDto;
import org.example.gym.dto.response.UpdateTrainerProfileResponseDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.service.TrainerService;
import org.example.gym.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainerMapperTest {

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerMapper trainerMapper;

    private TrainerEntity trainerEntity;
    private TrainingTypeEntity trainingTypeEntity;
    private TraineeEntity traineeEntity;

    /**
     * Setup method for initializing the required objects and mocks before each test.
     * This method runs before each test case to set up common objects.
     */
    @BeforeEach
    public void setUp() {
        trainingTypeEntity = new TrainingTypeEntity();
        trainingTypeEntity.setId(1L);
        trainingTypeEntity.setTrainingTypeName("Fitness");

        traineeEntity = new TraineeEntity();
        traineeEntity.setUsername("trainee123");
        traineeEntity.setFirstName("John");
        traineeEntity.setLastName("Doe");

        trainerEntity = new TrainerEntity();
        trainerEntity.setUsername("trainer123");
        trainerEntity.setFirstName("Jane");
        trainerEntity.setLastName("Smith");
        trainerEntity.setPassword("testPassword");
        trainerEntity.setIsActive(true);
        trainerEntity.setSpecialization(trainingTypeEntity);
        trainerEntity.setTrainees(new HashSet<>(Collections.singletonList(traineeEntity)));
    }

    @Test
    public void testTrainerRegistrationMapToEntity() {
        TrainerRegistrationRequestDto registrationDto = new TrainerRegistrationRequestDto();
        registrationDto.setFirstName("Jane");
        registrationDto.setLastName("Smith");
        registrationDto.setTrainingTypeId(1L);

        when(trainingTypeService.findById(1L)).thenReturn(trainingTypeEntity);

        TrainerEntity result = trainerMapper.trainerRegistrationMapToEntity(registrationDto);

        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getLastName()).isEqualTo("Smith");
        assertThat(result.getSpecialization().getId()).isEqualTo(1L);
        assertThat(result.getIsActive()).isTrue();
    }

    @Test
    public void testTrainerEntityMapToGetResponse() {
        GetTrainerProfileResponseDto responseDto = trainerMapper.trainerEntityMapToGetResponse(trainerEntity);

        assertThat(responseDto.getFirstName()).isEqualTo("Jane");
        assertThat(responseDto.getLastName()).isEqualTo("Smith");
        assertThat(responseDto.getTrainingTypeId()).isEqualTo(1L);
        assertThat(responseDto.getTraineeListResponseDtos()).hasSize(1);
        TraineeListResponseDto traineeResponse = responseDto.getTraineeListResponseDtos().get(0);
        assertThat(traineeResponse.getTraineeName()).isEqualTo("trainee123");
        assertThat(traineeResponse.getFirstName()).isEqualTo("John");
        assertThat(traineeResponse.getLastName()).isEqualTo("Doe");
    }

    @Test
    public void testUpdateTrainerProfileMapToResponseDto() {
        UpdateTrainerProfileResponseDto responseDto = trainerMapper.updateTrainerProfileMapToResponseDto(trainerEntity);

        assertThat(responseDto.getFirstName()).isEqualTo("Jane");
        assertThat(responseDto.getLastName()).isEqualTo("Smith");
        assertThat(responseDto.getTrainingTypeId()).isEqualTo(1L);
        assertThat(responseDto.getTrainerResponseDtos()).hasSize(1);
        TraineeListResponseDto traineeResponse = responseDto.getTrainerResponseDtos().get(0);
        assertThat(traineeResponse.getTraineeName()).isEqualTo("trainee123");
        assertThat(traineeResponse.getFirstName()).isEqualTo("John");
        assertThat(traineeResponse.getLastName()).isEqualTo("Doe");
    }

    @Test
    public void testUpdateRequestDtoMapToTrainerEntity() {
        UpdateTrainerRequestDto updateDto = new UpdateTrainerRequestDto();
        updateDto.setUsername("trainee123");
        updateDto.setFirstName("Jane");
        updateDto.setLastName("Doe");
        updateDto.setTrainingTypeId(1L);
        updateDto.setPublic(true);

        when(trainerService.getTrainer("trainee123")).thenReturn(trainerEntity);
        when(trainingTypeService.findById(1L)).thenReturn(trainingTypeEntity);

        TrainerEntity result = trainerMapper.updateRequestDtoMapToTrainerEntity(updateDto);

        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getSpecialization().getId()).isEqualTo(1L);
    }

    @Test
    public void testTrainerMapToResponse() {
        RegistrationResponseDto responseDto = trainerMapper.trainerMapToResponse(trainerEntity);

        assertThat(responseDto.getUsername()).isEqualTo("trainer123");
        assertThat(responseDto.getPassword()).isEqualTo("testPassword");
    }

    @Test
    public void testUpdateRequestDtoMapToTrainerEntity_ThrowsEntityNotFoundException_WhenTrainerNotFound() {
        UpdateTrainerRequestDto updateDto = new UpdateTrainerRequestDto();
        updateDto.setUsername("nonExistentTrainer");
        updateDto.setTrainingTypeId(1L);

        when(trainerService.getTrainer("nonExistentTrainer")).thenReturn(null);

        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> {
            trainerMapper.updateRequestDtoMapToTrainerEntity(updateDto);
        });
    }

    @Test
    public void testUpdateRequestDtoMapToTrainerEntity_ThrowsEntityNotFoundException_WhenTrainingTypeNotFound() {
        UpdateTrainerRequestDto updateDto = new UpdateTrainerRequestDto();
        updateDto.setUsername("trainer123");
        updateDto.setTrainingTypeId(999L);

        when(trainerService.getTrainer("trainer123")).thenReturn(trainerEntity);
        when(trainingTypeService.findById(999L)).thenReturn(null);

        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> {
            trainerMapper.updateRequestDtoMapToTrainerEntity(updateDto);
        });
    }
}
