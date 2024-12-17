package org.example.gym.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.example.gym.dto.request.ActivateRequestDto;
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

    private TraineeEntity traineeEntity;
    private RegistrationResponseDto registrationResponseDto;

    @BeforeEach
    public void setUp() {
        traineeEntity = new TraineeEntity();
        registrationResponseDto = new RegistrationResponseDto();
    }

    @Test
    public void testGetTraineeProfile() {
        // Arrange
        String username = "testUser";
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
        UpdateTraineeResponseDto updateResponseDto = new UpdateTraineeResponseDto();
        when(traineeService.updateTraineeProfile(requestDto)).thenReturn(traineeEntity);
        when(mapper.traineeEntityMapToUpdateResponse(traineeEntity)).thenReturn(updateResponseDto);

        // Act
        ResponseEntity<UpdateTraineeResponseDto> response = traineeController.updateTraineeProfile(requestDto);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updateResponseDto);
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
        String traineeName = "testTrainee";
        List<TrainerEntity> unassignedTrainers = new ArrayList<>();
        List<TrainerResponseDto> trainerResponseDtos = new ArrayList<>();
        when(traineeService.getUnassignedTrainers(traineeName)).thenReturn(unassignedTrainers);
        when(mapper.mapToTrainerResponse(unassignedTrainers)).thenReturn(trainerResponseDtos);

        // Act
        ResponseEntity<List<TrainerResponseDto>> response = traineeController
                .getNotAssignedOnTraineeActiveTrainers(traineeName);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(trainerResponseDtos);
    }

    @Test
    public void testUpdateTraineeTrainerList() {
        // Arrange
        UpdateTraineeTrainerListRequestDto requestDto = new UpdateTraineeTrainerListRequestDto();
        List<TrainerResponseDto> trainerResponseDtos = new ArrayList<>();
        when(traineeService.updateTraineeTrainerList(requestDto)).thenReturn(traineeEntity);
        when(mapper.updateTraineeTrainerListMapToTrainerResponse(traineeEntity)).thenReturn(trainerResponseDtos);

        // Act
        ResponseEntity<List<TrainerResponseDto>> response = traineeController.updateTraineeTrainerList(requestDto);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(trainerResponseDtos);
    }

    @Test
    public void testToggleTraineeStatus() {
        // Arrange
        ActivateRequestDto requestDto = new ActivateRequestDto();

        // Act
        ResponseEntity<Void> response = traineeController.toggleTraineeStatus(requestDto);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(traineeService).toggleTraineeStatus(requestDto);
    }

}
