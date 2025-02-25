package org.example.gym.componentTesting.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.*;
import org.example.gym.dto.request.TrainerRegistrationRequestDto;
import org.example.gym.dto.response.GetTrainerProfileResponseDto;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.mapper.TrainerMapper;
import org.example.gym.utils.UserUtils;
import org.mockito.Mockito;

public class TrainerMapperSteps {

    private TrainerMapper trainerMapper;
    private UserUtils userUtils;
    private TrainerEntity trainerEntity;
    private GetTrainerProfileResponseDto response;

    public TrainerMapperSteps() {
        userUtils = Mockito.mock(UserUtils.class);
        trainerMapper = new TrainerMapper(userUtils);
    }

    @Given("a trainer registration request with first name {string} and last name {string}")
    public void trainerRegistrationRequest(String firstName, String lastName) {
        Mockito.when(userUtils.generateUsername(firstName, lastName)).thenReturn(firstName.toLowerCase()
                + "." + lastName.toLowerCase());
        Mockito.when(userUtils.generatePassword()).thenReturn("securePass123");
        TrainerRegistrationRequestDto dto = new TrainerRegistrationRequestDto(firstName, lastName, 1L);
        trainerEntity = trainerMapper.trainerRegistrationMapToEntity(dto);
    }

    @When("the request is mapped to a TrainerEntity")
    public void mapTrainerEntity() {
        assertThat(trainerEntity).isNotNull();
    }

    @Then("the mapped entity should have username {string}")
    public void verifyMappedUsername(String expectedUsername) {
        assertThat(trainerEntity.getUser().getUsername()).isEqualTo(expectedUsername);
    }

    @Then("the mapped entity should have an active status")
    public void verifyActiveStatus() {
        assertThat(trainerEntity.getUser().getIsActive()).isTrue();
    }

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

    @When("it is mapped to GetTrainerProfileResponseDto")
    public void mapTrainerToGetResponse() {
        response = trainerMapper.trainerEntityMapToGetResponse(trainerEntity);
    }

    @Then("the response should contain first name {string}")
    public void verifyResponseFirstName(String expectedFirstName) {
        assertThat(response.getFirstName()).isEqualTo(expectedFirstName);
    }

    @Then("should contain trainees list")
    public void verifyTraineeList() {
        assertThat(response.getTraineeListResponseDtos()).isNotNull();
    }
}
