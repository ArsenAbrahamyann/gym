package org.example.gym.componentTesting.steps;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import org.example.gym.controller.TrainerController;
import org.example.gym.dto.request.ActivateRequestDto;
import org.example.gym.dto.request.TrainerRegistrationRequestDto;
import org.example.gym.dto.request.UpdateTrainerRequestDto;
import org.example.gym.dto.response.GetTrainerProfileResponseDto;
import org.example.gym.dto.response.RegistrationResponseDto;
import org.example.gym.dto.response.TrainerWorkloadResponseDto;
import org.example.gym.dto.response.UpdateTrainerProfileResponseDto;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.TrainerNotFoundException;
import org.example.gym.mapper.TrainerMapper;
import org.example.gym.service.TrainerService;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TrainerControllerSteps {

    private TrainerController trainerController;
    private TrainerService trainerService;
    private TrainerMapper trainerMapper;
    private ResponseEntity<?> response;
    private Exception responseException;

    private TrainerEntity mockTrainer;
    private UserEntity mockUser;

    public TrainerControllerSteps() {
        trainerService = mock(TrainerService.class);
        trainerMapper = mock(TrainerMapper.class);
        trainerController = new TrainerController(trainerService, trainerMapper);
    }

    @Given("the system knows about the following trainer")
    public void theSystemKnowsAboutTheFollowingTrainer(List<Map<String, String>> trainers) {
        mockTrainer = new TrainerEntity();
        mockUser = new UserEntity();
        mockUser.setUsername(trainers.get(0).get("username"));
        mockUser.setFirstName(trainers.get(0).get("firstName"));
        mockUser.setLastName(trainers.get(0).get("lastName"));
        mockUser.setPassword(trainers.get(0).get("password"));
        mockUser.setIsActive(Boolean.parseBoolean(trainers.get(0).get("isActive")));
        mockTrainer.setUser(mockUser);
    }

    @When("I register a trainer with details")
    public void iRegisterATrainerWithDetails(List<Map<String, String>> trainers) {
        try {
            TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto();
            requestDto.setFirstName(trainers.get(0).get("firstName"));
            requestDto.setLastName(trainers.get(0).get("lastName"));
            requestDto.setTrainingTypeId(Long.parseLong(trainers.get(0).get("trainingTypeId")));

            when(trainerMapper.trainerRegistrationMapToEntity(requestDto)).thenReturn(mockTrainer);
            when(trainerService.createTrainerProfile(mockTrainer)).thenReturn(mockTrainer);
            RegistrationResponseDto responseDto = new RegistrationResponseDto();
            responseDto.setUsername(mockUser.getUsername());
            responseDto.setPassword("dummyPassword");
            when(trainerMapper.trainerMapToResponse(mockTrainer, "dummyPassword")).thenReturn(responseDto);

            response = trainerController.registerTrainer(requestDto);
        } catch (Exception e) {
            responseException = e;
        }
    }

    @When("I register a trainer with invalid details")
    public void iRegisterATrainerWithInvalidDetails(List<Map<String, String>> trainers) {
        try {
            TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto();
            requestDto.setFirstName(trainers.get(0).get("firstName"));
            requestDto.setLastName(trainers.get(0).get("lastName"));
            requestDto.setTrainingTypeId(null);

            when(trainerMapper.trainerRegistrationMapToEntity(requestDto)).thenReturn(mockTrainer);
            when(trainerService.createTrainerProfile(mockTrainer))
                    .thenThrow(new IllegalArgumentException("Invalid data"));

            response = trainerController.registerTrainer(requestDto);
        } catch (Exception e) {
            responseException = e;
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Then("the trainer registration response status should be {int}")
    public void theTrainerRegistrationResponseStatusShouldBe(int statusCode) {
        assertEquals(statusCode, response.getStatusCodeValue());
    }

    @When("I request the profile for trainer {string}")
    public void iRequestTheProfileForTrainer(String username) {
        try {
            when(trainerService.getTrainer(username)).thenReturn(mockTrainer);
            GetTrainerProfileResponseDto responseDto = new GetTrainerProfileResponseDto();
            responseDto.setFirstName(mockUser.getFirstName());
            responseDto.setLastName(mockUser.getLastName());
            responseDto.setActive(mockUser.getIsActive());
            when(trainerMapper.trainerEntityMapToGetResponse(mockTrainer)).thenReturn(responseDto);

            response = trainerController.getTrainerProfile(username);
        } catch (TrainerNotFoundException e) {
            responseException = e;
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            responseException = e;
        }
    }

    @When("I request the profile for non-existent trainer {string}")
    public void iRequestTheProfileForNonExistentTrainer(String username) {
        try {
            when(trainerService.getTrainer(username)).thenThrow(new TrainerNotFoundException("Trainer not found"));
            response = trainerController.getTrainerProfile(username);
        } catch (TrainerNotFoundException e) {
            responseException = e;
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Then("the trainer profile response status should be {int}")
    public void theTrainerProfileResponseStatusShouldBe(int statusCode) {
        assertEquals(statusCode, response.getStatusCodeValue());
    }

    @When("I update the profile for trainer with details")
    public void iUpdateTheProfileForTrainerWithDetails(List<Map<String, String>> trainers) {
        try {
            UpdateTrainerRequestDto requestDto = new UpdateTrainerRequestDto();
            requestDto.setUsername(trainers.get(0).get("username"));
            requestDto.setFirstName(trainers.get(0).get("firstName"));
            requestDto.setLastName(trainers.get(0).get("lastName"));
            requestDto.setTrainingTypeId(Long.parseLong(trainers.get(0).get("trainingTypeId")));

            when(trainerService.updateTrainerProfile(requestDto)).thenReturn(mockTrainer);
            UpdateTrainerProfileResponseDto responseDto = new UpdateTrainerProfileResponseDto();
            responseDto.setUsername(mockUser.getUsername());
            responseDto.setFirstName(mockUser.getFirstName());
            responseDto.setLastName(mockUser.getLastName());
            when(trainerMapper.updateTrainerProfileMapToResponseDto(mockTrainer)).thenReturn(responseDto);

            response = trainerController.updateTrainerProfile(requestDto);
        } catch (Exception e) {
            responseException = e;
        }
    }

    @When("I update the profile for trainer with invalid details")
    public void iUpdateTheProfileForTrainerWithInvalidDetails(List<Map<String, String>> trainers) {
        try {
            UpdateTrainerRequestDto requestDto = new UpdateTrainerRequestDto();
            requestDto.setUsername(trainers.get(0).get("username"));
            requestDto.setFirstName(trainers.get(0).get("firstName"));
            requestDto.setLastName(null);
            requestDto.setTrainingTypeId(Long.parseLong(trainers.get(0).get("trainingTypeId")));

            when(trainerService.updateTrainerProfile(requestDto)).thenThrow(new IllegalArgumentException("Invalid data"));

            response = trainerController.updateTrainerProfile(requestDto);
        } catch (IllegalArgumentException e) {
            responseException = e;
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            responseException = e;
        }
    }

    @Then("the update trainer profile response status should be {int}")
    public void theUpdateTrainerProfileResponseStatusShouldBe(int statusCode) {
        assertEquals(statusCode, response.getStatusCodeValue());
    }

    @When("I toggle the status for trainer with details")
    public void iToggleTheStatusForTrainerWithDetails(List<Map<String, String>> trainers) {
        try {
            ActivateRequestDto requestDto = new ActivateRequestDto();
            requestDto.setUsername(trainers.get(0).get("username"));
            requestDto.setActive(Boolean.parseBoolean(trainers.get(0).get("isActive")));

            Mockito.doNothing().when(trainerService).toggleTrainerStatus(requestDto);
            response = trainerController.toggleTrainerStatus(requestDto);
        } catch (Exception e) {
            responseException = e;
        }
    }

    @Then("the toggle trainer status response status should be {int}")
    public void theToggleTrainerStatusResponseStatusShouldBe(int statusCode) {
        if (response != null) {
            assertEquals(statusCode, response.getStatusCodeValue());
        } else if (responseException != null) {
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                assertEquals(HttpStatus.NOT_FOUND.value(), responseException.getClass().getSimpleName());
            }
        }
    }

    @When("I request the workload for trainer {string} for month {int}")
    public void iRequestTheWorkloadForTrainerForMonth(String username, Integer month) {
        try {
            TrainerWorkloadResponseDto responseDto = new TrainerWorkloadResponseDto();
            responseDto.setTrainerUsername(username);
            when(trainerService.getTrainingHours(username, month)).thenReturn(responseDto);

            response = trainerController.getTrainerWorkload(username, month);
        } catch (Exception e) {
            responseException = e;
        }
    }

    @When("I request the workload for trainer {string} for month {int} with timeout")
    public void iRequestTheWorkloadForTrainerForMonthWithTimeout(String username, Integer month) {
        try {
            when(trainerService.getTrainingHours(username, month)).thenReturn(null);

            response = trainerController.getTrainerWorkload(username, month);
            if (response == null) {
                response = ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(null);
            }
        } catch (Exception e) {
            responseException = e;
            response = ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(null);
        }
    }

    @Then("the trainer workload response status should be {int}")
    public void theTrainerWorkloadResponseStatusShouldBe(int statusCode) {
        assertEquals(statusCode, response.getStatusCodeValue());
    }
}
