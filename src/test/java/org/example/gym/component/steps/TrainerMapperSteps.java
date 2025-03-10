package org.example.gym.component.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.gym.dto.request.TrainerRegistrationRequestDto;
import org.example.gym.dto.response.GetTrainerProfileResponseDto;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.mapper.TrainerMapper;
import org.example.gym.utils.UserUtils;
import org.mockito.Mockito;

/**
 * Cucumber steps for testing the {@link TrainerMapper}.
 */
public class TrainerMapperSteps {

    private TrainerMapper trainerMapper;
    private UserUtils userUtils;
    private TrainerEntity trainerEntity;
    private GetTrainerProfileResponseDto response;

    /**
     * Initializes the {@link TrainerMapper} and mocks the {@link UserUtils}.
     */
    public TrainerMapperSteps() {
        userUtils = Mockito.mock(UserUtils.class);
        trainerMapper = new TrainerMapper(userUtils);
    }

    /**
     * Sets up a trainer registration request with the given first and last names.
     * Mocks the {@link UserUtils} to return a generated username and password.
     *
     * @param firstName The first name of the trainer.
     * @param lastName  The last name of the trainer.
     */
    @Given("a trainer registration request with first name {string} and last name {string}")
    public void trainerRegistrationRequest(String firstName, String lastName) {
        Mockito.when(userUtils.generateUsername(firstName, lastName)).thenReturn(firstName.toLowerCase()
                + "." + lastName.toLowerCase());
        Mockito.when(userUtils.generatePassword()).thenReturn("securePass123");
        TrainerRegistrationRequestDto dto = new TrainerRegistrationRequestDto(firstName, lastName, 1L);
        trainerEntity = trainerMapper.trainerRegistrationMapToEntity(dto);
    }

    /**
     * Maps the trainer registration request to a {@link TrainerEntity}.
     */
    @When("the request is mapped to a TrainerEntity")
    public void mapTrainerEntity() {
        assertThat(trainerEntity).isNotNull();
    }

    /**
     * Verifies that the mapped {@link TrainerEntity} has the correct username.
     *
     * @param expectedUsername The expected username.
     */
    @Then("the mapped entity should have username {string}")
    public void verifyMappedUsername(String expectedUsername) {
        assertThat(trainerEntity.getUser().getUsername()).isEqualTo(expectedUsername);
    }

    /**
     * Verifies that the mapped {@link TrainerEntity} has an active status.
     */
    @Then("the mapped entity should have an active status")
    public void verifyActiveStatus() {
        assertThat(trainerEntity.getUser().getIsActive()).isTrue();
    }

    /**
     * Sets up a {@link TrainerEntity} with the given first and last names.
     *
     * @param firstName The first name of the trainer.
     * @param lastName  The last name of the trainer.
     */
    @Given("a TrainerEntity with first name {string} and last name {string}")
    public void trainerEntitySetup(String firstName, String lastName) {
        trainerEntity = new TrainerEntity();
        UserEntity user = new UserEntity();
        TrainingTypeEntity trainingType = new TrainingTypeEntity();
        trainingType.setId(2L);
        trainingType.setTrainingTypeName("yoga");
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setIsActive(true);
        user.setId(1L);
        trainerEntity.setUser(user);
        trainerEntity.setSpecialization(trainingType);
    }

    /**
     * Maps the {@link TrainerEntity} to a {@link GetTrainerProfileResponseDto}.
     */
    @When("it is mapped to GetTrainerProfileResponseDto")
    public void mapTrainerToGetResponse() {
        response = trainerMapper.trainerEntityMapToGetResponse(trainerEntity);
    }

    /**
     * Verifies that the mapped {@link GetTrainerProfileResponseDto} contains the correct first name.
     *
     * @param expectedFirstName The expected first name.
     */
    @Then("the response should contain first name {string}")
    public void verifyResponseFirstName(String expectedFirstName) {
        assertThat(response.getFirstName()).isEqualTo(expectedFirstName);
    }

    /**
     * Verifies that the mapped {@link GetTrainerProfileResponseDto} contains a non-null trainee list.
     */
    @Then("should contain trainees list")
    public void verifyTraineeList() {
        assertThat(response.getTraineeListResponseDtos()).isNotNull();
    }
}
