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

    private TrainerRegistrationRequestDto registrationRequestDto;
    private TrainerEntity trainerEntity;
    private RegistrationResponseDto registrationResponseDto;
    private GetTrainerProfileResponseDto getTrainerProfileResponseDto;
    private UpdateTrainerRequestDto updateTrainerRequestDto;
    private UpdateTrainerProfileResponseDto updateTrainerProfileResponseDto;
    private ActivateRequestDto activateRequestDto;

    /**
     * Sets up the test environment by initializing the test data used in the tests.
     */
    @BeforeEach
    public void setUp() {
        registrationRequestDto = new TrainerRegistrationRequestDto();
        registrationRequestDto.setFirstName("John");
        registrationRequestDto.setLastName("Doe");

        trainerEntity = new TrainerEntity();
        trainerEntity.setUsername("johndoe");

        registrationResponseDto = new RegistrationResponseDto();
        registrationResponseDto.setUsername("johndoe");

        getTrainerProfileResponseDto = new GetTrainerProfileResponseDto();
        getTrainerProfileResponseDto.setFirstName("johndoe");

        updateTrainerRequestDto = new UpdateTrainerRequestDto();
        updateTrainerRequestDto.setUsername("johndoe");

        updateTrainerProfileResponseDto = new UpdateTrainerProfileResponseDto();
        updateTrainerProfileResponseDto.setUsername("johndoe");

        activateRequestDto = new ActivateRequestDto();
        activateRequestDto.setUsername("johndoe");
    }

    /**
     * Tests the registration of a trainer.
     */
    @Test
    public void registerTrainer_ShouldReturnCreatedResponse() {
        when(mapper.trainerRegistrationMapToEntity(registrationRequestDto)).thenReturn(trainerEntity);
        when(trainerService.createTrainerProfile(trainerEntity)).thenReturn(trainerEntity);
        when(mapper.trainerMapToResponse(trainerEntity)).thenReturn(registrationResponseDto);

        ResponseEntity<RegistrationResponseDto> response = trainerController.registerTrainer(registrationRequestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(registrationResponseDto, response.getBody());
    }

    /**
     * Tests retrieval of a trainer's profile by username.
     */
    @Test
    public void getTrainerProfile_ShouldReturnOkResponse() {
        when(trainerService.getTrainer("johndoe")).thenReturn(trainerEntity);
        when(mapper.trainerEntityMapToGetResponse(trainerEntity)).thenReturn(getTrainerProfileResponseDto);

        ResponseEntity<GetTrainerProfileResponseDto> response = trainerController.getTrainerProfile("johndoe");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(getTrainerProfileResponseDto, response.getBody());
    }

    /**
     * Tests updating a trainer's profile.
     */
    @Test
    public void updateTrainerProfile_ShouldReturnOkResponse() {
        when(mapper.updateRequestDtoMapToTrainerEntity(updateTrainerRequestDto)).thenReturn(trainerEntity);
        when(trainerService.updateTrainerProfile(trainerEntity)).thenReturn(trainerEntity);
        when(mapper.updateTrainerProfileMapToResponseDto(trainerEntity)).thenReturn(updateTrainerProfileResponseDto);

        ResponseEntity<UpdateTrainerProfileResponseDto> response =
                trainerController.updateTrainerProfile(updateTrainerRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updateTrainerProfileResponseDto, response.getBody());
    }

    /**
     * Tests activation of a trainer's account.
     */
    @Test
    public void activateTrainer_ShouldReturnOkResponse() {
        doNothing().when(trainerService).toggleTrainerStatus(activateRequestDto);

        ResponseEntity<Void> response = trainerController.toggleTrainerStatus(activateRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(trainerService, times(1)).toggleTrainerStatus(activateRequestDto);
    }

}
