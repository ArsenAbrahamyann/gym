package org.example.gym.componentTesting.steps;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.example.gym.controller.TrainingTypeController;
import org.example.gym.dto.response.TrainingTypesResponseDto;
import org.example.gym.mapper.TrainingTypeMapper;
import org.example.gym.repository.TrainingTypeRepository;
import org.example.gym.service.TrainingTypeService;
import org.springframework.http.ResponseEntity;

public class TrainingTypeControllerSteps {

    private TrainingTypeController trainingTypeController;
    private TrainingTypeService trainingTypeService;
    private TrainingTypeRepository trainingTypeRepository;
    private ResponseEntity<List<TrainingTypesResponseDto>> response;
    private Exception responseException;
    private List<Map<String, String>> trainingTypes = new ArrayList<>();
    private List<String> retrievedTrainingTypes = new ArrayList<>();

    public TrainingTypeControllerSteps() {
        trainingTypeService = mock(TrainingTypeService.class);

        TrainingTypeMapper mapper = mock(TrainingTypeMapper.class);
        trainingTypeRepository = mock(TrainingTypeRepository.class);

        trainingTypeController = new TrainingTypeController(trainingTypeService, mapper);
    }

    @Given("the system knows about the following training types")
    public void theSystemKnowsAboutTheFollowingTrainingTypes(List<Map<String, String>> trainingTypes) {
        this.trainingTypes = trainingTypes;
    }

    @When("the client requests for some specific training type")
    public void theClientRequestsForAllTrainingTypes() {
        for (Map<String, String> trainingType : trainingTypes) {
            retrievedTrainingTypes.add(trainingType.get("type"));
        }
    }

    @Then("the response should contain the following training types")
    public void theResponseShouldContainTheFollowingTrainingTypes(List<Map<String, String>> expectedTypes) {
        List<String> expectedTrainingTypes = new ArrayList<>();
        for (Map<String, String> expectedType : expectedTypes) {
            expectedTrainingTypes.add(expectedType.get("type"));
        }

        assertEquals("The retrieved training types do not match the expected ones.",expectedTrainingTypes,
                retrievedTrainingTypes);
    }

    @When("the client requests for all training types")
    public void fetchTrainingTypes() {
        try {
            response = trainingTypeController.getTrainingTypes();
            responseException = null;
        } catch (RuntimeException ex) {
            responseException = ex;
        }
    }

    @Given("the training type service is down")
    public void serviceIsDown() {
        when(trainingTypeService.findAll()).thenThrow(new RuntimeException("Service is down"));
    }

    @Then("the response should be an internal server error")
    public void verifyInternalServerError() {
        try {
            fetchTrainingTypes();
        } catch (RuntimeException exception) {
            assertEquals("Service is down", exception.getMessage());
        }
    }
}





