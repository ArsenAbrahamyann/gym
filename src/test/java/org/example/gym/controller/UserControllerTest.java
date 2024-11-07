package org.example.gym.controller;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.gym.dto.request.ChangeLoginRequestDto;
import org.example.gym.dto.response.JwtResponse;
import org.example.gym.security.JWTTokenProvider;
import org.example.gym.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private JWTTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserController userController;

    /**
     * Sets up the test environment by clearing the SecurityContext before each test.
     * This ensures that there is no residual authentication data from previous tests,
     * which could interfere with the current test's execution and results.
     */
    @BeforeEach
    public void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testLogin_Success() {
        // Arrange
        String username = "testUser";
        String password = "testPass";
        String token = "jwtToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn(token);

        // Act
        ResponseEntity<JwtResponse> response = userController.login(username, password);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(token, response.getBody().getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, times(1)).generateToken(authentication);
    }

    @Test
    public void testChangeLogin_Success() {
        // Arrange
        ChangeLoginRequestDto requestDto = new ChangeLoginRequestDto();
        requestDto.setUsername("testUser");
        requestDto.setNewPassword("newPass");

        // Act
        ResponseEntity<Void> response = userController.changeLogin(requestDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).changePassword(requestDto);
    }

}
