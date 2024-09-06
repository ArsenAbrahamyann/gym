package org.example.consoleImpl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import org.example.console.TraineeConsoleImpl;
import org.example.console.UserConsoleImpl;
import org.example.entity.TraineeEntity;
import org.example.entity.UserEntity;
import org.example.entity.dto.UserDto;
import org.example.service.TraineeService;
import org.example.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TraineeConsoleImplTest {
    @Mock
    private TraineeService traineeService;
    @Mock
    private UserConsoleImpl userConsole;
    @Mock
    private UserService userService;
    @Mock
    private Scanner scanner;
    @InjectMocks
    private TraineeConsoleImpl underTest;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final ByteArrayInputStream inputStream = new ByteArrayInputStream("".getBytes());

    private final InputStream originalIn = System.in;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private void simulateUserInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    public void shouldThrowExceptionWhenScannerIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> underTest.setScanner(null));
        assertEquals("Scanner cannot be null.", exception.getMessage());
    }

    @Test
    public void shouldCreateTraineeSuccessfully() throws Exception {
        // Mock required dependencies and user input
        UserDto mockUserDto = new UserDto(); // Create an empty UserDto
        mockUserDto.setUserName("JohnDoe");  // Set username before simulating input

        when(userConsole.createUser()).thenReturn(mockUserDto);

        // Simulate user input for date and address (if needed)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        simulateUserInput("2000-01-01\n123 Main St\n");

        underTest.createTrainee();  // Call the method under test

        // Improved assertions: Verify specific method calls and output
        verify(traineeService).createTrainee(argThat(trainee -> trainee.getUserId().equals(mockUserDto.getUserName()) &&
                                                                trainee.getLocalDateTime().toString().equals("2000-01-01") &&
                                                                trainee.getAddress().equals("123 Main St")));
        assertTrue(outputStream.toString().contains("Trainee created successfully."));
    }

    @Test
    public void shouldHandleInvalidDateFormatWhenCreatingTrainee() {
        when(userConsole.createUser()).thenReturn(new UserDto());
        simulateUserInput("invalid-date\n123 Main St\n");

        underTest.createTrainee();

        verify(traineeService, never()).createTrainee(any(TraineeEntity.class));
        assertTrue(outputStream.toString().contains("Invalid date format."));
    }

    @Test
    public void shouldUpdateTraineeSuccessfully() {
        TraineeEntity mockTrainee = new TraineeEntity();
        mockTrainee.setUserId("JohnDoe");
        when(traineeService.getTrainee("JohnDoe")).thenReturn(mockTrainee);
        when(userConsole.getUser("JohnDoe")).thenReturn(Optional.of(new UserEntity()));

        simulateUserInput("JohnDoe\nJohn\nDoe\n2001-01-01\n456 Main St\n");

        underTest.updateTrainee();

        // Improved assertion: Verify specific method call arguments
        verify(traineeService).updateTrainee(argThat(trainee -> trainee.getUserId().equals("JohnDoe") &&
                                                                trainee.getLocalDateTime().toString().equals("2001-01-01") &&
                                                                trainee.getAddress().equals("456 Main St")));
        assertTrue(outputStream.toString().contains("Trainee updated successfully."));
    }

    @Test
    public void shouldHandleInvalidDateFormatWhenUpdatingTrainee() {
        TraineeEntity mockTrainee = new TraineeEntity();
        mockTrainee.setUserId("JohnDoe");
        when(traineeService.getTrainee("JohnDoe")).thenReturn(mockTrainee);
        when(userConsole.getUser("JohnDoe")).thenReturn(Optional.of(new UserEntity()));

        simulateUserInput("JohnDoe\nJohn\nDoe\ninvalid-date\n456 Main St\n");

        underTest.updateTrainee();

        verify(traineeService, never()).updateTrainee(any(TraineeEntity.class));
        assertTrue(outputStream.toString().contains("Invalid date format."));
    }

    @Test
    public void shouldDeleteTraineeSuccessfully() {
        simulateUserInput("JohnDoe\n");

        underTest.deleteTrainee();

        verify(traineeService).deleteTrainee("JohnDoe");
        verify(userService).deleteUserByUsername("JohnDoe");
        assertTrue(outputStream.toString().contains("Trainee deleted."));
    }

    @Test
    public void shouldViewTraineeSuccessfully() {
        TraineeEntity mockTrainee = new TraineeEntity();
        mockTrainee.setUserId("JohnDoe");
        mockTrainee.setLocalDateTime("2000-01-01");
        mockTrainee.setAddress("123 Main St");
        when(traineeService.getTrainee("JohnDoe")).thenReturn(mockTrainee);

        simulateUserInput("JohnDoe\n");

        underTest.viewTrainee();

        verify(traineeService).getTrainee("JohnDoe");
        assertTrue(outputStream.toString().contains("Trainee Details:"));
        assertTrue(outputStream.toString().contains("JohnDoe"));
    }

    @Test
    public void shouldViewAllTraineesSuccessfully() {
        List<TraineeEntity> traineeEntities = new ArrayList<>();
        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setUserId("JohnDoe");
        traineeEntities.add(traineeEntity);
        when(traineeService.getAllTrainees()).thenReturn(traineeEntities);

        underTest.viewAllTrainee();

        verify(traineeService).getAllTrainees();
        assertTrue(outputStream.toString().contains("JohnDoe"));
    }

    @Test
    public void shouldPrintMenuSuccessfully() {
        underTest.printMenu();
        String expectedOutput = "\nManage Trainee " +
                                "\n1. Create Trainee " +
                                "\n2. Update Trainee " +
                                "\n3. Delete Trainee " +
                                "\n4. View Trainee " +
                                "\n5. View All Trainees " +
                                "\n6. Back to Main Menu" +
                                "\nEnter your choice: ";
        assertTrue(outputStream.toString().contains(expectedOutput));
    }

}
