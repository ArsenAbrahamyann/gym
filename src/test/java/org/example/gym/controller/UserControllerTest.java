//package org.example.gym.controller;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import org.example.gym.dto.request.ChangeLoginRequestDto;
//import org.example.gym.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//@ExtendWith(MockitoExtension.class)
//public class UserControllerTest {
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;
//
//    /**
//     * Sets up the test environment by initializing the UserController
//     * and mocking the UserService dependencies before each test.
//     */
//    @BeforeEach
//    public void setUp() {
//    }
//
//    @Test
//    public void testLogin_Success() {
//        // Arrange
//        String username = "testUser";
//        String password = "testPass";
//        when(userService.authenticateUser(username, password)).thenReturn(true);
//
//        // Act
//        ResponseEntity<Void> response = userController.login(username, password);
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(userService, times(1)).authenticateUser(username, password);
//    }
//
//
//    @Test
//    public void testLogin_Failure() {
//        // Arrange
//        String username = "testUser";
//        String password = "wrongPass";
//        when(userService.authenticateUser(username, password)).thenReturn(false);
//
//        // Act
//        ResponseEntity<Void> response = userController.login(username, password);
//
//        // Assert
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        verify(userService, times(1)).authenticateUser(username, password);
//    }
//
//
//    @Test
//    public void testChangeLogin_Success() {
//        // Arrange
//        ChangeLoginRequestDto requestDto = new ChangeLoginRequestDto();
//        requestDto.setUsername("testUser");
//        requestDto.setNewPassword("newPass");
//
//        // Act
//        ResponseEntity<Void> response = userController.changeLogin(requestDto);
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(userService, times(1)).changePassword(requestDto);
//    }
//}
