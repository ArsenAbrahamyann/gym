package org.example.gym.componentTesting.steps;

import org.example.gym.controller.UserController;
import org.example.gym.dto.request.ChangeLoginRequestDto;
import org.example.gym.exeption.UserNotFoundException;
import org.example.gym.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
public class UserControllerSteps {

    private UserService userService = mock(UserService.class);
    private UserController userController = new UserController(userService);
    private ResponseEntity<Void> response;


    @Given("a user exists with username {string}")
    public void a_user_exists_with_username(String username) {
        when(userService.existsByUsername(username)).thenReturn(true);

        ChangeLoginRequestDto validRequestDto = new ChangeLoginRequestDto();
        validRequestDto.setUsername(username);
        validRequestDto.setOldPassword("existingOldPassword");
        validRequestDto.setNewPassword("newSecurePassword123");

        doNothing().when(userService).changePassword(isA(ChangeLoginRequestDto.class));
    }

    @When("I request to change password for {string} from {string} to {string}")
    public void i_request_to_change_password(String username, String oldPassword, String newPassword) {
        ChangeLoginRequestDto dto = new ChangeLoginRequestDto(username, oldPassword, newPassword);
        response = userController.changeLogin(dto);
    }

    @Then("the password should be changed successfully")
    public void the_password_should_be_changed_successfully() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Then("the password change should fail with a {int} error")
    public void the_password_change_should_fail_with_a_error(int statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), this.response.getStatusCode(),
                "Expected and actual status codes do not match.");
    }

    @Given("a user does not exist with username {string}")
    public void aUserDoesNotExistWithUsername(String username) {
        doThrow(new UserNotFoundException("User not found"))
                .when(userService)
                .changePassword(argThat(requestDto -> requestDto.getUsername().equals(username)));
    }

    @When("I  change password for {string} to {string}")
    public void iChangePasswordForTo(String username, String newPassword) {
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
