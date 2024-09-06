package org.example.consoleImpl;

import org.example.console.TraineeConsoleImpl;
import org.example.console.UserConsoleImpl;
import org.example.entity.TraineeEntity;
import org.example.entity.UserEntity;
import org.example.entity.dto.UserDto;
import org.example.repository.TraineeDAO;
import org.example.service.TraineeService;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Optional;
import java.util.Scanner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TraineeConsoleImplTest {
    @Mock
    private TraineeService traineeService;
    @Mock
    private UserService userService;
    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private UserConsoleImpl userConsole;
    @Mock
    private Scanner scanner;

    @InjectMocks
    private TraineeConsoleImpl traineeConsoleImpl;

    @BeforeEach
    void setup() {
        traineeConsoleImpl.setScanner(scanner);
    }

    @Test
    public void testUpdateTrainee_Success() {
        when(scanner.nextLine()).thenReturn("john_doe")
                .thenReturn("John")
                .thenReturn("Doe")
                .thenReturn("1990-01-01")
                .thenReturn("New Address");

        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setUserId("user1");
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("Jane");
        userEntity.setLastName("Doe");
        userEntity.setUserName("jane_doe");

        when(traineeService.getTrainee("john_doe")).thenReturn(traineeEntity);
        when(userConsole.getUser("user1")).thenReturn(Optional.of(userEntity));

        doNothing().when(userService).updateUser(any(UserEntity.class));
        doNothing().when(traineeService).updateTrainee(any(TraineeEntity.class));

        traineeConsoleImpl.updateTrainee();

        verify(userService).updateUser(any(UserEntity.class));
        verify(traineeService).updateTrainee(any(TraineeEntity.class));
    }

    @Test
    public void testUpdateTrainee_InvalidDateFormat() {
        String traineeDateOfBirth = "test-12-03";
        UserDto user = new UserDto();
        Mockito.when(userConsole.createUser()).thenReturn(user);
        when(scanner.nextLine()).thenReturn(traineeDateOfBirth);

        traineeConsoleImpl.createTrainee();

        Mockito.verify(traineeService, Mockito.never()).createTrainee(any(TraineeEntity.class));
    }

    @Test
    public void testUpdateTrainee_TraineeNotFound() {
        String username = "testUser";

        when(traineeService.getTrainee(anyString())).thenReturn(null);

        traineeConsoleImpl.setScanner(new Scanner(new ByteArrayInputStream(
                (username
                        + "\n").getBytes())));

        traineeConsoleImpl.updateTrainee();

        verify(userService, times(0)).updateUser(any(UserEntity.class));
        verify(traineeService, times(0)).updateTrainee(any(TraineeEntity.class));
    }

    @Test
    public void testUpdateTrainee_UserNotFound() {
        String username = "john_doe";
        String newFirstName = "John";
        String newLastName = "Doe";
        String dateOfBirth = "1990-01-01";
        String address = "New Address";
        String input = username
                + "\n"
                + newFirstName
                + "\n"
                + newLastName
                + "\n"
                + dateOfBirth
                + "\n"
                + address
                + "\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inputStream);
        traineeConsoleImpl.setScanner(scanner);
        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setUserId("user1");
        when(traineeService.getTrainee(username)).thenReturn(traineeEntity);
        when(userConsole.getUser("user1")).thenReturn(Optional.empty());
        traineeConsoleImpl.updateTrainee();
        verify(userService, never()).updateUser(any(UserEntity.class));
        verify(traineeService, never()).updateTrainee(any(TraineeEntity.class));
    }

    @Test
    public void testCreateTrainee_NoInteractionsWithService() {
        TraineeEntity trainee = new TraineeEntity();
        String traineeDateOfBirth = "2007-12-03";
        UserDto userDto = new UserDto();
        Mockito.when(userConsole.createUser()).thenReturn(new UserDto());
        when(scanner.nextLine()).thenReturn(traineeDateOfBirth);
        traineeConsoleImpl.createTrainee();
        Mockito.verify(traineeService, Mockito.times(1)).createTrainee(any(TraineeEntity.class));
    }

    @Test
    public void testCreateTrainee_InteractionsWithService() {
        String traineeDateOfBirth = "2007-12-03";
        ModelMapper modelMapperSpy = spy(ModelMapper.class);
        UserDto user = new UserDto();
        Mockito.when(userConsole.createUser()).thenReturn(user);
        when(scanner.nextLine()).thenReturn(traineeDateOfBirth);
        Mockito.doNothing().when(traineeService).createTrainee(any(TraineeEntity.class));
        traineeConsoleImpl.createTrainee();
        Mockito.verify(traineeService, Mockito.times(1)).createTrainee(Mockito.any(TraineeEntity.class));
    }

    @Test
    public void testCreateTrainee_InvalidDateFormat() {
        String traineeDateOfBirth = "test-12-03";
        UserDto user = new UserDto();
        Mockito.when(userConsole.createUser()).thenReturn(user);
        when(scanner.nextLine()).thenReturn(traineeDateOfBirth);
        traineeConsoleImpl.createTrainee();
        Mockito.verify(traineeService, Mockito.never()).createTrainee(any(TraineeEntity.class));
    }

    @Test
    void testCreateTrainee_Exception() {
        when(userConsole.createUser()).thenThrow(new RuntimeException("User creation failed"));
        traineeConsoleImpl.createTrainee();
        verify(userService, never()).saveUser(any());
        verify(traineeService, never()).createTrainee(any());
    }

    @Test
    void testDeleteTraineeSuccess() {
        String username = "testUser";
        when(scanner.nextLine()).thenReturn(username);
        traineeConsoleImpl.deleteTrainee();
        verify(userService, times(1)).deleteUserByUsername(username);
        verify(traineeService, times(1)).deleteTrainee(username);
    }

    @Test
    void testDeleteTraineeError() {
        String username = "testUser";
        when(scanner.nextLine()).thenReturn(username);
        doThrow(new RuntimeException("Database error")).when(userService).deleteUserByUsername(anyString());
        traineeConsoleImpl.deleteTrainee();
        verify(userService, times(1)).deleteUserByUsername(username);
        verify(traineeService, never()).deleteTrainee(anyString()); // No call due to exception
    }

    @Test
    public void viewAllTrainee_WhenNoTraineesExist_ShouldDisplayNoTraineesMessage() {
        when(traineeService.getAllTrainees()).thenReturn(Collections.emptyList());
        traineeConsoleImpl.viewAllTrainee();
        verify(traineeService, times(1)).getAllTrainees();
    }

    @Test
    public void viewAllTrainee_WhenExceptionOccurs_ShouldHandleException() {
        when(traineeService.getAllTrainees()).thenThrow(new RuntimeException("Test Exception"));
        traineeConsoleImpl.viewAllTrainee();
        verify(traineeService, times(1)).getAllTrainees();
    }

    @Test
    public void testViewTrainee_TraineeNotFound() {
        String username = "nonExistentUser";
        when(traineeService.getTrainee(username)).thenReturn(null);
        InputStream in = new ByteArrayInputStream((username + "\n").getBytes());
        System.setIn(in);
        traineeConsoleImpl.viewTrainee();
    }

    @Test
    public void testViewTrainee_ExceptionHandling() {
        String username = "testUser";

        when(traineeService.getTrainee(username)).thenThrow(new RuntimeException("Database error"));

        InputStream in = new ByteArrayInputStream((username + "\n").getBytes());
        System.setIn(in);

        traineeConsoleImpl.viewTrainee();

    }

    @Test
    public void testPrintMenu() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));


        traineeConsoleImpl.printMenu();

        String expectedOutput = "\nManage Trainee "
                + "\n1. Create Trainee "
                + "\n2. Update Trainee "
                + "\n3. Delete Trainee "
                + "\n4. View Trainee "
                + "\n5. View All Trainees "
                + "\n6. Back to Main Menu"
                + "\nEnter your choice: ";

        String actualOutput = outContent.toString().trim();

    }

}
