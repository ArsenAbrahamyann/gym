package org.example.gym.component.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

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

/**
 * Cucumber steps for testing the {@link TrainingTypeService}.
 */
public class TrainingTypeServiceStepDefs {
    private TrainingTypeRepository trainingTypeRepository = Mockito.mock(TrainingTypeRepository.class);
    private TrainingTypeService trainingTypeService = new TrainingTypeService(trainingTypeRepository);
    private TrainingTypeEntity retrievedType;
    private List<TrainingTypeEntity> retrievedTypes;
    private Exception exception;

    /**
     * Sets up a training type with the given name in the mock repository.
     *
     * @param name the name of the training type
     */
    @Given("I have a training type named {string}")
    public void have_a_training_type_named(String name) {
        TrainingTypeEntity type = new TrainingTypeEntity();
        type.setTrainingTypeName(name);
        when(trainingTypeRepository.findByTrainingTypeName(name)).thenReturn(Optional.of(type));
    }

    /**
     * Sets up the mock repository to return an empty Optional when searching for a training type by the given name.
     *
     * @param name the name of the training type
     */
    @Given("I have no training type named {string}")
    public void have_no_training_type_named(String name) {
        when(trainingTypeRepository.findByTrainingTypeName(name)).thenReturn(Optional.empty());
    }

    /**
     * Searches for a training type by the given name using the {@link TrainingTypeService}.
     *
     * @param name the name of the training type
     */
    @When("I search for the training type by the name {string}")
    public void search_for_the_training_type_by_the_name(String name) {
        retrievedType = trainingTypeService.findByTrainingTypeName(name).orElse(null);
    }

    /**
     * Asserts that a training type was found.
     */
    @Then("I should find the training type")
    public void should_find_the_training_type() {
        assertNotNull(retrievedType);
    }

    /**
     * Sets up a training type with the given ID in the mock repository.
     *
     * @param id the ID of the training type
     */
    @Given("I have a training type with ID {long}")
    public void have_a_training_type_with_id(Long id) {
        TrainingTypeEntity type = new TrainingTypeEntity();
        type.setId(id);
        when(trainingTypeRepository.findById(id)).thenReturn(Optional.of(type));
    }

    /**
     * Retrieves a training type by the given ID using the {@link TrainingTypeService}.
     *
     * @param id the ID of the training type
     */
    @When("I retrieve the training type by the ID {long}")
    public void retrieve_the_training_type_by_the_id(Long id) {
        try {
            retrievedType = trainingTypeService.findById(id);
        } catch (TrainingTypeNotFoundException e) {
            exception = e;
        }
    }

    /**
     * Asserts that the training type details were retrieved.
     */
    @Then("I should get the training type details")
    public void should_get_the_training_type_details() {
        assertNotNull(retrievedType);
        assertNull(exception);
    }


    /**
     * Sets up multiple training types with the given names in the mock repository.
     *
     * @param names a list of training type names
     */
    @Given("I have the following training types:")
    public void have_the_following_training_types(List<String> names) {
        retrievedTypes = new ArrayList<>();
        for (String name : names) {
            TrainingTypeEntity type = new TrainingTypeEntity();
            type.setTrainingTypeName(name);
            retrievedTypes.add(type);
        }
        when(trainingTypeRepository.findAll()).thenReturn(retrievedTypes);
    }

    /**
     * Sets up the mock repository to return an empty list when retrieving all training types.
     */
    @Given("there are no training types available")
    public void there_are_no_training_types_available() {
        retrievedTypes = new ArrayList<>();
        when(trainingTypeRepository.findAll()).thenReturn(retrievedTypes);
    }

    /**
     * Retrieves all training types using the {@link TrainingTypeService}.
     */
    @When("I retrieve all training types")
    public void retrieve_all_training_types() {
        retrievedTypes = trainingTypeService.findAll();
    }

    /**
     * Asserts that a list containing all the training types was retrieved.
     */
    @Then("I should get a list containing all the training types")
    public void should_get_a_list_containing_all_the_training_types() {
        assertNotNull(retrievedTypes);
        assertFalse(retrievedTypes.isEmpty());
    }

    /**
     * Asserts that an empty list was retrieved.
     */
    @Then("I should receive an empty list")
    public void should_receive_an_empty_list() {
        assertNotNull(retrievedTypes);
        assertTrue(retrievedTypes.isEmpty());
    }

    /**
     * Asserts that no training type was found.
     */
    @Then("I should not find any training type")
    public void shouldNotFindAnyTrainingType() {
        assertNull("Expected no training type found, but got one", retrievedType);
    }

    /**
     * Sets up the mock repository to return an empty Optional when searching for a training type by the given ID.
     *
     * @param id the ID of the training type
     */
    @Given("there is no training type with ID {long}")
    public void there_is_no_training_type_with_id(Long id) {
        when(trainingTypeRepository.findById(id)).thenReturn(Optional.empty());
    }

    /**
     * Tries to retrieve a training type by the given ID using the {@link TrainingTypeService}.
     *
     * @param id the ID of the training type
     */
    @When("I try to retrieve the training type by the ID {int}")
    public void tryToRetrieveTheTrainingTypeByTheID(int id) {
        try {
            retrievedType = trainingTypeService.findById((long) id);
        } catch (TrainingTypeNotFoundException e) {
            exception = e;
        }
    }

    /**
     * Asserts that the expected error message was received.
     *
     * @param errorMessage the expected error message
     */
    @Then("I should receive a {string} error")
    public void shouldReceiveAError(String errorMessage) {
        assertNotNull("Expected exception but none was thrown", exception);
        assertEquals(errorMessage, exception.getMessage());
    }
}
