package org.example.consoleImpl;

import org.example.console.TrainerConsoleImpl;
import org.example.console.UserConsoleImpl;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
import org.example.entity.dto.TrainerDto;
import org.example.entity.dto.UserDto;
import org.example.service.TrainerService;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerConsoleImplTest {
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
        underTest.setScanner(scanner);
    }

    @Test
    public void testCreateTrainerSuccess() {
        UserDto mockUserDto = new UserDto();
        mockUserDto.setUserName("testUser");

        TrainerDto mockTrainerDto = new TrainerDto();
        mockTrainerDto.setUserId("testUser");
        mockTrainerDto.setSpecialization("Java");

        TrainerEntity mockTrainerEntity = new TrainerEntity();
        mockTrainerEntity.setUserId("testUser");
        mockTrainerEntity.setSpecialization("Java");

        when(userConsole.createUser()).thenReturn(mockUserDto);
        when(scanner.nextLine()).thenReturn("Java");

        underTest.createTrainer();

        verify(trainerService, times(1)).createTrainer(mockTrainerEntity);
    }

    @Test
    public void testCreateTrainer_ExceptionHandling() {
        when(userConsole.createUser()).thenThrow(new RuntimeException("User creation failed"));

        underTest.createTrainer();

        verify(trainerService, never()).createTrainer(any(TrainerEntity.class));
    }

    @Test
    public void testCreateTrainer_UserCreationException() {
        when(userConsole.createUser()).thenThrow(new RuntimeException("User creation failed"));

        underTest.createTrainer();

        verify(trainerService, never()).createTrainer(any(TrainerEntity.class));
    }

    @Test
    public void testCreateTrainerFailure() {
        when(userConsole.createUser()).thenThrow(new RuntimeException("User creation failed"));

        String input = "Java\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        underTest.createTrainer();

        verify(userConsole).createUser();
    }

    @Test
    public void testCreateTrainer_ScannerException() {
        UserDto userDto = new UserDto();
        userDto.setUserName("username");

        when(userConsole.createUser()).thenReturn(userDto);
        when(scanner.nextLine()).thenThrow(new RuntimeException("Scanner error"));

        underTest.createTrainer();

        verify(trainerService, never()).createTrainer(any(TrainerEntity.class));
    }

    @Test
    public void testCreateTrainer_Validation() {
        UserDto userDto = new UserDto();
        userDto.setUserName("username");

        when(userConsole.createUser()).thenReturn(userDto);
        when(scanner.nextLine()).thenReturn("Specialization");

        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setUserId(userDto.getUserName());
        trainerDto.setSpecialization("Specialization");

        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUserId(trainerDto.getUserId());
        trainerEntity.setSpecialization(trainerDto.getSpecialization());

        doThrow(new RuntimeException("Service error")).when(trainerService).createTrainer(trainerEntity);

        underTest.createTrainer();

        verify(userConsole).createUser();
        verify(scanner).nextLine();
        verify(trainerService).createTrainer(trainerEntity);
    }

    @Test
    public void testUpdateTrainer_TrainerNotFound() {
        String username = "nonExistentUsername";

        when(scanner.nextLine()).thenReturn(username);
        when(trainerService.getTrainer(username)).thenReturn(null);

        underTest.updateTrainer();

        verify(userService, never()).updateUser(any(UserEntity.class));
        verify(userService, never()).deleteUserByUsername(anyString());
        verify(trainerService, never()).updateTrainer(anyString(), any(TrainerEntity.class));
        verify(trainerService, never()).deleteTrainer(anyString());
    }

    @Test
    public void testUpdateTrainer_ExceptionHandling() {
        String username = "trainerUsername";
        when(scanner.nextLine()).thenReturn(username);
        when(trainerService.getTrainer(username)).thenThrow(new RuntimeException("Service error"));

        underTest.updateTrainer();

        verify(userService, never()).updateUser(any(UserEntity.class));
        verify(userService, never()).deleteUserByUsername(anyString());
        verify(trainerService, never()).updateTrainer(anyString(), any(TrainerEntity.class));
        verify(trainerService, never()).deleteTrainer(anyString());
    }

    @Test
    void testDeleteTrainerSuccess() {
        String username = "trainer123";
        doNothing().when(userService).deleteUserByUsername(anyString());
        doNothing().when(trainerService).deleteTrainer(anyString());
        when(scanner.nextLine()).thenReturn(username);

        underTest.deleteTrainer();

        verify(userService, times(1)).deleteUserByUsername(username);
        verify(trainerService, times(1)).deleteTrainer(username);
    }

    @Test
    void testDeleteTrainerFailure() {
        String username = "trainer123";
        doThrow(new RuntimeException("Deletion error")).when(userService).deleteUserByUsername(anyString());
        when(scanner.nextLine()).thenReturn(username);

        underTest.deleteTrainer();

        verify(userService, times(1)).deleteUserByUsername(username);
        verify(trainerService, never()).deleteTrainer(username);
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
    public void shouldHandleTrainerNotFoundDuringUpdate() {
        when(scanner.nextLine()).thenReturn("NonExistentUser", "Yoga");

        when(trainerService.getTrainer("NonExistentUser")).thenReturn(null);

        underTest.updateTrainer();

        verify(trainerService, never()).updateTrainer(anyString(), any(TrainerEntity.class));
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
