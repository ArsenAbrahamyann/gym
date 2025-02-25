package org.example.gym.componentTesting.steps;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.cucumber.java.en.*;
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

public class TraineeServiceSteps {

    private TraineeService traineeService;
    private TraineeRepository traineeRepository;
    private UserService userService;
    private TrainerService trainerService;
    private ValidationUtils validationUtils;
    private BCryptPasswordEncoder passwordEncoder;

    private TraineeEntity trainee;
    private Exception exception;

    public TraineeServiceSteps() {
        this.traineeRepository = mock(TraineeRepository.class);
        this.userService = mock(UserService.class);
        this.trainerService = mock(TrainerService.class);
        this.validationUtils = mock(ValidationUtils.class);
        this.passwordEncoder = mock(BCryptPasswordEncoder.class);

        this.traineeService = new TraineeService(trainerService, traineeRepository, validationUtils, passwordEncoder, userService);
    }

    @Given("a trainee with username {string} and password {string}")
    public void aTraineeWithUsernameAndPassword(String username, String password) {
        trainee = new TraineeEntity();
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(Role.ROLE_TRAINEE);
        trainee.setUser(user);

        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
    }

    @When("the trainee is saved")
    public void theTraineeIsSaved() {
        when(userService.save(any(UserEntity.class))).thenReturn(trainee.getUser());
        when(traineeRepository.save(any(TraineeEntity.class))).thenReturn(trainee);

        traineeService.createTraineeProfile(trainee);
    }

    @Then("the trainee should be persisted in the repository")
    public void theTraineeShouldBePersisted() {
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Given("a trainee with username {string} already exists")
    public void aTraineeWithUsernameAlreadyExists(String username) {
        trainee = new TraineeEntity();
        UserEntity user = new UserEntity();
        user.setUsername(username);
        trainee.setUser(user);
        traineeRepository.save(trainee);
        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.of(new TraineeEntity()));
    }

    @When("trying to create a new trainee with username {string}")
    public void tryingToCreateANewTraineeWithUsername(String username) {
        try {
            traineeService.createTraineeProfile(trainee);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("an error should be thrown with message {string}")
    public void anErrorShouldBeThrownWithMessage(String message) {
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Given("a trainee with username {string} exists and is inactive")
    public void aTraineeWithUsernameExistsAndIsInactive(String username) {
        trainee = new TraineeEntity();
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setIsActive(false);
        trainee.setUser(user);

        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.of(trainee));
        when(userService.save(any(UserEntity.class))).thenReturn(user);
    }

    @When("trainee status is toggled to active")
    public void traineeStatusIsToggledToActive() {
        ActivateRequestDto requestDto = new ActivateRequestDto(trainee.getUser().getUsername(), true);
        traineeService.toggleTraineeStatus(requestDto);
    }

    @Then("the trainee should be active")
    public void theTraineeShouldBeActive() {
        assertTrue(trainee.getUser().getIsActive());
    }

    @Given("a trainee with username {string} does not exist")
    public void aTraineeWithUsernameDoesNotExist(String username) {
        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.empty());
    }

    @When("trainee status is toggled")
    public void traineeStatusIsToggled() {
        try {
            ActivateRequestDto requestDto = new ActivateRequestDto("non_existing_user", true);
            traineeService.toggleTraineeStatus(requestDto);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Given("a trainee with username {string} exists")
    public void aTraineeWithUsernameExists(String username) {
        trainee = new TraineeEntity();
        trainee.setUser(new UserEntity());
        trainee.getUser().setUsername(username);

        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.of(trainee));
    }

    @When("trainee is deleted")
    public void traineeIsDeleted() {
        traineeService.deleteTraineeByUsername(trainee.getUser().getUsername());
    }

    @Then("the trainee should no longer exist in the repository")
    public void theTraineeShouldNoLongerExist() {
        verify(traineeRepository, times(1)).delete(trainee);
    }
}
