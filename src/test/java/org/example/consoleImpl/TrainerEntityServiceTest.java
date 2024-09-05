package org.example.consoleImpl;

import org.example.console.TrainerConsoleImpl;
import org.example.console.UserConsoleImpl;
import org.example.entity.TrainerEntity;
import org.example.service.TrainerService;
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
public class TrainerEntityServiceTest {
    @Mock
    private TrainerService trainerService;
    @Mock
    private UserConsoleImpl userConsole;
    @Mock
    private UserService userService;
    @Mock
    private Scanner scanner;
    @InjectMocks
    private TrainerConsoleImpl underTest;

    @BeforeEach
    public void setUp() {
        underTest = new TrainerConsoleImpl(trainerService,userConsole,userService);
        underTest.setScanner(scanner);
    }

    @Test
    public void shouldSetScannerSuccessfully() {
        Scanner customScanner = new Scanner(System.in);
        underTest.setScanner(customScanner);
    }

    @Test
    public void shouldThrowExceptionWhenSettingNullScanner() {
        assertThrows(IllegalArgumentException.class, () -> underTest.setScanner(null));
    }

    @Test
    public void shouldCreateTrainerSuccessfully() {
        when(scanner.nextLine()).thenReturn("Jane", "Smith", "Fitness");

        doNothing().when(trainerService).createTrainer(any(TrainerEntity.class));

        underTest.createTrainer();

        verify(trainerService).createTrainer(any(TrainerEntity.class));
    }

    @Test
    public void shouldHandleExceptionDuringTrainerCreation() {
        when(scanner.nextLine()).thenReturn("Jane", "Smith", "Fitness");
        doThrow(new RuntimeException("Creation Error")).when(trainerService).createTrainer(any(TrainerEntity.class));

        underTest.createTrainer();

        verify(trainerService).createTrainer(any(TrainerEntity.class));
    }

    @Test
    public void shouldUpdateTrainerSuccessfully() {
        when(scanner.nextLine()).thenReturn("JaneSmith", "Yoga");

        TrainerEntity existingTrainerEntity = new TrainerEntity();
        existingTrainerEntity.setUserId("JaneSmith");
        when(trainerService.getTrainer("JaneSmith")).thenReturn(existingTrainerEntity);
        doNothing().when(trainerService).updateTrainer(anyString(), any(TrainerEntity.class));

        underTest.updateTrainer();

        verify(trainerService).updateTrainer(anyString(), any(TrainerEntity.class));
    }

    @Test
    public void shouldHandleTrainerNotFoundDuringUpdate() {
        when(scanner.nextLine()).thenReturn("NonExistentUser", "Yoga");

        when(trainerService.getTrainer("NonExistentUser")).thenReturn(null);

        underTest.updateTrainer();

        verify(trainerService, never()).updateTrainer(anyString(), any(TrainerEntity.class));
    }

    @Test
    public void shouldHandleExceptionDuringTrainerUpdate() {
        when(scanner.nextLine()).thenReturn("JaneSmith", "Yoga");

        TrainerEntity existingTrainerEntity = new TrainerEntity();
        existingTrainerEntity.setUserId("JaneSmith");
        when(trainerService.getTrainer("JaneSmith")).thenReturn(existingTrainerEntity);
        doThrow(new RuntimeException("Update Error")).when(trainerService).updateTrainer(anyString(), any(TrainerEntity.class));

        underTest.updateTrainer();

        verify(trainerService).updateTrainer(anyString(), any(TrainerEntity.class));
    }

    @Test
    public void shouldDeleteTrainerSuccessfully() {
        when(scanner.nextLine()).thenReturn("JaneSmith");

        TrainerEntity existingTrainerEntity = new TrainerEntity();
        existingTrainerEntity.setUserId("JaneSmith");
        when(trainerService.getTrainer("JaneSmith")).thenReturn(existingTrainerEntity);
        doNothing().when(trainerService).deleteTrainer(anyString());

        underTest.deleteTrainer();

        verify(trainerService).deleteTrainer(anyString());
    }

    @Test
    public void shouldHandleTrainerNotFoundDuringDeletion() {
        when(scanner.nextLine()).thenReturn("NonExistentUser");

        when(trainerService.getTrainer("NonExistentUser")).thenReturn(null);

        underTest.deleteTrainer();

        verify(trainerService, never()).deleteTrainer(anyString());
    }

    @Test
    public void shouldHandleExceptionDuringTrainerDeletion() {
        when(scanner.nextLine()).thenReturn("JaneSmith");

        TrainerEntity existingTrainerEntity = new TrainerEntity();
        existingTrainerEntity.setUserId("JaneSmith");
        when(trainerService.getTrainer("JaneSmith")).thenReturn(existingTrainerEntity);
        doThrow(new RuntimeException("Deletion Error")).when(trainerService).deleteTrainer(anyString());

        underTest.deleteTrainer();

        verify(trainerService).deleteTrainer(anyString());
    }

    @Test
    public void shouldViewTrainerSuccessfully() {
        when(scanner.nextLine()).thenReturn("JaneSmith");

        TrainerEntity existingTrainerEntity = new TrainerEntity();
        existingTrainerEntity.setUserId("JaneSmith");
        existingTrainerEntity.setSpecialization("Fitness");
        when(trainerService.getTrainer("JaneSmith")).thenReturn(existingTrainerEntity);

        underTest.viewTrainer();

        verify(trainerService).getTrainer(anyString());
    }

    @Test
    public void shouldHandleTrainerNotFoundDuringView() {
        when(scanner.nextLine()).thenReturn("NonExistentUser");

        when(trainerService.getTrainer("NonExistentUser")).thenReturn(null);

        underTest.viewTrainer();

        verify(trainerService).getTrainer(anyString());
    }

    @Test
    public void shouldHandleExceptionDuringTrainerView() {
        when(scanner.nextLine()).thenReturn("JaneSmith");

        doThrow(new RuntimeException("View Error")).when(trainerService).getTrainer(anyString());

        underTest.viewTrainer();

        verify(trainerService).getTrainer(anyString());
    }

    @Test
    public void shouldViewAllTrainersSuccessfully() {
        List<TrainerEntity> trainerEntities = new ArrayList<>();
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUserId("JaneSmith");
        trainerEntities.add(trainerEntity);
        when(trainerService.getAllTrainers()).thenReturn(trainerEntities);

        underTest.viewAllTrainer();

        verify(trainerService).getAllTrainers();
    }

    @Test
    public void shouldHandleNoTrainersFoundDuringViewAll() {
        when(trainerService.getAllTrainers()).thenReturn(new ArrayList<>());

        underTest.viewAllTrainer();

        verify(trainerService).getAllTrainers();
    }

    @Test
    public void shouldHandleExceptionDuringViewAllTrainers() {
        doThrow(new RuntimeException("View All Error")).when(trainerService).getAllTrainers();

        underTest.viewAllTrainer();

        verify(trainerService).getAllTrainers();
    }

    @Test
    public void shouldPrintMenuSuccessfully() {
        underTest.printMenu();
    }
}
