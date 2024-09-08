package org.example.consoleImpl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.example.console.TraineeConsoleImpl;
import org.example.console.TrainerConsoleImpl;
import org.example.console.TrainingConsoleImpl;
import org.example.entity.TrainingEntity;
import org.example.entity.dto.TrainingDto;
import org.example.service.TrainingService;
import org.example.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
    private ModelMapper modelMapper;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private Scanner scanner;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {

        scanner = mock(Scanner.class);
        Field scannerField = TrainingConsoleImpl.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(trainingConsole, scanner);
    }

    @Test
    public void testCreateTraining_ValidInput() {
        when(scanner.nextLine()).thenReturn("traineeUser", "trainerUser", "TrainingName", "TrainingType", "2024-09-07",
                "01:00");

        when(validationUtils.validateTraineeExists("traineeUser")).thenReturn(null);
        when(validationUtils.validateTrainerExists("trainerUser")).thenReturn(null);
        when(validationUtils.validateBirthDate("2024-09-07")).thenReturn(true);
        when(validationUtils.validateTrainingDuration("01:00")).thenReturn(null);

        TrainingEntity trainingEntity = new TrainingEntity();
        when(modelMapper.map(any(TrainingDto.class), eq(TrainingEntity.class))).thenReturn(trainingEntity);

        trainingConsole.createTraining();

        verify(traineeConsole).viewAllTrainee();
        verify(trainerConsole).viewAllTrainer();
        verify(validationUtils).validateTraineeExists("traineeUser");
        verify(validationUtils).validateTrainerExists("trainerUser");
        verify(validationUtils).validateBirthDate("2024-09-07");
        verify(validationUtils).validateTrainingDuration("01:00");
        verify(trainingService).createTraining(trainingEntity);
        verify(modelMapper).map(any(TrainingDto.class), eq(TrainingEntity.class));
        verifyNoMoreInteractions(trainingService, modelMapper);

        assertTrue(true, "Method createTraining() should be invoked.");
        System.out.println("Test for createTraining() passed.");
    }

    @Test
    public void testViewTraining_TrainingExists() {
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
    }
}
