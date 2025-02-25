package org.example.gym.componentTesting.steps;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.exeption.TrainingTypeNotFoundException;
import org.example.gym.repository.TrainingTypeRepository;
import org.example.gym.service.TrainingTypeService;
import org.mockito.Mockito;

public class TrainingTypeServiceStepDefs {
    private TrainingTypeRepository trainingTypeRepository = Mockito.mock(TrainingTypeRepository.class);
    private TrainingTypeService trainingTypeService = new TrainingTypeService(trainingTypeRepository);
    private TrainingTypeEntity retrievedType;
    private List<TrainingTypeEntity> retrievedTypes;
    private Exception exception;

    @Given("I have a training type named {string}")
    public void i_have_a_training_type_named(String name) {
        TrainingTypeEntity type = new TrainingTypeEntity();
        type.setTrainingTypeName(name);
        when(trainingTypeRepository.findByTrainingTypeName(name)).thenReturn(Optional.of(type));
    }

    @Given("I have no training type named {string}")
    public void i_have_no_training_type_named(String name) {
        when(trainingTypeRepository.findByTrainingTypeName(name)).thenReturn(Optional.empty());
    }

    @When("I search for the training type by the name {string}")
    public void i_search_for_the_training_type_by_the_name(String name) {
        retrievedType = trainingTypeService.findByTrainingTypeName(name).orElse(null);
    }

    @Then("I should find the training type")
    public void i_should_find_the_training_type() {
        assertNotNull(retrievedType);
    }
    @Given("I have a training type with ID {long}")
    public void i_have_a_training_type_with_id(Long id) {
        TrainingTypeEntity type = new TrainingTypeEntity();
        type.setId(id);
        when(trainingTypeRepository.findById(id)).thenReturn(Optional.of(type));
    }

    @When("I retrieve the training type by the ID {long}")
    public void i_retrieve_the_training_type_by_the_id(Long id) {
        try {
            retrievedType = trainingTypeService.findById(id);
        } catch (TrainingTypeNotFoundException e) {
            exception = e;
        }
    }

    @Then("I should get the training type details")
    public void i_should_get_the_training_type_details() {
        assertNotNull(retrievedType);
        assertNull(exception);
    }

    @Given("I have the following training types:")
    public void i_have_the_following_training_types(List<String> names) {
        retrievedTypes = new ArrayList<>();
        for (String name : names) {
            TrainingTypeEntity type = new TrainingTypeEntity();
            type.setTrainingTypeName(name);
            retrievedTypes.add(type);
        }
        when(trainingTypeRepository.findAll()).thenReturn(retrievedTypes);
    }

    @Given("there are no training types available")
    public void there_are_no_training_types_available() {
        retrievedTypes = new ArrayList<>();
        when(trainingTypeRepository.findAll()).thenReturn(retrievedTypes);
    }

    @When("I retrieve all training types")
    public void i_retrieve_all_training_types() {
        retrievedTypes = trainingTypeService.findAll();
    }

    @Then("I should get a list containing all the training types")
    public void i_should_get_a_list_containing_all_the_training_types() {
        assertNotNull(retrievedTypes);
        assertFalse(retrievedTypes.isEmpty());
    }

    @Then("I should receive an empty list")
    public void i_should_receive_an_empty_list() {
        assertNotNull(retrievedTypes);
        assertTrue(retrievedTypes.isEmpty());
    }

    @Then("I should not find any training type")
    public void iShouldNotFindAnyTrainingType() {
        assertNull("Expected no training type found, but got one", retrievedType);
    }

    @Given("there is no training type with ID {long}")
    public void there_is_no_training_type_with_id(Long id) {
        when(trainingTypeRepository.findById(id)).thenReturn(Optional.empty());
    }
    @When("I try to retrieve the training type by the ID {int}")
    public void iTryToRetrieveTheTrainingTypeByTheID(int id) {
        try {
            retrievedType = trainingTypeService.findById((long) id);
        } catch (TrainingTypeNotFoundException e) {
            exception = e;
        }
    }

    @Then("I should receive a {string} error")
    public void iShouldReceiveAError(String errorMessage) {
        assertNotNull("Expected exception but none was thrown", exception);
        assertEquals(errorMessage, exception.getMessage());
    }
}
