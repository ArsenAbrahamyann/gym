package org.example.gym.componentTesting.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Optional;
import org.example.gym.dto.request.ChangeLoginRequestDto;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.UnauthorizedException;
import org.example.gym.exeption.UserNotFoundException;
import org.example.gym.repository.UserRepository;
import org.example.gym.service.MetricsService;
import org.example.gym.service.UserService;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceStepDefs {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private MetricsService metricsService;

    private UserService userService;

    private UserEntity user;
    private boolean authenticationResult;
    private String passwordChangeResult;
    private boolean userExists;

    public UserServiceStepDefs() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.passwordEncoder = Mockito.mock(PasswordEncoder.class);
        this.userService = Mockito.mock(UserService.class);
        this.metricsService = Mockito.mock(MetricsService.class);

        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenAnswer(invocation -> {
            String input = invocation.getArgument(0);
            return "encoded-" + input;
        });

        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenAnswer(invocation -> {
            String rawPassword = invocation.getArgument(0);
            String storedEncodedPassword = invocation.getArgument(1);
            return storedEncodedPassword.equals("encoded-" + rawPassword);
        });
    }

    @Given("the user credentials are valid")
    public void the_user_credentials_are_valid() {
        given(userService.authenticateUser("validUsername", "validPassword")).willReturn(true);
    }


    @Given("the user credentials are invalid")
    public void the_user_credentials_are_invalid() {
        given(userService.authenticateUser("invalidUsername", "invalidPassword"))
                .willThrow(new UnauthorizedException("the user credentials are invalid"));
    }

    @Given("no user exists with username {string}")
    public void no_user_exists_with_username(String username) {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
    }

    @When("I check if the user exists with username {string}")
    public void i_check_if_the_user_exists_with_username(String username) {
        userExists = userService.existsByUsername(username);
    }

    @Then("the authentication should fail")
    public void the_authentication_should_fail() {
        assertFalse("Authentication succeeded unexpectedly", authenticationResult);
    }

    @Given("a registered user with username {string} and password {string}")
    public void a_registered_user_with_username_and_password(String username, String password) {
        user = new UserEntity();
        user.setUsername(username);

        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        UserEntity savedUser = userRepository.findByUsername(username).get();

        System.out.println("User saved with password: " + savedUser.getPassword());
    }

    @When("the user tries to log in with username {string} and password {string}")
    public void the_user_tries_to_log_in_with_username_and_password(String username, String password) {
        authenticationResult = userService.authenticateUser(username, password);
        System.out.println("Authentication result: " + authenticationResult);
    }



    @When("the user changes the password to {string}")
    public void the_user_changes_the_password_to(String newPassword) {
        if (user == null) {
            throw new IllegalStateException("User object is not initialized.");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);

        Mockito.doAnswer(invocation -> {
            ChangeLoginRequestDto req = invocation.getArgument(0);
            user.setPassword(passwordEncoder.encode(req.getNewPassword()));
            return null;
        }).when(userService).changePassword(Mockito.any(ChangeLoginRequestDto.class));

        ChangeLoginRequestDto request = new ChangeLoginRequestDto(user.getUsername(), user.getPassword(), newPassword);
        userService.changePassword(request);

        userRepository.save(user);

        UserEntity updatedUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        System.out.println("Updated password for user: " + (updatedUser != null ? updatedUser.getPassword() :
                "User not found"));

        passwordChangeResult = newPassword;
        System.out.println("Changed password for user: " + passwordChangeResult);
    }

    @Then("the password should be updated successfully")
    public void the_password_should_be_updated_successfully() {
        System.out.println("Actual encoded password in user entity: " + user.getPassword());

        assertTrue(passwordEncoder.matches(passwordChangeResult, user.getPassword()));
    }

    @When("the user tries to change the password for {string}")
    public void the_user_tries_to_change_the_password_for(String username) {
        try {
            ChangeLoginRequestDto changeLoginRequestDto = new ChangeLoginRequestDto(
                    username, "password123", "newPassword123"
            );
            userService.changePassword(changeLoginRequestDto);
        } catch (UserNotFoundException e) {
            authenticationResult = false;
        }
    }

    @Then("a {string} exception should be thrown")
    public void a_exception_should_be_thrown(String exceptionMessage) {
        Mockito.doThrow(new UserNotFoundException("User not found"))
                .when(userService)
                .changePassword(Mockito.argThat(req ->
                        "nonexistinguser".equals(req.getUsername())));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.changePassword(new ChangeLoginRequestDto("nonexistinguser", "oldpassword",
                    "newpassword"));
        });

        System.out.println("Caught exception: " + exception.getMessage());
        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Then("the result should be true")
    public void the_result_should_be_true() {
        assertTrue(userExists);
    }

    @Given("a registered user with username {string}")
    public void a_registered_user_with_username(String username) {
        given(userService.existsByUsername(username)).willReturn(true);
    }

    @Then("the result should be false")
    public void the_result_should_be_false() {
        assertFalse(userExists);
    }

}
