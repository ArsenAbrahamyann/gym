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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingConsoleImplTest {
    @Mock
    private TrainingService trainingService;
    @Mock
    private TraineeConsoleImpl traineeConsole;
    @Mock
    private TrainerConsoleImpl trainerConsole;
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private UserConsoleImpl userConsole;
    @Mock
    private Scanner scanner;
    @InjectMocks
    private TrainingConsoleImpl underTest;

    @BeforeEach
    public void setUp() {
        underTest = new TrainingConsoleImpl(trainingService,traineeConsole,trainerConsole,userConsole,traineeService,trainerService);
        underTest.setScanner(scanner);
    }

    @Test
    void testCreateTrainingSuccessful() {
        String traineeUsername = "traineeUser";
        String trainerUsername = "trainerUser";
        String trainingName = "Training1";
        String trainingTypeName = "Type1";
        LocalDate trainingDate = LocalDate.of(2024, 9, 6);
        Duration trainingDuration = Duration.ofHours(1).plusMinutes(30);

        TraineeEntity trainee = new TraineeEntity();
        TrainerEntity trainer = new TrainerEntity();
        TrainingDto trainingDto = new TrainingDto();
        TrainingEntity trainingEntity = new TrainingEntity();

        when(scanner.nextLine()).thenReturn(traineeUsername, trainerUsername, trainingName, trainingTypeName, "2024-09-06", "01:30");
        when(traineeService.getTrainee(traineeUsername)).thenReturn(trainee);
        when(trainerService.getTrainer(trainerUsername)).thenReturn(trainer);

        underTest.createTraining();

        verify(trainingService).createTraining(any(TrainingEntity.class));
    }

    @Test
    void testCreateTrainingTraineeNotFound() {
        when(scanner.nextLine()).thenReturn("traineeUser", "trainerUser", "Training1", "Type1", "2024-09-06", "01:30");
        when(traineeService.getTrainee("traineeUser")).thenReturn(null);

        underTest.createTraining();

        verify(trainingService, never()).createTraining(any(TrainingEntity.class));
    }

    @Test
    void testCreateTrainingTrainerNotFound() {
        when(scanner.nextLine()).thenReturn("traineeUser", "trainerUser", "Training1", "Type1", "2024-09-06", "01:30");
        when(traineeService.getTrainee("traineeUser")).thenReturn(new TraineeEntity());
        when(trainerService.getTrainer("trainerUser")).thenReturn(null);

        underTest.createTraining();

        verify(trainingService, never()).createTraining(any(TrainingEntity.class));
    }

    @Test
    void testUpdateTrainingTrainingNotFound() {
        when(trainingService.getTraining(anyString())).thenReturn(null);

        underTest.updateTraining();
    }

    @Test
    void testUpdateTrainingTraineeNotFound() {
        when(trainingService.getTraining(anyString())).thenReturn(new TrainingEntity());
        when(traineeService.getTrainee(anyString())).thenReturn(null);

        underTest.updateTraining();
    }

    @Test
    void testUpdateTrainingTrainerNotFound() {
        when(trainingService.getTraining(anyString())).thenReturn(new TrainingEntity());
        when(traineeService.getTrainee(anyString())).thenReturn(new TraineeEntity());
        when(trainerService.getTrainer(anyString())).thenReturn(null);

        underTest.updateTraining();
    }
    @Test
    public void shouldNotCreateTrainingWithInvalidDate() {
        when(scanner.nextLine()).thenReturn(
                "trainee1", "trainer1", "Training1", "Yoga",
                "invalid-date", "01:30"
        );

        underTest.createTraining();

        verify(trainingService, never()).createTraining(any(TrainingEntity.class));
    }

    @Test
    public void shouldNotCreateTrainingWithInvalidDuration() {
        when(scanner.nextLine()).thenReturn(
                "trainee1", "trainer1", "Training1", "Yoga",
                "2024-09-01", "invalid-duration"
        );

        underTest.createTraining();

        verify(trainingService, never()).createTraining(any(TrainingEntity.class));
    }


    @Test
    public void shouldNotUpdateTrainingWithInvalidDate() {
        TrainingEntity existingTraining = new TrainingEntity();
        when(scanner.nextLine()).thenReturn(
                "ExistingTraining", "trainee2", "trainer2",
                "UpdatedTraining", "Pilates", "invalid-date", "02:00"
        );
        when(trainingService.getTraining("ExistingTraining")).thenReturn(existingTraining);

        underTest.updateTraining();

        verify(trainingService, never()).updateTraining(eq("ExistingTraining"), any(TrainingEntity.class));
    }

    @Test
    public void shouldNotUpdateTrainingWithInvalidDuration() {
        TrainingEntity existingTraining = new TrainingEntity();
        when(scanner.nextLine()).thenReturn(
                "ExistingTraining", "trainee2", "trainer2",
                "UpdatedTraining", "Pilates", "2024-09-02", "invalid-duration"
        );
        when(trainingService.getTraining("ExistingTraining")).thenReturn(existingTraining);

        underTest.updateTraining();

        verify(trainingService, never()).updateTraining(eq("ExistingTraining"), any(TrainingEntity.class));
    }


    @Test
    public void shouldViewTrainingSuccessfully() {
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTraineeId("trainee1");
        trainingEntity.setTrainerId("trainer1");
        trainingEntity.setTrainingName("Training1");
        trainingEntity.setTrainingTypeEntity(new TrainingTypeEntity("Yoga"));
        trainingEntity.setTrainingDate("2024-09-01");
        trainingEntity.setTrainingDuration(Duration.ofHours(1).plusMinutes(30));
        when(scanner.nextLine()).thenReturn("Training1");
        when(trainingService.getTraining("Training1")).thenReturn(trainingEntity);

        underTest.viewTraining();

        verify(trainingService).getTraining("Training1");
    }

    @Test
    public void shouldNotViewNonExistentTraining() {
        when(scanner.nextLine()).thenReturn("NonExistentTraining");
        when(trainingService.getTraining("NonExistentTraining")).thenReturn(null);

        underTest.viewTraining();

        verify(trainingService).getTraining("NonExistentTraining");
    }

    @Test
    public void shouldViewAllTrainingsSuccessfully() {
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTraineeId("trainee1");
        trainingEntity.setTrainerId("trainer1");
        trainingEntity.setTrainingName("Training1");
        trainingEntity.setTrainingTypeEntity(new TrainingTypeEntity("Yoga"));
        trainingEntity.setTrainingDate("2024-09-01");
        trainingEntity.setTrainingDuration(Duration.ofHours(1).plusMinutes(30));

        when(trainingService.getAllTrainings()).thenReturn(List.of(trainingEntity));

        underTest.viewAllTrainings();

        verify(trainingService).getAllTrainings();
    }

    @Test
    public void shouldHandleEmptyTrainingListGracefully() {
        when(trainingService.getAllTrainings()).thenReturn(List.of());

        underTest.viewAllTrainings();

        verify(trainingService).getAllTrainings();
    }

    @Test
    public void shouldSetScannerSuccessfully() {
        Scanner newScanner = mock(Scanner.class);
        underTest.setScanner(newScanner);

        verifyNoInteractions(newScanner);
    }

    @Test
    public void shouldThrowExceptionWhenSettingNullScanner() {
        assertThrows(IllegalArgumentException.class, () -> underTest.setScanner(null));
    }
}
