package org.example.gym.component.steps;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.example.gym.controller.TraineeController;
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
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.TraineeNotFoundException;
import org.example.gym.mapper.TraineeMapper;
import org.example.gym.service.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * This class defines the step definitions for component testing of the TraineeController
 * in a gym management application. It uses Cucumber annotations to specify the behavior
 * in response to various test scenarios.
 */
public class TraineeControllerSteps {

    private TraineeController traineeController;
    private TraineeService traineeService;
    private TraineeMapper traineeMapper;
    private ResponseEntity<?> response;
    private Exception responseException;
    private TraineeEntity mockTrainee;

    /**
     * Initializes the {@code TraineeControllerSteps} class by creating mock instances of
     * {@link TraineeService} and {@link TraineeMapper}, and using them to instantiate
     * a {@link TraineeController}.
     * This setup is typically used for unit testing, allowing controlled behavior of dependencies.
     */
    public TraineeControllerSteps() {
        traineeService = mock(TraineeService.class);
        traineeMapper = mock(TraineeMapper.class);
        traineeController = new TraineeController(traineeService, traineeMapper);
    }

    /**
     * Sets up a known trainee in the system with the given details.
     *
     * @param trainees a list of maps containing the trainee details
     */
    @Given("the system knows about the following trainee")
    public void theSystemKnowsAboutTheFollowingTrainee(List<Map<String, String>> trainees) {
        mockTrainee = new TraineeEntity();
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername(trainees.get(0).get("username"));
        mockUser.setFirstName(trainees.get(0).get("firstName"));
        mockUser.setLastName(trainees.get(0).get("lastName"));
        mockUser.setPassword(trainees.get(0).get("password"));
        mockUser.setIsActive(Boolean.parseBoolean(trainees.get(0).get("isActive")));
        mockTrainee.setUser(mockUser);
    }

    /**
     * Registers a trainee with the given details.
     *
     * @param trainees a list of maps containing the trainee details
     */
    @When("I register a trainee with details")
    public void registerTraineeWithDetails(List<Map<String, String>> trainees) {
        try {
            TraineeRegistrationRequestDto requestDto = new TraineeRegistrationRequestDto();
            requestDto.setFirsName(trainees.get(0).get("firstName"));
            requestDto.setLastName(trainees.get(0).get("lastName"));
            requestDto.setDateOfBrith(LocalDateTime.parse(trainees.get(0).get("dateOfBrith")));
            requestDto.setAddress(trainees.get(0).get("address"));

            when(traineeMapper.traineeRegistrationMapToEntity(requestDto)).thenReturn(mockTrainee);
            when(traineeService.createTraineeProfile(mockTrainee)).thenReturn(mockTrainee);
            RegistrationResponseDto responseDto = new RegistrationResponseDto();
            responseDto.setUsername(mockTrainee.getUser().getUsername());
            responseDto.setPassword("dummyPassword");
            when(traineeMapper.traineeEntityMapToResponseDto(mockTrainee, "dummyPassword")).thenReturn(responseDto);

            response = traineeController.traineeRegistration(requestDto);
        } catch (Exception e) {
            responseException = e;
        }
    }

