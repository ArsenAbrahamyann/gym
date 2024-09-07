package org.example.consoleImpl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.List;
import org.example.console.UserConsoleImpl;
import org.example.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserConsoleImplTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserConsoleImpl userConsoleImpl;


    @Test
    public void testPrintAllUsername() {
        // Given
        List<String> usernames = List.of("user1", "user2", "user3");
        Mockito.when(userService.getAllUsernames()).thenReturn(usernames);

        // Capture the output
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // When
        userConsoleImpl.printAllUsername();

        // Then
        String output = outContent.toString().trim();
        assert(output.contains("user1"));
        assert(output.contains("user2"));
        assert(output.contains("user3"));

        // Verify the interaction with the userService
        verify(userService, times(1)).getAllUsernames();

        // Reset System.out
        System.setOut(originalOut);
    }

    @Test
    public void testUserConsoleImplConstructor() {
        assertDoesNotThrow(() -> {
            // Use reflection to create an instance of UserConsoleImpl
            Constructor<UserConsoleImpl> constructor = UserConsoleImpl.class.getDeclaredConstructor(UserService.class);
            constructor.setAccessible(true);
            constructor.newInstance(userService);
        });
    }

    @Test
    public void testPrintAllUsernameExceptionHandling() {
        // Given
        Mockito.when(userService.getAllUsernames()).thenThrow(RuntimeException.class);

        // Capture the output
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // When
        assertThrows(RuntimeException.class, () -> userConsoleImpl.printAllUsername());

        // Reset System.out
        System.setOut(originalOut);
    }
}
