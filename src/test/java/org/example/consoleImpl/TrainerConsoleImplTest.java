package org.example.consoleImpl;

import org.example.console.TrainerConsoleImpl;
import org.example.console.UserConsoleImpl;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
import org.example.entity.dto.TrainerDto;
import org.example.entity.dto.UserDto;
import org.example.service.TrainerService;
import org.example.service.UserService;
import org.example.utils.UserUtils;
import org.example.utils.ValidationUtils;
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
import java.io.InputStream;

import java.util.Arrays;
import java.util.List;

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
    private ModelMapper modelMapper;

    @Mock
    private ValidationUtils validationUtils;

    @InjectMocks
    private TrainerConsoleImpl trainerConsoleImpl;

    private final Logger log = LoggerFactory.getLogger(TrainerConsoleImplTest.class);

    @BeforeEach
    void setUp() {
        // Setup code if necessary
    }

    @Test
    void testCreateTrainer() {
        String input = "John\nDoe\ntrue\nExpert\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        List<String> existingUsernames = Arrays.asList("existingUser1");
        when(userService.getAllUsernames()).thenReturn(existingUsernames);
        when(UserUtils.generateUsername(anyString(), anyString(), anyList())).thenReturn("john.doe");
        when(UserUtils.generatePassword()).thenReturn("password123");

        TrainerDto trainerDto = new TrainerDto();
        when(modelMapper.map(any(TrainerDto.class), eq(TrainerEntity.class))).thenReturn(new TrainerEntity());

        trainerConsoleImpl.createTrainer();

        verify(trainerService).createTrainer(any(TrainerEntity.class));
        log.info("Test for createTrainer() passed.");
    }

    @Test
    void testUpdateTrainer() {
        String input = "trainerUser\nNewFirstName\nNewLastName\nNewSpecialization\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        TrainerEntity trainerEntity = new TrainerEntity();
        when(trainerService.getTrainer(anyString())).thenReturn(trainerEntity);
        when(modelMapper.map(any(TrainerEntity.class), eq(TrainerEntity.class))).thenReturn(trainerEntity);

        trainerConsoleImpl.updateTrainer();

        verify(trainerService).updateTrainer(anyString(), any(TrainerEntity.class));
        log.info("Test for updateTrainer() passed.");
    }

    @Test
    void testViewTrainer() {
        String input = "trainerUser\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        TrainerEntity trainerEntity = new TrainerEntity();
        TrainerDto trainerDto = new TrainerDto();
        when(trainerService.getTrainer(anyString())).thenReturn(trainerEntity);
        when(modelMapper.map(any(TrainerEntity.class), eq(TrainerDto.class))).thenReturn(trainerDto);

        trainerConsoleImpl.viewTrainer();

        verify(trainerService).getTrainer(anyString());
        log.info("Test for viewTrainer() passed.");
    }

    @Test
    void testViewAllTrainer() {
        TrainerEntity trainer1 = new TrainerEntity();
        TrainerEntity trainer2 = new TrainerEntity();
        List<TrainerEntity> trainers = Arrays.asList(trainer1, trainer2);

        when(trainerService.getAllTrainers()).thenReturn(trainers);
        when(modelMapper.map(any(TrainerEntity.class), eq(TrainerDto.class))).thenReturn(new TrainerDto());

        trainerConsoleImpl.viewAllTrainer();

        verify(trainerService).getAllTrainers();
        log.info("Test for viewAllTrainer() passed.");
    }

    @Test
    void testPrintMenu() {
        trainerConsoleImpl.printMenu();

        // Since printMenu() just prints to console, you might want to manually verify the output.
        // We can't verify the output directly, so this test mainly ensures the method runs without exceptions.
        log.info("Test for printMenu() passed.");
    }
}
