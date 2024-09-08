package org.example.consoleImpl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.example.console.TraineeConsoleImpl;
import org.example.console.UserConsoleImpl;
import org.example.entity.TraineeEntity;
import org.example.entity.dto.TraineeDto;
import org.example.service.TraineeService;
import org.example.service.UserService;
import org.example.utils.UserUtils;
import org.example.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TraineeConsoleImplTest {
    @InjectMocks
    private TraineeConsoleImpl traineeConsole;

    @Mock
    private TraineeService traineeService;

    @Mock
    private UserService userService;
    @Mock
    private UserConsoleImpl userConsole;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private Scanner scanner;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        scanner = mock(Scanner.class);
        Field scannerField = TraineeConsoleImpl.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(traineeConsole, scanner);
    }

    @Test
    void testCreateTrainee_ValidInput() {
        when(scanner.nextLine()).thenReturn("John", "Doe", "true", "2000-01-01", "123 Main St");

        List<String> existingUsernames = new ArrayList<>();
        existingUsernames.add("existingUser1");
        existingUsernames.add("existingUser2");

        when(userService.getAllUsernames()).thenReturn(existingUsernames);

        try (MockedStatic<UserUtils> mockedUserUtils = mockStatic(UserUtils.class)) {
            mockedUserUtils.when(() -> UserUtils.generateUsername("John", "Doe", existingUsernames))
                    .thenReturn("john.doe");
            mockedUserUtils.when(UserUtils::generatePassword)
                    .thenReturn("password123");

            TraineeDto mockTraineeDto = new TraineeDto();
            mockTraineeDto.setUserId("john.doe");
            mockTraineeDto.setDateOfBirth("2000-01-01");
            mockTraineeDto.setAddress("123 Main St");
            mockTraineeDto.setUserName("john.doe");
            mockTraineeDto.setFirstName("John");
            mockTraineeDto.setLastName("Doe");
            mockTraineeDto.setPassword("password123");
            mockTraineeDto.setIsActive("true");

            TraineeEntity mockTraineeEntity = new TraineeEntity();
            when(modelMapper.map(any(TraineeDto.class), eq(TraineeEntity.class))).thenReturn(mockTraineeEntity);

            traineeConsole.createTrainee();

            verify(userService).getAllUsernames();
            verify(validationUtils).isValidBoolean("true");
            verify(validationUtils).validateBirthDate("2000-01-01");
            verify(modelMapper).map(any(TraineeDto.class), eq(TraineeEntity.class));
            verify(traineeService).createTrainee(mockTraineeEntity);

            System.out.println("Test for createTrainee() passed.");
        }
    }

    @Test
    void testUpdateTrainee() {
        String updateUsername = "trainee1";
        TraineeEntity existingTrainee = new TraineeEntity();
        existingTrainee.setUserId(updateUsername);
        existingTrainee.setFirstName("John");
        existingTrainee.setLastName("Doe");
        existingTrainee.setDateOfBirth("2000-01-01");
        existingTrainee.setAddress("123 Main St");

        TraineeEntity traineeDto = new TraineeEntity();
        TraineeEntity updatedTrainee = new TraineeEntity();
        updatedTrainee.setUserId("jane.smith");

        List<String> allUsernames = Arrays.asList("trainee1", "trainee2");

        when(scanner.nextLine()).thenReturn(updateUsername, "Jane", "Smith", "1999-12-31", "456 Elm St");
        when(traineeService.getTrainee(updateUsername)).thenReturn(existingTrainee);
        when(userService.getAllUsernames()).thenReturn(allUsernames);
        when(modelMapper.map(existingTrainee, TraineeEntity.class)).thenReturn(traineeDto);
        when(modelMapper.map(traineeDto, TraineeEntity.class)).thenReturn(updatedTrainee);

        doNothing().when(traineeService).deleteTrainee(updateUsername);
        doNothing().when(traineeService).updateTrainee(updatedTrainee);
        doNothing().when(userConsole).printAllUsername();

        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        traineeConsole.updateTrainee();

        verify(traineeService).getTrainee(updateUsername);
        verify(userService).getAllUsernames();
        verify(validationUtils).validateBirthDate("1999-12-31");
        verify(traineeService).deleteTrainee(updateUsername);
        verify(traineeService).updateTrainee(updatedTrainee);
        verify(userConsole).printAllUsername();

        String output = outputStream.toString().trim();
        assertTrue(output.contains("Enter new firstName:"));
        assertTrue(output.contains("Enter new lastName:"));
        assertTrue(output.contains("Enter new date of birth (YYYY-MM-DD):"));
        assertTrue(output.contains("Enter new address:"));

        System.setOut(originalOut);
    }

    @Test
    public void testDeleteTrainee() {
        when(scanner.nextLine()).thenReturn("userToDelete");

        traineeConsole.deleteTrainee();

        verify(traineeService, times(1)).deleteTrainee(anyString());
        verifyNoMoreInteractions(traineeService);
    }

    @Test
    void testViewTraineeWhenTraineeIsFound() {
        // Arrange
        String username = "trainee1";
        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setUserId(username);
        traineeEntity.setDateOfBirth("2000-01-01");
        traineeEntity.setAddress("123 Main St");

        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setUserId(username);
        traineeDto.setDateOfBirth("2000-01-01");
        traineeDto.setAddress("123 Main St");

        when(scanner.nextLine()).thenReturn(username);
        when(traineeService.getTrainee(username)).thenReturn(traineeEntity);
        when(modelMapper.map(traineeEntity, TraineeDto.class)).thenReturn(traineeDto);
        doNothing().when(userConsole).printAllUsername();

        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        traineeConsole.viewTrainee();

        String output = outputStream.toString().trim();
        assertTrue(output.contains("Username: trainee1"));
        assertTrue(output.contains("Date of Birth: 2000-01-01"));
        assertTrue(output.contains("Address: 123 Main St"));

        System.setOut(originalOut);

        verify(traineeService).getTrainee(username);
        verify(userConsole).printAllUsername();
        verify(modelMapper).map(traineeEntity, TraineeDto.class);
    }

    @Test
    void testViewTraineeWhenTraineeIsNotFound() {
        String username = "trainee2";

        when(scanner.nextLine()).thenReturn(username);
        when(traineeService.getTrainee(username)).thenReturn(null);
        doNothing().when(userConsole).printAllUsername();

        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        traineeConsole.viewTrainee();

        String output = outputStream.toString().trim();
        assertTrue(output.contains("Trainee not found."));

        System.setOut(originalOut);

        verify(traineeService).getTrainee(username);
        verify(userConsole).printAllUsername();
    }

    @Test
    public void testViewTrainee_TraineeDoesNotExist() {
        when(scanner.nextLine()).thenReturn("nonExistentUser");

        when(traineeService.getTrainee(anyString())).thenReturn(null);

        traineeConsole.viewTrainee();

        verify(traineeService, times(1)).getTrainee(anyString());
        verifyNoMoreInteractions(traineeService);
    }

    @Test
    public void testViewAllTrainees() {
        List<TraineeEntity> traineeEntities = new ArrayList<>();
        traineeEntities.add(new TraineeEntity());

        when(traineeService.getAllTrainees()).thenReturn(traineeEntities);
        when(modelMapper.map(any(TraineeEntity.class), eq(TraineeDto.class))).thenReturn(new TraineeDto());

        traineeConsole.viewAllTrainee();

        verify(traineeService, times(1)).getAllTrainees();
        verify(modelMapper, times(1)).map(any(TraineeEntity.class), eq(TraineeDto.class));
        verifyNoMoreInteractions(traineeService, modelMapper);
    }

    @Test
    public void testPrintMenu() {
        traineeConsole.printMenu();
    }
}
