package org.example.gym.componentTesting.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Arrays;
import java.util.List;
import org.example.gym.dto.response.TrainingTypesResponseDto;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.mapper.TrainingTypeMapper;
import org.junit.Assert;

public class TrainingTypeMapperSteps {

    private TrainingTypeMapper trainingTypeMapper = new TrainingTypeMapper();
    private List<TrainingTypeEntity> trainingTypeEntities;
    private List<TrainingTypesResponseDto> resultDtos;

    @Given("a list of training type entities")
    public void a_list_of_training_type_entities() {
        trainingTypeEntities = Arrays.asList(
                new TrainingTypeEntity(1L, "Strength"),
                new TrainingTypeEntity(2L, "Cardio")
        );
    }

    @When("I map these entities to response DTOs")
    public void i_map_these_entities_to_response_dtos() {
        resultDtos = trainingTypeMapper.entityMapToResponse(trainingTypeEntities);
    }

    @Then("I should get a list of training types response DTOs")
    public void i_should_get_a_list_of_training_types_response_dtos() {
        Assert.assertNotNull(resultDtos);
        Assert.assertEquals(2, resultDtos.size());
        Assert.assertEquals("Strength", resultDtos.get(0).getTrainingType());
    }
}
