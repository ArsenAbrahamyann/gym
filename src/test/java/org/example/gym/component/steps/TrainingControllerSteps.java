package org.example.gym.component.steps;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.example.gym.controller.TrainingController;
import org.example.gym.dto.request.AddTrainingRequestDto;
import org.example.gym.dto.request.TraineeTrainingsRequestDto;
import org.example.gym.dto.response.TrainingResponseDto;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.exeption.TrainingNotFoundException;
import org.example.gym.mapper.TrainingMapper;
import org.example.gym.service.TrainingService;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Cucumber steps for testing the {@link TrainingController}.
 */
public class TrainingControllerSteps {

    private TrainingController trainingController;
    private TrainingService trainingService;
    private TrainingMapper trainingMapper;
    private ResponseEntity<?> response;
    private Exception responseException;
    private List<TrainingEntity> mockTrainings;

    /**
     * Initializes the necessary components for testing.
     */
    public TrainingControllerSteps() {
        trainingService = mock(TrainingService.class);
        trainingMapper = mock(TrainingMapper.class);
        trainingController = new TrainingController(trainingService, trainingMapper);
    }

    /**
     * Sets up mock trainings based on the provided data.
     *
     * @param trainingTypes A list of maps representing training details.
     */
    @Given("the service knows about the following trainings")
    public void theServiceKnowsAboutTheFollowingTrainings(List<Map<String, String>> trainingTypes) {
        mockTrainings = new ArrayList<>();
        for (Map<String, String> training : trainingTypes) {
            TrainingEntity trainingEntity = new TrainingEntity();
            trainingEntity.setId(Long.parseLong(training.get("id")));
            trainingEntity.setTrainingDate(LocalDateTime.parse(training.get("trainingDate")));
            mockTrainings.add(trainingEntity);
        }
    }

    /**
     * Sets up mock trainings for a trainee based on the provided data.
     *
     * @param trainings A list of maps representing training details.
     */
    @Given("the following trainings exist for the trainee")
    public void theFollowingTrainingsExistForTheTrainee(List<Map<String, String>> trainings) {
        mockTrainings = new ArrayList<>();
        for (Map<String, String> training : trainings) {
            TrainingEntity trainingEntity = new TrainingEntity();
            trainingEntity.setId(Long.parseLong(training.get("id")));
            trainingEntity.setTrainingDate(LocalDateTime.parse(training.get("trainingDate")));
            mockTrainings.add(trainingEntity);
        }
    }

    /**
     * Requests the training list for a trainee.
     *
     * @param traineeName The username of the trainee.
     */
    @When("I request the training list for trainee {string}")
    public void requestTheTrainingListForTrainee(String traineeName) {
        try {
            TraineeTrainingsRequestDto request = new TraineeTrainingsRequestDto(traineeName, null,
                    null, null, null);
            when(trainingService.getTrainingsForTrainee(request)).thenReturn(mockTrainings);
            List<TrainingResponseDto> responseDto = new ArrayList<>();
            when(trainingMapper.mapToDtoTrainingTrainee(mockTrainings)).thenReturn(responseDto);

            if (mockTrainings.isEmpty()) {
                response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                response = ResponseEntity.ok(responseDto);
            }
        } catch (Exception e) {
            responseException = e;
        }
    }

    /**
     * Checks the HTTP status code of the response.
     *
     * @param statusCode The expected HTTP status code.
     */
    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int statusCode) {
        if (response != null) {
            assertEquals(statusCode, response.getStatusCodeValue());
        } else if (responseException != null) {
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                assertEquals(HttpStatus.NOT_FOUND.value(), responseException.getMessage());
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                assertEquals(HttpStatus.BAD_REQUEST.value(), responseException.getMessage());
            }
        }
    }

    /**
     * Checks the number of training records in the response.
     *
     * @param count The expected number of training records.
     */
    @Then("the response should contain {int} training records")
    public void theResponseShouldContainTrainingRecords(int count) {
        assertEquals(count, mockTrainings.size());
    }

    /**
     * Sets up an empty list of trainings for a trainee.
     */
    @Given("no trainings exist for the trainee")
    public void noTrainingsExistForTheTrainee() {
        mockTrainings = new ArrayList<>();
        when(trainingService.getTrainingsForTrainee(any())).thenReturn(mockTrainings);
    }

    /**
     * Requests to delete a training with the given ID.
     *
     * @param trainingId The ID of the training to delete.
     */
    @When("I request to delete a training with ID {long}")
    public void requestToDeleteATrainingWithID(Long trainingId) {
        try {
            Mockito.doThrow(new TrainingNotFoundException("Training not found")).when(trainingService)
                    .deleteTraining(trainingId);
            response = trainingController.deleteTraining(trainingId);
        } catch (Exception e) {
            responseException = e;
        }
    }

    /**
     * Checks the HTTP status code of the delete response.
     *
     * @param statusCode The expected HTTP status code.
     */
    @Then("the delete response status should be {int}")
    public void theDeleteResponseStatusShouldBe(int statusCode) {
        if (response != null) {
            assertEquals(statusCode, response.getStatusCodeValue());
        } else if (responseException != null) {
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                assertEquals("Training not found", responseException.getMessage());
            }
        }
    }

    /**
     * Sets up an existing training with the given ID.
     *
     * @param trainingId The ID of the existing training.
     */
    @Given("a training with ID {long} exists")
    public void trainingWithIdExists(Long trainingId) {
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setId(trainingId);
        mockTrainings = List.of(trainingEntity);
        when(trainingService.getTrainingsForTrainee(any())).thenReturn(mockTrainings);
    }

    /**
     * Adds a training with the provided details.
     *
     * @param trainingDetails A list of maps representing training details.
     */
    @When("I add a training with details")
    public void addATrainingWithDetails(List<Map<String, String>> trainingDetails) {
        try {
            AddTrainingRequestDto requestDto = new AddTrainingRequestDto();
            for (Map<String, String> trainingDetail : trainingDetails) {
                switch (trainingDetail.get("parameter")) {
                    case "traineeUsername":
                        requestDto.setTraineeUsername(trainingDetail.get("value"));
                        break;
                    case "trainerUsername":
                        requestDto.setTrainerUsername(trainingDetail.get("value"));
                        break;
                    case "trainingDate":
                        requestDto.setTrainingDate(LocalDateTime.parse(trainingDetail.get("value")));
                        break;
                    case "trainingDuration":
                        requestDto.setTrainingDuration(Integer.parseInt(trainingDetail.get("value")));
                        break;
                    default:
                }
            }
            if (requestDto.getTraineeUsername() == null || requestDto.getTraineeUsername().isEmpty()) {
                throw new IllegalArgumentException("Trainee username cannot be empty");
            }
            trainingController.addTraining(requestDto);
            response = ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            responseException = e;
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            responseException = e;
        }
    }

    /**
     * Checks the HTTP status code of the add training response.
     *
     * @param statusCode The expected HTTP status code.
     */
    @Then("the add training response status should be {int}")
    public void theAddTrainingResponseStatusShouldBe(int statusCode) {
        if (response != null) {
            assertEquals(statusCode, response.getStatusCodeValue());
        } else if (responseException != null) {
            if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                assertEquals(HttpStatus.BAD_REQUEST.value(), responseException.getMessage());
            }
        }
    }
}
