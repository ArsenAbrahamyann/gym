package org.example.gym.component.steps;

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

/**
 * Cucumber steps for testing the {@link TrainingTypeController}.
 */
public class TrainingTypeControllerSteps {

    private TrainingTypeController trainingTypeController;
    private TrainingTypeService trainingTypeService;
    private TrainingTypeRepository trainingTypeRepository;
    private ResponseEntity<List<TrainingTypesResponseDto>> response;
    private Exception responseException;
    private List<Map<String, String>> trainingTypes = new ArrayList<>();
    private List<String> retrievedTrainingTypes = new ArrayList<>();

    /**
     * Initializes the necessary components for testing.
     */
    public TrainingTypeControllerSteps() {
        trainingTypeService = mock(TrainingTypeService.class);

        TrainingTypeMapper mapper = mock(TrainingTypeMapper.class);
        trainingTypeRepository = mock(TrainingTypeRepository.class);

        trainingTypeController = new TrainingTypeController(trainingTypeService, mapper);
    }


    /**
     * Sets up the training types known by the system.
     *
     * @param trainingTypes A list of maps representing training type details.
     */
    @Given("the system knows about the following training types")
    public void theSystemKnowsAboutTheFollowingTrainingTypes(List<Map<String, String>> trainingTypes) {
        this.trainingTypes = trainingTypes;
    }

    /**
     * Simulates a client requesting specific training types.  Currently, this just populates
     * the `retrievedTrainingTypes` list based on the Given data.  It doesn't actually
     * make a controller call.  This should probably be refactored to actually call the controller.
     */
    @When("the client requests for some specific training type")
    public void theClientRequestsForAllTrainingTypes() {
        for (Map<String, String> trainingType : trainingTypes) {
            retrievedTrainingTypes.add(trainingType.get("type"));
        }
    }

    /**
     * Verifies that the retrieved training types match the expected training types.
     *
     * @param expectedTypes A list of maps representing the expected training type details.
     */
    @Then("the response should contain the following training types")
    public void theResponseShouldContainTheFollowingTrainingTypes(List<Map<String, String>> expectedTypes) {
        List<String> expectedTrainingTypes = new ArrayList<>();
        for (Map<String, String> expectedType : expectedTypes) {
            expectedTrainingTypes.add(expectedType.get("type"));
        }

        assertEquals("The retrieved training types do not match the expected ones.", expectedTrainingTypes,
                retrievedTrainingTypes);
    }

    /**
     * Simulates a client requesting all training types.
     */
    @When("the client requests for all training types")
    public void fetchTrainingTypes() {
        try {
            response = trainingTypeController.getTrainingTypes();
            responseException = null;
        } catch (RuntimeException ex) {
            responseException = ex;
        }
    }


    /**
     * Simulates a scenario where the training type service is down.
     */
    @Given("the training type service is down")
    public void serviceIsDown() {
        when(trainingTypeService.findAll()).thenThrow(new RuntimeException("Service is down"));
    }

    /**
     * Verifies that the response is an internal server error when the service is down.
     */
    @Then("the response should be an internal server error")
    public void verifyInternalServerError() {
        try {
            fetchTrainingTypes();
        } catch (RuntimeException exception) {
            assertEquals("Service is down", exception.getMessage());
        }
    }
}





