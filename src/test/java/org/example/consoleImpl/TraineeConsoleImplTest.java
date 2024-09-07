package org.example.consoleImpl;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.example.console.TraineeConsoleImpl;
import org.example.console.TrainingConsoleImpl;
import org.example.console.UserConsoleImpl;
import org.example.entity.TraineeEntity;
import org.example.entity.dto.TraineeDto;
import org.example.service.TraineeService;
import org.example.service.UserService;
import org.example.utils.UserUtils;
import org.example.utils.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
public class TraineeConsoleImplTest {
    @InjectMocks
    private TraineeConsoleImpl traineeConsole;

    @Mock
    private TraineeService traineeService;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private Scanner scanner;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        // Use reflection to set a mock Scanner
        scanner = mock(Scanner.class);
        Field scannerField = TraineeConsoleImpl.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(traineeConsole, scanner);
    }

    @Test
    public void testCreateTrainee_ValidInput() throws Exception {
        // Mock user input
        when(scanner.nextLine()).thenReturn("John", "Doe", "true", "1990-01-01", "123 Main St");

        // Mock service calls
        List<String> existingUsernames = new ArrayList<>();
        when(userService.getAllUsernames()).thenReturn(existingUsernames);
        when(UserUtils.generateUsername(anyString(), anyString(), anyList())).thenReturn("john.doe");
        when(UserUtils.generatePassword()).thenReturn("password123");
        when(validationUtils.isValidBoolean(anyString())).thenReturn(true);
        when(validationUtils.validateBirthDate(anyString())).thenReturn(true);

        TraineeDto traineeDto = new TraineeDto();
        when(modelMapper.map(any(TraineeDto.class), eq(TraineeEntity.class))).thenReturn(new TraineeEntity());

        traineeConsole.createTrainee();

        verify(traineeService, times(1)).createTrainee(any(TraineeEntity.class));
        verify(modelMapper, times(1)).map(any(TraineeDto.class), eq(TraineeEntity.class));
        verifyNoMoreInteractions(traineeService, modelMapper);
    }

    @Test
    public void testUpdateTrainee_ValidInput() throws Exception {
        // Mock user input
        when(scanner.nextLine()).thenReturn("existingUser", "Jane", "Doe", "1995-05-05", "456 Elm St");

        // Mock service calls
        TraineeEntity existingTrainee = new TraineeEntity();
        when(traineeService.getTrainee(anyString())).thenReturn(existingTrainee);
        when(userService.getAllUsernames()).thenReturn(new ArrayList<>());
        when(UserUtils.generateUsername(anyString(), anyString(), anyList())).thenReturn("jane.doe");

        when(modelMapper.map(any(TraineeEntity.class), eq(TraineeEntity.class))).thenReturn(new TraineeEntity());

        traineeConsole.updateTrainee();

        verify(traineeService, times(1)).getTrainee(anyString());
        verify(traineeService, times(1)).deleteTrainee(anyString());
        verify(traineeService, times(1)).updateTrainee(any(TraineeEntity.class));
        verify(modelMapper, times(2)).map(any(), eq(TraineeEntity.class));
        verifyNoMoreInteractions(traineeService, modelMapper);
    }

    @Test
    public void testDeleteTrainee() throws Exception {
        // Mock user input
        when(scanner.nextLine()).thenReturn("userToDelete");

        traineeConsole.deleteTrainee();

        verify(traineeService, times(1)).deleteTrainee(anyString());
        verifyNoMoreInteractions(traineeService);
    }

    @Test
    public void testViewTrainee_TraineeExists() throws Exception {
        // Mock user input
        when(scanner.nextLine()).thenReturn("existingUser");

        TraineeEntity traineeEntity = new TraineeEntity();
        when(traineeService.getTrainee(anyString())).thenReturn(traineeEntity);
        when(modelMapper.map(any(TraineeEntity.class), eq(TraineeDto.class))).thenReturn(new TraineeDto());

        traineeConsole.viewTrainee();

        verify(traineeService, times(1)).getTrainee(anyString());
        verify(modelMapper, times(1)).map(any(TraineeEntity.class), eq(TraineeDto.class));
        verifyNoMoreInteractions(traineeService, modelMapper);
    }

    @Test
    public void testViewTrainee_TraineeDoesNotExist() throws Exception {
        // Mock user input
        when(scanner.nextLine()).thenReturn("nonExistentUser");

        when(traineeService.getTrainee(anyString())).thenReturn(null);

        traineeConsole.viewTrainee();

        verify(traineeService, times(1)).getTrainee(anyString());
        verifyNoMoreInteractions(traineeService);
    }

    @Test
    public void testViewAllTrainees() throws Exception {
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

        // Verify log output if necessary
        // You might use an in-memory appender to capture logs if needed
    }
}
