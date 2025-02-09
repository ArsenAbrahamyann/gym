package org.example.gym.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.SecureRandom;
import org.example.gym.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

@ExtendWith(MockitoExtension.class)

public class UserUtilsTest {

    @Mock
    private UserService userService;

    @Mock
    private SecureRandom random;

    @InjectMocks
    private UserUtils userUtils;

    @Value("${user.password.characters}")
    private String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private Integer passwordLength = 10;

    @BeforeEach
    void setUp() {
        userUtils = new UserUtils(userService, random);
    }

    @Test
    public void generateUsername_WithNoExistingUsername_ShouldReturnBaseUsername() {
        String firstName = "John";
        String lastName = "Doe";
        String baseUsername = "John.Doe";

        when(userService.countByUsernameStartingWith(baseUsername)).thenReturn(0);

        String result = userUtils.generateUsername(firstName, lastName);

        assertEquals(baseUsername, result);
        verify(userService, times(1)).countByUsernameStartingWith(baseUsername);
    }

    @Test
    public void generateUsername_WithExistingBaseUsername_ShouldReturnModifiedUsername() {
        String firstName = "John";
        String lastName = "Doe";
        String expectedUsername = "John.Doe1";

        when(userService.countByUsernameStartingWith("John.Doe")).thenReturn(1);

        String result = userUtils.generateUsername(firstName, lastName);

        assertEquals(expectedUsername, result);
        verify(userService, times(1)).countByUsernameStartingWith("John.Doe");
    }

}
