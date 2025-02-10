package org.example.gym.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.example.gym.dto.request.ChangeLoginRequestDto;
import org.example.gym.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    /**
     * Sets up the test environment by initializing the UserController
     * and mocking the UserService dependencies before each test.
     */
    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testChangeLogin_Success() {
        ChangeLoginRequestDto requestDto = new ChangeLoginRequestDto("testUser",
                "oldPass", "newPass");

        ResponseEntity<Void> response = userController.changeLogin(requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).changePassword(requestDto);
    }

    @Test
    public void testChangeLogin_Failure_DueToServiceException() {
        ChangeLoginRequestDto requestDto = new ChangeLoginRequestDto("testUser",
                "oldPass", "newPass");
        doThrow(RuntimeException.class).when(userService).changePassword(requestDto);

        assertThrows(RuntimeException.class, () -> userController.changeLogin(requestDto));
        verify(userService, times(1)).changePassword(requestDto);
    }

    @Test
    public void testChangeLogin_WithNullRequest() {
        assertThrows(NullPointerException.class, () -> userController.changeLogin(null));
        verify(userService, never()).changePassword(any());
    }

    @Test
    public void testChangeLogin_WithMalformedData() {
        ChangeLoginRequestDto requestDto = new ChangeLoginRequestDto("testUser",
                "oldPass", "123");
        doThrow(new IllegalArgumentException("Password is too weak")).when(userService).changePassword(requestDto);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> userController.changeLogin(requestDto));
        assertEquals("Password is too weak", exception.getMessage());
        verify(userService, times(1)).changePassword(requestDto);
    }
}
