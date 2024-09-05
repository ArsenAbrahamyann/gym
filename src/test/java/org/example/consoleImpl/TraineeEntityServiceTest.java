package org.example.consoleImpl;

import org.example.console.TraineeConsoleImpl;
import org.example.console.UserConsoleImpl;
import org.example.entity.TraineeEntity;
import org.example.service.TraineeService;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TraineeEntityServiceTest {
    @Mock
    private TraineeService traineeService;
    @Mock
    private UserConsoleImpl userConsole;
    @Mock
    private UserService userService;
    @Mock
    private  Scanner scanner;
    @InjectMocks
    private TraineeConsoleImpl underTest;

    @BeforeEach
    public void setUp() {
        underTest = new TraineeConsoleImpl(traineeService,userConsole,userService);
        underTest.setScanner(scanner);
    }

    @Test
    public void shouldThrowExceptionWhenScannerIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> underTest.setScanner(null));
        assertEquals("Scanner cannot be null.", exception.getMessage());
    }

    @Test
    public void shouldCreateTraineeSuccessfully() {
        when(scanner.nextLine()).thenReturn("John", "Doe", "2000-01-01", "123 Main St");
        doNothing().when(traineeService).createTrainee(any(TraineeEntity.class));

        underTest.createTrainee();

        verify(traineeService).createTrainee(any(TraineeEntity.class));
    }

    @Test
    public void shouldHandleInvalidDateFormatWhenCreatingTrainee() {
        when(scanner.nextLine()).thenReturn("John", "Doe", "invalid-date", "123 Main St");

        underTest.createTrainee();

        verify(traineeService, never()).createTrainee(any(TraineeEntity.class));
    }

    @Test
    public void shouldUpdateTraineeSuccessfully() {
        when(scanner.nextLine()).thenReturn("JohnDoe", "2001-01-01", "456 Main St");
        TraineeEntity existingTraineeEntity = new TraineeEntity();
        existingTraineeEntity.setUserId("JohnDoe");
        when(traineeService.getTrainee("JohnDoe")).thenReturn(existingTraineeEntity);

        underTest.updateTrainee();

        verify(traineeService).updateTrainee(any(TraineeEntity.class));
    }

    @Test
    public void shouldHandleInvalidDateFormatWhenUpdatingTrainee() {
        when(scanner.nextLine()).thenReturn("JohnDoe", "invalid-date", "456 Main St");
        TraineeEntity existingTraineeEntity = new TraineeEntity();
        existingTraineeEntity.setUserId("JohnDoe");
        when(traineeService.getTrainee("JohnDoe")).thenReturn(existingTraineeEntity);

        underTest.updateTrainee();

        verify(traineeService, never()).updateTrainee(any(TraineeEntity.class));
    }

    @Test
    public void shouldDeleteTraineeSuccessfully() {
        when(scanner.nextLine()).thenReturn("JohnDoe");
        TraineeEntity existingTraineeEntity = new TraineeEntity();
        existingTraineeEntity.setUserId("JohnDoe");
        when(traineeService.getTrainee("JohnDoe")).thenReturn(existingTraineeEntity);

        underTest.deleteTrainee();

        verify(traineeService).deleteTrainee(anyString());
    }

    @Test
    public void shouldViewTraineeSuccessfully() {
        when(scanner.nextLine()).thenReturn("JohnDoe");
        TraineeEntity existingTraineeEntity = new TraineeEntity();
        existingTraineeEntity.setUserId("JohnDoe");
        existingTraineeEntity.setLocalDateTime("2000-01-01");
        existingTraineeEntity.setAddress("123 Main St");
        when(traineeService.getTrainee("JohnDoe")).thenReturn(existingTraineeEntity);

        underTest.viewTrainee();

        verify(traineeService).getTrainee(anyString());
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
    }

    @Test
    public void shouldPrintMenuSuccessfully() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        underTest.printMenu();

        String expectedOutput = "\nManage TraineeEntity " +
                                "\n1. Create TraineeEntity " +
                                "\n2. Update TraineeEntity " +
                                "\n3. Delete TraineeEntity " +
                                "\n4. View TraineeEntity " +
                                "\n5. View All Trainees " +
                                "\n6. Back to Main Menu" +
                                "\n Enter your choice: ";
        assertTrue(outputStream.toString().contains(expectedOutput));

        System.setOut(System.out); // Reset the standard output stream
    }
}
