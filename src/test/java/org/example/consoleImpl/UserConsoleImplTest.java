package org.example.consoleImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.example.console.UserConsoleImpl;
import org.example.entity.UserEntity;
import org.example.entity.UserUtils;
import org.example.entity.dto.UserDto;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;



@ExtendWith(MockitoExtension.class)
public class UserConsoleImplTest {
    @Mock
    private UserService userService;

    @Mock
    private UserUtils userUtils;
    @Mock
    private Scanner scanner;


    @InjectMocks
    private UserConsoleImpl userConsole;


    @BeforeEach
    void setUp() {
        userConsole.setScanner(scanner);

    }


    @Test
    void testCreateUserException() {
        // Arrange
        when(scanner.nextLine()).thenThrow(new RuntimeException("Scanner error"));

        // Act
        UserDto result = userConsole.createUser();

        // Assert
        assertNotNull(result);
        // Check that the default values are used
        assertNull(result.getFirstName());
        assertNull(result.getLastName());
        assertNull(result.getUserName());
        assertNull(result.getPassword());
        assertFalse(result.isActive());

        // Verify that the error message was printed
        // You can use a custom stream to capture System.out prints or verify log messages
    }
}
