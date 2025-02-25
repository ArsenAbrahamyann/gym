package org.example.gym.componentTesting.steps;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.gym.dto.request.ActivateRequestDto;
import org.example.gym.dto.request.UpdateTrainerRequestDto;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.TrainerNotFoundException;
import org.example.gym.repository.TrainerRepository;
import org.example.gym.service.TrainerService;
import org.example.gym.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class TrainerServiceStepDefs {
    private TrainerService trainerService;
    private TrainerEntity trainer;
    private UserEntity user;
    private Exception exceptionStore;
    private boolean operationSuccess;

    @Given("a new trainer data with valid details")
    public void a_new_trainer_data_with_valid_details() {
        TrainerRepository trainerRepository = mock(TrainerRepository.class);
        UserService userService = mock(UserService.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);

        // Assuming TrainerEntity and UserEntity have all-args constructors for simplicity
        user = new UserEntity(1L, "user1", "John", "Doe", "pass1234",
                true, null, null);
        trainer = new TrainerEntity(2L, user, new TrainingTypeEntity(3L, "Yoga"),
                null, null);

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userService.save(any(UserEntity.class))).thenReturn(user);
        when(trainerRepository.save(any(TrainerEntity.class))).thenReturn(trainer);

        trainerService = new TrainerService(trainerRepository, null, null,
                userService, passwordEncoder, null);
    }

    @When("I attempt to create the trainer profile")
    public void i_attempt_to_create_the_trainer_profile() {
        try {
            trainer = trainerService.createTrainerProfile(trainer);
            operationSuccess = true;
        } catch (Exception e) {
            exceptionStore = e;
            operationSuccess = false;
        }
    }

    @Then("the trainer profile should be created successfully")
    public void the_trainer_profile_should_be_created_successfully() {
        assertTrue("Trainer profile creation should succeed", operationSuccess);
    }

    @Given("an existing trainer with a specified username")
    public void an_existing_trainer_with_a_specified_username() {
        TrainerRepository trainerRepository = mock(TrainerRepository.class);
        UserService userService = mock(UserService.class);

        user = new UserEntity(1L, "existingUser", "Jane", "Doe", "encoded1234",
                true, null, null);
        trainer = new TrainerEntity(2L, user,new TrainingTypeEntity(3L, "Yoga"),
                null, null);

        when(trainerRepository.findByUser_Username("existingUser")).thenReturn(java.util.Optional.of(trainer));
        when(userService.save(any(UserEntity.class))).thenReturn(user);
        trainerService = new TrainerService(trainerRepository, null, null,
                userService, mock(BCryptPasswordEncoder.class), null);
    }

    @When("I toggle the trainer's active status")
    public void i_toggle_the_trainer_s_active_status() {
        try {
            ActivateRequestDto requestDto = new ActivateRequestDto("existingUser", false);
            trainerService.toggleTrainerStatus(requestDto);
            operationSuccess = true;
        } catch (Exception e) {
            exceptionStore = e;
            operationSuccess = false;
        }
    }

    @Then("the trainer's status should be updated successfully")
    public void the_trainer_s_status_should_be_updated_successfully() {
        assertTrue("Trainer status should be updated successfully", operationSuccess);
        assertFalse("Trainer active status should be toggled off", trainer.getUser().getIsActive());
    }

    @Given("a non-existent trainer username")
    public void a_non_existent_trainer_username() {
        TrainerRepository trainerRepository = mock(TrainerRepository.class);
        when(trainerRepository.findByUser_Username(anyString())).thenReturn(java.util.Optional.empty());
        trainerService = new TrainerService(trainerRepository, null, null,
                mock(UserService.class), mock(BCryptPasswordEncoder.class), null);
    }

    @Then("the status toggling should fail with an error")
    public void the_status_toggling_should_fail_with_an_error() {
        assertFalse("Operation should fail due to non-existent trainer", operationSuccess);
        assertNotNull("Exception should be thrown for non-existent trainer", exceptionStore);
    }

    @Given("update details for a non-existent trainer")
    public void update_details_for_a_non_existent_trainer() {
        TrainerRepository trainerRepository = mock(TrainerRepository.class);
        when(trainerRepository.findByUser_Username(anyString())).thenReturn(java.util.Optional.empty());
        trainerService = new TrainerService(trainerRepository, null, null,
                mock(UserService.class), mock(BCryptPasswordEncoder.class), null);
    }

    @When("I try to update the trainer's profile")
    public void i_try_to_update_the_trainer_s_profile() {
        try {
            UpdateTrainerRequestDto requestDto = new UpdateTrainerRequestDto();
            requestDto.setUsername("nonExistentUser");
            trainerService.updateTrainerProfile(requestDto);
            operationSuccess = true;
        } catch (Exception e) {
            exceptionStore = e;
            operationSuccess = false;
        }
    }

    @Then("the update should fail with an error message")
    public void the_update_should_fail_with_an_error_message() {
        assertFalse("Update operation should have failed", operationSuccess);
        assertTrue("Error message should be related to 'Trainer not found'",
                exceptionStore instanceof TrainerNotFoundException);
    }

}
