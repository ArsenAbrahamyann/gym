package org.example.gym.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.example.gym.dto.request.ActivateRequestDto;
import org.example.gym.dto.request.TraineeRegistrationRequestDto;
import org.example.gym.dto.request.UpdateTraineeRequestDto;
import org.example.gym.dto.request.UpdateTraineeTrainerListRequestDto;
import org.example.gym.dto.response.GetTraineeProfileResponseDto;
import org.example.gym.dto.response.RegistrationResponseDto;
import org.example.gym.dto.response.TrainerResponseDto;
import org.example.gym.dto.response.UpdateTraineeResponseDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.mapper.TraineeMapper;
import org.example.gym.service.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TraineeMapper mapper;

    @InjectMocks
    private TraineeController traineeController;

    /**
     * Sets up the test environment before each test.
     * This method initializes the necessary mocks and prepares the environment
     * for the controller tests.
     */
    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testTraineeRegistration() {
        // Arrange
        TraineeRegistrationRequestDto requestDto = new TraineeRegistrationRequestDto();
        TraineeEntity traineeEntity = new TraineeEntity(); // Assume a valid entity is created
        RegistrationResponseDto responseDto = new RegistrationResponseDto();

        when(mapper.traineeRegistrationMapToEntity(requestDto)).thenReturn(traineeEntity);
        when(traineeService.createTraineeProfile(traineeEntity)).thenReturn(traineeEntity);
        when(mapper.traineeEntityMapToResponseDto(traineeEntity)).thenReturn(responseDto);

        // Act
        ResponseEntity<RegistrationResponseDto> response = traineeController.traineeRegistration(requestDto);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(responseDto);
    }

    @Test
    public void testGetTraineeProfile() {
        // Arrange
        String username = "testUser";
        TraineeEntity traineeEntity = new TraineeEntity(); // Assume a valid entity is created
        GetTraineeProfileResponseDto responseDto = new GetTraineeProfileResponseDto();

        when(traineeService.getTrainee(username)).thenReturn(traineeEntity);
        when(mapper.traineeEntityMapToGetResponseTraineeDto(traineeEntity)).thenReturn(responseDto);

        // Act
        ResponseEntity<GetTraineeProfileResponseDto> response = traineeController.getTraineeProfile(username);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDto);
    }

    @Test
    public void testUpdateTraineeProfile() {
        // Arrange
        UpdateTraineeRequestDto requestDto = new UpdateTraineeRequestDto();
        TraineeEntity traineeEntity = new TraineeEntity(); // Assume a valid entity is created
        UpdateTraineeResponseDto responseDto = new UpdateTraineeResponseDto();

        when(mapper.updateDtoMapToTraineeEntity(requestDto)).thenReturn(traineeEntity);
        when(traineeService.updateTraineeProfile(traineeEntity)).thenReturn(traineeEntity);
        when(mapper.traineeEntityMapToUpdateResponse(traineeEntity)).thenReturn(responseDto);

        // Act
        ResponseEntity<UpdateTraineeResponseDto> response = traineeController.updateTraineeProfile(requestDto);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDto);
    }

    @Test
    public void testDeleteTraineeProfile() {
        // Arrange
        String username = "testUser";

        // Act
        ResponseEntity<Void> response = traineeController.deleteTraineeProfile(username);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(traineeService).deleteTraineeByUsername(username);
    }

    @Test
    public void testGetNotAssignedOnTraineeActiveTrainers() {
        // Arrange
        String traineeUsername = "testUser";
        List<TrainerEntity> unassignedTrainers = new ArrayList<>();
        List<TrainerResponseDto> responseDtos = new ArrayList<>();

        when(traineeService.getUnassignedTrainers(traineeUsername)).thenReturn(unassignedTrainers);
        when(mapper.mapToTrainerResponse(unassignedTrainers)).thenReturn(responseDtos);

        // Act
        ResponseEntity<List<TrainerResponseDto>> response =
                traineeController.getNotAssignedOnTraineeActiveTrainers(traineeUsername);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDtos);
    }

    @Test
    public void testUpdateTraineeTrainerList() {
        // Arrange
        UpdateTraineeTrainerListRequestDto requestDto = new UpdateTraineeTrainerListRequestDto();
        TraineeEntity traineeEntity = new TraineeEntity(); // Assume a valid entity is created
        List<TrainerResponseDto> responseDtos = new ArrayList<>();

        when(mapper.updateTraineeTrainerListMapToEntity(requestDto)).thenReturn(traineeEntity);
        when(traineeService.updateTraineeTrainers(traineeEntity)).thenReturn(traineeEntity);
        when(mapper.updateTraineeTrainerListMapToTrainerResponse(traineeEntity)).thenReturn(responseDtos);

        // Act
        ResponseEntity<List<TrainerResponseDto>> response = traineeController.updateTraineeTrainerList(requestDto);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDtos);
    }

    @Test
    public void testActivateTrainee() {
        // Arrange
        ActivateRequestDto requestDto = new ActivateRequestDto();

        // Act
        ResponseEntity<Void> response = traineeController.activateTrainee(requestDto);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(traineeService).toggleTraineeStatus(requestDto);
    }

    @Test
    public void testDeActivateTrainee() {
        // Arrange
        ActivateRequestDto requestDto = new ActivateRequestDto();

        // Act
        ResponseEntity<Void> response = traineeController.deActivateTrainee(requestDto);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(traineeService).toggleTraineeStatus(requestDto);
    }
}
