package org.example.gym.component.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Arrays;
import java.util.List;
import org.example.gym.dto.response.TrainingTypesResponseDto;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.mapper.TrainingTypeMapper;
import org.junit.Assert;

/**
 * Cucumber steps for testing the {@link TrainingTypeMapper}.
 */
public class TrainingTypeMapperSteps {

    private TrainingTypeMapper trainingTypeMapper = new TrainingTypeMapper();
    private List<TrainingTypeEntity> trainingTypeEntities;
    private List<TrainingTypesResponseDto> resultDtos;

    /**
     * Sets up a list of {@link TrainingTypeEntity} objects for testing.
     */
    @Given("a list of training type entities")
    public void list_of_training_type_entities() {
        trainingTypeEntities = Arrays.asList(
                new TrainingTypeEntity(1L, "Strength"),
                new TrainingTypeEntity(2L, "Cardio")
        );
    }

    /**
     * Maps the list of {@link TrainingTypeEntity} objects to a list of {@link TrainingTypesResponseDto} objects.
     */
    @When("I map these entities to response DTOs")
    public void map_these_entities_to_response_dtos() {
        resultDtos = trainingTypeMapper.entityMapToResponse(trainingTypeEntities);
    }

    /**
     * Verifies that the mapping resulted in a list of {@link TrainingTypesResponseDto} objects
     * with the correct details.
     */
    @Then("I should get a list of training types response DTOs")
    public void should_get_a_list_of_training_types_response_dtos() {
        Assert.assertNotNull(resultDtos);
        Assert.assertEquals(2, resultDtos.size());
        Assert.assertEquals("Strength", resultDtos.get(0).getTrainingType());
    }
}
