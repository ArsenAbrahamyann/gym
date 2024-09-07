package org.example.consoleImpl;

import org.example.console.TraineeConsoleImpl;
import org.example.console.TrainerConsoleImpl;
import org.example.console.TrainingConsoleImpl;
import org.example.console.UserConsoleImpl;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.entity.dto.TrainingDto;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.example.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingConsoleImplTest {
    @InjectMocks
    private TrainingConsoleImpl trainingConsole;

    @Mock
    private TrainingService trainingService;

    @Mock
    private TraineeConsoleImpl traineeConsole;

    @Mock
    private TrainerConsoleImpl trainerConsole;

    @Mock
    private UserConsoleImpl userConsole;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

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
        Field scannerField = TrainingConsoleImpl.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(trainingConsole, scanner);
    }

    @Test
    public void testCreateTraining_ValidInput() throws Exception {
        // Mock user input
        when(scanner.nextLine()).thenReturn("traineeUser", "trainerUser", "TrainingName", "TrainingType", "2024-09-07", "01:00");

        // Mock validation and service calls
        when(validationUtils.validateTraineeExists(anyString())).thenReturn(null);
        when(validationUtils.validateTrainerExists(anyString())).thenReturn(null);
        when(validationUtils.validateBirthDate(anyString())).thenReturn(true);
        when(validationUtils.validateTrainingDuration(anyString())).thenReturn(null);

        TrainingDto trainingDto = new TrainingDto();
        when(modelMapper.map(any(TrainingDto.class), eq(TrainingEntity.class))).thenReturn(new TrainingEntity());

        trainingConsole.createTraining();

        verify(trainingService, times(1)).createTraining(any(TrainingEntity.class));
        verify(modelMapper, times(1)).map(any(TrainingDto.class), eq(TrainingEntity.class));
        verifyNoMoreInteractions(trainingService, modelMapper);
    }

    @Test
    public void testViewTraining_TrainingExists() {
        // Mock user input
        when(scanner.nextLine()).thenReturn("TrainingName");

        TrainingEntity trainingEntity = new TrainingEntity();
        when(trainingService.getTraining(anyString())).thenReturn(trainingEntity);
        when(modelMapper.map(any(TrainingEntity.class), eq(TrainingDto.class))).thenReturn(new TrainingDto());

        trainingConsole.viewTraining();

        verify(trainingService, times(1)).getTraining(anyString());
        verify(modelMapper, times(1)).map(any(TrainingEntity.class), eq(TrainingDto.class));
        verifyNoMoreInteractions(trainingService, modelMapper);
    }

    @Test
    public void testViewTraining_TrainingDoesNotExist() {
        // Mock user input
        when(scanner.nextLine()).thenReturn("NonExistingTraining");

        when(trainingService.getTraining(anyString())).thenReturn(null);

        trainingConsole.viewTraining();

        verify(trainingService, times(1)).getTraining(anyString());
        verifyNoMoreInteractions(trainingService);
    }

    @Test
    public void testViewAllTrainings() {
        List<TrainingEntity> trainingEntities = new ArrayList<>();
        trainingEntities.add(new TrainingEntity());

        when(trainingService.getAllTrainings()).thenReturn(trainingEntities);
        when(modelMapper.map(any(TrainingEntity.class), eq(TrainingDto.class))).thenReturn(new TrainingDto());

        trainingConsole.viewAllTrainings();

        verify(trainingService, times(1)).getAllTrainings();
        verify(modelMapper, times(1)).map(any(TrainingEntity.class), eq(TrainingDto.class));
        verifyNoMoreInteractions(trainingService, modelMapper);
    }

    @Test
    public void testPrintMenu() {
        trainingConsole.printMenu();

        // Verify log output if necessary
        // You might use an in-memory appender to capture logs if needed
    }
}
