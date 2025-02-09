package org.example.gym.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.gym.dto.request.ActivateRequestDto;
import org.example.gym.dto.request.TrainerRegistrationRequestDto;
import org.example.gym.dto.request.UpdateTrainerRequestDto;
import org.example.gym.dto.response.GetTrainerProfileResponseDto;
import org.example.gym.dto.response.RegistrationResponseDto;
import org.example.gym.dto.response.UpdateTrainerProfileResponseDto;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.mapper.TrainerMapper;
import org.example.gym.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainerMapper mapper;

    @InjectMocks
    private TrainerController trainerController;

    private final TrainerRegistrationRequestDto registrationRequestDto = new TrainerRegistrationRequestDto();
    private final TrainerEntity trainerEntity = new TrainerEntity();
    private final UserEntity user = new UserEntity();
    private final RegistrationResponseDto registrationResponseDto = new RegistrationResponseDto();
    private final GetTrainerProfileResponseDto getTrainerProfileResponseDto = new GetTrainerProfileResponseDto();
    private final UpdateTrainerRequestDto updateTrainerRequestDto = new UpdateTrainerRequestDto();
    private final UpdateTrainerProfileResponseDto updateTrainerProfileResponseDto = new UpdateTrainerProfileResponseDto();
    private final ActivateRequestDto activateRequestDto = new ActivateRequestDto();

    /**
     * Sets up test data before each test execution.
     * Initializes various DTOs and entities with default values to be used in the tests.
     */
    @BeforeEach
    public void setUp() {
        registrationRequestDto.setFirstName("John");
        registrationRequestDto.setLastName("Doe");

        user.setUsername("johndoe");
        user.setPassword("123456");
        trainerEntity.setUser(user);

        registrationResponseDto.setUsername("johndoe");

        updateTrainerRequestDto.setUsername("johndoe");

        activateRequestDto.setUsername("johndoe");
    }

    @Test
    public void registerTrainer_ShouldReturnCreatedResponse() {
        when(mapper.trainerRegistrationMapToEntity(registrationRequestDto)).thenReturn(trainerEntity);
        when(trainerService.createTrainerProfile(trainerEntity)).thenReturn(trainerEntity);
        when(mapper.trainerMapToResponse(trainerEntity, "123456")).thenReturn(registrationResponseDto);

        ResponseEntity<RegistrationResponseDto> response = trainerController.registerTrainer(registrationRequestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(registrationResponseDto, response.getBody());
        verify(trainerService, times(1)).createTrainerProfile(trainerEntity);
    }

    @Test
    public void getTrainerProfile_ShouldReturnCorrectProfile() {
        when(trainerService.getTrainer(user.getUsername())).thenReturn(trainerEntity);
        when(mapper.trainerEntityMapToGetResponse(trainerEntity)).thenReturn(getTrainerProfileResponseDto);

        ResponseEntity<GetTrainerProfileResponseDto> response = trainerController.getTrainerProfile(user.getUsername());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(getTrainerProfileResponseDto, response.getBody());
    }

    @Test
    public void updateTrainerProfile_ShouldReturnUpdatedProfile() {
        when(trainerService.updateTrainerProfile(updateTrainerRequestDto)).thenReturn(trainerEntity);
        when(mapper.updateTrainerProfileMapToResponseDto(trainerEntity)).thenReturn(updateTrainerProfileResponseDto);

        ResponseEntity<UpdateTrainerProfileResponseDto> response = trainerController.updateTrainerProfile(updateTrainerRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updateTrainerProfileResponseDto, response.getBody());
    }

    @Test
    public void toggleTrainerStatus_ShouldReturnNoContent() {
        doNothing().when(trainerService).toggleTrainerStatus(activateRequestDto);

        ResponseEntity<Void> response = trainerController.toggleTrainerStatus(activateRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(trainerService, times(1)).toggleTrainerStatus(activateRequestDto);
    }
}