    /**
     * Registers a trainee with invalid details.
     *
     * @param trainees a list of maps containing the trainee details
     */
    @When("I register a trainee with invalid details")
    public void registerTraineeWithInvalidDetails(List<Map<String, String>> trainees) {
        try {
            TraineeRegistrationRequestDto requestDto = new TraineeRegistrationRequestDto();
            requestDto.setFirsName(null);
            requestDto.setLastName(null);

            when(traineeMapper.traineeRegistrationMapToEntity(requestDto)).thenReturn(mockTrainee);
            when(traineeService.createTraineeProfile(mockTrainee))
                    .thenThrow(new IllegalArgumentException("Invalid data"));

            response = traineeController.traineeRegistration(requestDto);
        } catch (Exception e) {
            responseException = e;
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Asserts that the trainee registration response status matches the given status code.
     *
     * @param statusCode the expected status code
     */
    @Then("the trainee registration response status should be {int}")
    public void theTraineeRegistrationResponseStatusShouldBe(int statusCode) {
        assertEquals(statusCode, response.getStatusCodeValue());
    }

    /**
     * Requests the profile for a given trainee.
     *
     * @param username the username of the trainee
     */
    @When("I request the profile for trainee {string}")
    public void requestTheProfileForTrainee(String username) {
        try {
            when(traineeService.getTrainee(username)).thenReturn(mockTrainee);
            GetTraineeProfileResponseDto responseDto = new GetTraineeProfileResponseDto();
            responseDto.setFirstName(mockTrainee.getUser().getFirstName());
            responseDto.setLastName(mockTrainee.getUser().getLastName());
            responseDto.setActive(mockTrainee.getUser().getIsActive());
            when(traineeMapper.traineeEntityMapToGetResponseTraineeDto(mockTrainee)).thenReturn(responseDto);

            response = traineeController.getTraineeProfile(username);
        } catch (TraineeNotFoundException e) {
            responseException = e;
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            responseException = e;
        }
    }

    /**
     * Requests the profile for a non-existent trainee.
     *
     * @param username the username of the trainee
     */
    @When("I request the profile for non-existent trainee {string}")
    public void requestTheProfileForNonExistentTrainee(String username) {
        try {
            when(traineeService.getTrainee(username)).thenThrow(new TraineeNotFoundException("Trainee not found"));
            response = traineeController.getTraineeProfile(username);
        } catch (TraineeNotFoundException e) {
            responseException = e;
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Asserts that the trainee profile response status matches the given status code.
     *
     * @param statusCode the expected status code
     */
    @Then("the trainee profile response status should be {int}")
    public void theTraineeProfileResponseStatusShouldBe(int statusCode) {
        assertEquals(statusCode, response.getStatusCodeValue());
    }

    /**
     * Updates the profile for a trainee with the given details.
     *
     * @param trainees a list of maps containing the updated trainee details
     */
    @When("I update the profile for trainee with details")
    public void updateTheProfileForTraineeWithDetails(List<Map<String, String>> trainees) {
        try {
            UpdateTraineeRequestDto requestDto = new UpdateTraineeRequestDto();
            requestDto.setUsername(trainees.get(0).get("username"));
            requestDto.setFirstName(trainees.get(0).get("firstName"));
            requestDto.setLastName(trainees.get(0).get("lastName"));
            requestDto.setDateOfBirth(LocalDateTime.parse(trainees.get(0).get("dateOfBrith")));
            requestDto.setAddress(trainees.get(0).get("address"));

            when(traineeService.updateTraineeProfile(requestDto)).thenReturn(mockTrainee);
            UpdateTraineeResponseDto responseDto = new UpdateTraineeResponseDto();
            responseDto.setUsername(mockTrainee.getUser().getUsername());
            responseDto.setFirstName(mockTrainee.getUser().getFirstName());
            responseDto.setLastName(mockTrainee.getUser().getLastName());
            when(traineeMapper.traineeEntityMapToUpdateResponse(mockTrainee)).thenReturn(responseDto);

            response = traineeController.updateTraineeProfile(requestDto);
        } catch (Exception e) {
            responseException = e;
        }
    }

    /**
     * Updates the profile for a trainee with invalid details.
     *
     * @param trainees a list of maps containing the updated trainee details
     */
    @When("I update the profile for trainee with invalid details")
    public void updateTheProfileForTraineeWithInvalidDetails(List<Map<String, String>> trainees) {
        try {
            UpdateTraineeRequestDto requestDto = new UpdateTraineeRequestDto();
            requestDto.setUsername(trainees.get(0).get("username"));
            requestDto.setFirstName(trainees.get(0).get("firstName"));
            requestDto.setLastName(null);

            doThrow(new IllegalArgumentException("Invalid data")).when(traineeService).updateTraineeProfile(requestDto);

            response = traineeController.updateTraineeProfile(requestDto);
        } catch (IllegalArgumentException e) {
            responseException = e;
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            responseException = e;
        }
    }

    /**
     * Asserts that the update trainee profile response status matches the given status code.
     *
     * @param statusCode the expected status code
     */
    @Then("the update trainee profile response status should be {int}")
    public void theUpdateTraineeProfileResponseStatusShouldBe(int statusCode) {
        assertEquals(statusCode, response.getStatusCodeValue());
    }

    /**
     * Toggles the status for a trainee with the given details.
     *
     * @param trainees a list of maps containing the trainee details
     */
    @When("I toggle the status for trainee with details")
    public void toggleTheStatusForTraineeWithDetails(List<Map<String, String>> trainees) {
        try {
            ActivateRequestDto requestDto = new ActivateRequestDto();
            requestDto.setUsername(trainees.get(0).get("username"));
            requestDto.setActive(Boolean.parseBoolean(trainees.get(0).get("isActive")));

            doThrow(new TraineeNotFoundException("Trainee not found"))
                    .when(traineeService).toggleTraineeStatus(requestDto);

            response = traineeController.toggleTraineeStatus(requestDto);
        } catch (TraineeNotFoundException e) {
            responseException = e;
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            responseException = e;
        }
    }

    /**
     * Asserts that the toggle trainee status response status matches the given status code.
     *
     * @param statusCode the expected status code
     */
    @Then("the toggle trainee status response status should be {int}")
    public void theToggleTraineeStatusResponseStatusShouldBe(int statusCode) {
        assertEquals(statusCode, response.getStatusCodeValue());
    }

    /**
     * Requests the unassigned trainers for a given trainee.
     *
     * @param username the username of the trainee
     */
    @When("I request the unassigned trainers for trainee {string}")
    public void requestTheUnassignedTrainersForTrainee(String username) {
        try {
            List<TrainerEntity> unassignedTrainers = new ArrayList<>();
            TrainerEntity trainer = new TrainerEntity();
            unassignedTrainers.add(trainer);

            when(traineeService.getUnassignedTrainers(username)).thenReturn(unassignedTrainers);
            List<TrainerResponseDto> responseDtos = new ArrayList<>();
            TrainerResponseDto trainerResponseDto = new TrainerResponseDto();
            trainerResponseDto.setFirstName("TrainerFirstName");
            trainerResponseDto.setLastName("TrainerLastName");
            responseDtos.add(trainerResponseDto);
            when(traineeMapper.mapToTrainerResponse(unassignedTrainers)).thenReturn(responseDtos);

            response = traineeController.getNotAssignedOnTraineeActiveTrainers(username);
        } catch (Exception e) {
            responseException = e;
        }
    }

    /**
     * Asserts that the unassigned trainers response status matches the given status code.
     *
     * @param statusCode the expected status code
     */
    @Then("the unassigned trainers response status should be {int}")
    public void theUnassignedTrainersResponseStatusShouldBe(int statusCode) {
        assertEquals(statusCode, response.getStatusCodeValue());
    }

    /**
     * Updates the trainer list for a trainee with the given details.
     *
     * @param trainees a list of maps containing the updated trainee details
     */
    @When("I update the trainer list for trainee with details")
    public void updateTheTrainerListForTraineeWithDetails(List<Map<String, String>> trainees) {
        try {
            UpdateTraineeTrainerListRequestDto requestDto = new UpdateTraineeTrainerListRequestDto();
            requestDto.setTraineeUsername(trainees.get(0).get("traineeUsername"));
            List<String> trainerUsernames = new ArrayList<>();
            trainerUsernames.add(trainees.get(0).get("trainerUsername"));
            requestDto.setTrainerUsername(trainerUsernames);

            when(traineeService.updateTraineeTrainerList(requestDto)).thenReturn(mockTrainee);
            List<TrainerResponseDto> responseDtos = new ArrayList<>();
            TrainerResponseDto trainerResponseDto = new TrainerResponseDto();
            trainerResponseDto.setFirstName("TrainerFirstName");
            trainerResponseDto.setLastName("TrainerLastName");
            responseDtos.add(trainerResponseDto);
            when(traineeMapper.updateTraineeTrainerListMapToTrainerResponse(mockTrainee)).thenReturn(responseDtos);

            response = traineeController.updateTraineeTrainerList(requestDto);
        } catch (Exception e) {
            responseException = e;
        }
    }

    /**
     * Asserts that the update trainer list response status matches the given status code.
     *
     * @param statusCode the expected status code
     */
    @Then("the update trainer list response status should be {int}")
    public void theUpdateTrainerListResponseStatusShouldBe(int statusCode) {
        assertEquals(statusCode, response.getStatusCodeValue());
    }
}
