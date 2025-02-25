package org.example.gym.component.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Optional;
import org.example.gym.dto.request.ActivateRequestDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.entity.enums.Role;
import org.example.gym.repository.TraineeRepository;
import org.example.gym.service.TraineeService;
import org.example.gym.service.TrainerService;
import org.example.gym.service.UserService;
import org.example.gym.utils.ValidationUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Step definitions for testing the TraineeService using Cucumber.
 */
public class TraineeServiceSteps {

    private TraineeService traineeService;
    private TraineeRepository traineeRepository;
    private UserService userService;
    private TrainerService trainerService;
    private ValidationUtils validationUtils;
    private BCryptPasswordEncoder passwordEncoder;
    private TraineeEntity trainee;
    private Exception exception;

    /**
     * Initializes mock dependencies and the TraineeService instance.
     */
    public TraineeServiceSteps() {
        this.traineeRepository = mock(TraineeRepository.class);
        this.userService = mock(UserService.class);
        this.trainerService = mock(TrainerService.class);
        this.validationUtils = mock(ValidationUtils.class);
        this.passwordEncoder = mock(BCryptPasswordEncoder.class);

        this.traineeService = new TraineeService(trainerService, traineeRepository,
                validationUtils, passwordEncoder, userService);
    }

    /**
     * Given a trainee with a specific username and password.
     */
    @Given("a trainee with username {string} and password {string}")
    public void traineeWithUsernameAndPassword(String username, String password) {
        trainee = new TraineeEntity();
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(Role.ROLE_TRAINEE);
        trainee.setUser(user);

        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
    }

    /**
     * When the trainee is saved.
     */
    @When("the trainee is saved")
    public void theTraineeIsSaved() {
        when(userService.save(any(UserEntity.class))).thenReturn(trainee.getUser());
        when(traineeRepository.save(any(TraineeEntity.class))).thenReturn(trainee);

        traineeService.createTraineeProfile(trainee);
    }

    /**
     * Then the trainee should be persisted in the repository.
     */
    @Then("the trainee should be persisted in the repository")
    public void theTraineeShouldBePersisted() {
        verify(traineeRepository, times(1)).save(trainee);
    }

    /**
     * Given a trainee with a specific username already exists.
     */
    @Given("a trainee with username {string} already exists")
    public void traineeWithUsernameAlreadyExists(String username) {
        trainee = new TraineeEntity();
        UserEntity user = new UserEntity();
        user.setUsername(username);
        trainee.setUser(user);
        traineeRepository.save(trainee);
        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.of(new TraineeEntity()));
    }

    /**
     * When trying to create a new trainee with the same username.
     */
    @When("trying to create a new trainee with username {string}")
    public void tryingToCreateANewTraineeWithUsername(String username) {
        try {
            traineeService.createTraineeProfile(trainee);
        } catch (Exception e) {
            exception = e;
        }
    }

    /**
     * Then an error should be thrown with a specific message.
     */
    @Then("an error should be thrown with message {string}")
    public void anErrorShouldBeThrownWithMessage(String message) {
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    /**
     * Given a trainee with a specific username exists and is inactive.
     */
    @Given("a trainee with username {string} exists and is inactive")
    public void traineeWithUsernameExistsAndIsInactive(String username) {
        trainee = new TraineeEntity();
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setIsActive(false);
        trainee.setUser(user);

        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.of(trainee));
        when(userService.save(any(UserEntity.class))).thenReturn(user);
    }

    /**
     * When trainee status is toggled to active.
     */
    @When("trainee status is toggled to active")
    public void traineeStatusIsToggledToActive() {
        ActivateRequestDto requestDto = new ActivateRequestDto(trainee.getUser().getUsername(), true);
        traineeService.toggleTraineeStatus(requestDto);
    }

    /**
     * Then the trainee should be active.
     */
    @Then("the trainee should be active")
    public void theTraineeShouldBeActive() {
        assertTrue(trainee.getUser().getIsActive());
    }

    /**
     * Given a trainee with a specific username does not exist.
     */
    @Given("a trainee with username {string} does not exist")
    public void traineeWithUsernameDoesNotExist(String username) {
        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.empty());
    }


    /**
     * When trainee status is toggled.
     */
    @When("trainee status is toggled")
    public void traineeStatusIsToggled() {
        try {
            ActivateRequestDto requestDto = new ActivateRequestDto("non_existing_user", true);
            traineeService.toggleTraineeStatus(requestDto);
        } catch (Exception e) {
            exception = e;
        }
    }

    /**
     * Given a trainee with a specific username exists.
     */
    @Given("a trainee with username {string} exists")
    public void traineeWithUsernameExists(String username) {
        trainee = new TraineeEntity();
        trainee.setUser(new UserEntity());
        trainee.getUser().setUsername(username);

        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.of(trainee));
    }

    /**
     * When trainee is deleted.
     */
    @When("trainee is deleted")
    public void traineeIsDeleted() {
        traineeService.deleteTraineeByUsername(trainee.getUser().getUsername());
    }

    /**
     * Then the trainee should no longer exist in the repository.
     */
    @Then("the trainee should no longer exist in the repository")
    public void theTraineeShouldNoLongerExist() {
        verify(traineeRepository, times(1)).delete(trainee);
    }
}
