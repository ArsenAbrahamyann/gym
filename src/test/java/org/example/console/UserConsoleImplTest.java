package org.example.console;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.List;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserConsoleImplTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserConsoleImpl userConsoleImpl;

    @Test
    public void testPrintAllUsername() {
        List<String> usernames = List.of("user1", "user2", "user3");
        Mockito.when(userService.getAllUsernames()).thenReturn(usernames);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        userConsoleImpl.printAllUsername();
        PrintStream originalOut = System.out;
        String output = outContent.toString().trim();
        assert (output.contains("user1"));
        assert (output.contains("user2"));
        assert (output.contains("user3"));
        verify(userService, times(1)).getAllUsernames();
        System.setOut(originalOut);
    }

    @Test
    public void testUserConsoleImplConstructor() {
        assertDoesNotThrow(() -> {
            Constructor<UserConsoleImpl> constructor = UserConsoleImpl.class.getDeclaredConstructor(UserService.class);
            constructor.setAccessible(true);
            constructor.newInstance(userService);
        });
    }

    @Test
    public void testPrintAllUsernameExceptionHandling() {
        Mockito.when(userService.getAllUsernames()).thenThrow(RuntimeException.class);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        assertThrows(RuntimeException.class, () -> userConsoleImpl.printAllUsername());

        System.setOut(originalOut);
    }
}
