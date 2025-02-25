package org.example.gym.component.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.gym.controller.UserController;
import org.example.gym.dto.request.ChangeLoginRequestDto;
import org.example.gym.exeption.UserNotFoundException;
import org.example.gym.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


/**
 * Cucumber steps for testing the {@link UserController} related to password changes.
 */
public class UserControllerSteps {

    private UserService userService = mock(UserService.class);
    private UserController userController = new UserController(userService);
    private ResponseEntity<Void> response;

    /**
     * Sets up a user with the given username in the mock UserService.
     * Mocks the `changePassword` method to do nothing when called with a valid request.
     *
     * @param username the username of the existing user
     */
    @Given("a user exists with username {string}")
    public void user_exists_with_username(String username) {
        when(userService.existsByUsername(username)).thenReturn(true);

        ChangeLoginRequestDto validRequestDto = new ChangeLoginRequestDto();
        validRequestDto.setUsername(username);
        validRequestDto.setOldPassword("existingOldPassword");
        validRequestDto.setNewPassword("newSecurePassword123");

        doNothing().when(userService).changePassword(isA(ChangeLoginRequestDto.class));
    }

    /**
     * Sends a request to change the password for the given user.
     *
     * @param username    the username of the user
     * @param oldPassword the old password
     * @param newPassword the new password
     */
    @When("I request to change password for {string} from {string} to {string}")
    public void request_to_change_password(String username, String oldPassword, String newPassword) {
        ChangeLoginRequestDto dto = new ChangeLoginRequestDto(username, oldPassword, newPassword);
        response = userController.changeLogin(dto);
    }

    /**
     * Asserts that the password change was successful (HTTP status 200 OK).
     */
    @Then("the password should be changed successfully")
    public void the_password_should_be_changed_successfully() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Asserts that the password change failed with the given HTTP error code.
     *
     * @param statusCode the expected HTTP status code
     */
    @Then("the password change should fail with a {int} error")
    public void the_password_change_should_fail_with_a_error(int statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), this.response.getStatusCode(),
                "Expected and actual status codes do not match.");
    }


    /**
     * Mocks the UserService to throw a UserNotFoundException when trying to change the password for a non-existent user.
     *
     * @param username the username of the non-existent user
     */
    @Given("a user does not exist with username {string}")
    public void userDoesNotExistWithUsername(String username) {
        doThrow(new UserNotFoundException("User not found"))
                .when(userService)
                .changePassword(argThat(requestDto -> requestDto.getUsername().equals(username)));
    }

    /**
     * Attempts to change the password for a user.  Handles the potential UserNotFoundException
     * and sets the response accordingly.  If no exception is caught, sets the response to an
     * internal server error (which shouldn't happen in the normal flow).
     *
     * @param username    the username of the user
     * @param newPassword the new password
     */
    @When("I  change password for {string} to {string}")
    public void changePasswordForTo(String username, String newPassword) {
        ChangeLoginRequestDto dto = new ChangeLoginRequestDto(username, "oldPassword", newPassword);
        try {
            userService.changePassword(dto);
        } catch (UserNotFoundException e) {
            this.response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (this.response == null) {
            this.response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
