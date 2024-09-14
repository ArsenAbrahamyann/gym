package org.example.console;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.example.entity.TrainerEntity;
import org.example.entity.dto.TrainerDto;
import org.example.service.TrainerService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
public class TrainerConsoleImplTest {

    private final Logger log = LoggerFactory.getLogger(TrainerConsoleImplTest.class);
    @Mock
    private TrainerService trainerService;
    @Mock
    private ValidationUtils validationUtils;
    @Mock
    private UserConsoleImpl userConsole;
    @Mock
    private UserService userService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private TrainerConsoleImpl trainerConsoleImpl;
    @Mock
    private Scanner scanner;

    /**
     * Sets up the test environment before each test case for {@link TrainerConsoleImpl}.
     * This method does the following:
     * <ul>
     *   <li>Mocks the {@code Scanner} object to simulate user input during tests.</li>
     *   <li>Uses reflection to retrieve the private {@code scanner} field from {@link TrainerConsoleImpl}.</li>
     *   <li>Sets the mocked {@code Scanner} instance into the {@code trainerConsoleImpl} object, allowing tests
     *       to interact with a mock {@code Scanner} for controlled input.</li>
     * </ul>
     *
     * @throws NoSuchFieldException if the {@code scanner} field is not found in {@link TrainerConsoleImpl}.
     * @throws IllegalAccessException if the {@code scanner} field is inaccessible or cannot be modified.
     */
    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {

        scanner = mock(Scanner.class);
        Field scannerField = TrainerConsoleImpl.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(trainerConsoleImpl, scanner);
    }

    @Test
    public void testCreateTrainer_ValidInput() {
        when(scanner.nextLine()).thenReturn("John", "Doe", "true", "Expert");

        List<String> existingUsernames = new ArrayList<>();
        existingUsernames.add("sgfsd.sdfsdf");
        existingUsernames.add("sdfsd.dsf");

        when(userService.getAllUsernames()).thenReturn(existingUsernames);

        try (MockedStatic<UserUtils> mockedUserUtils = mockStatic(UserUtils.class)) {
            mockedUserUtils.when(() -> UserUtils.generateUsername("John", "Doe", existingUsernames))
                    .thenReturn("John.Doe");
            mockedUserUtils.when(UserUtils::generatePassword)
                    .thenReturn("password123");

            TrainerEntity mockTrainerEntity = new TrainerEntity();
            when(modelMapper.map(any(TrainerDto.class), eq(TrainerEntity.class))).thenReturn(mockTrainerEntity);

            when(validationUtils.isValidBoolean("true")).thenReturn(true);

            trainerConsoleImpl.createTrainer();

            verify(userService).getAllUsernames();
            verify(trainerService).createTrainer(any(TrainerEntity.class));
            verify(modelMapper).map(any(TrainerDto.class), eq(TrainerEntity.class));
            verify(validationUtils).isValidBoolean("true");

            System.out.println("Test for createTrainer() passed.");
        }
    }

    @Test
    void updateTrainer_shouldUpdateTrainerSuccessfully() {
        String username = "trainerUser";
        String newFirstName = "John";
        String newLastName = "Doe";

        TrainerEntity existingTrainer = new TrainerEntity();
        existingTrainer.setUserId(username);

        TrainerEntity trainerDto = new TrainerEntity();
        trainerDto.setUserId(username);
        TrainerEntity updatedTrainer = new TrainerEntity();
        updatedTrainer.setUserId(username);
        updatedTrainer.setFirstName(newFirstName);
        updatedTrainer.setLastName(newLastName);
        String newSpecialization = "Fitness";
        updatedTrainer.setSpecialization(newSpecialization);

        when(trainerService.getTrainer(username)).thenReturn(existingTrainer);
        when(modelMapper.map(existingTrainer, TrainerEntity.class)).thenReturn(trainerDto);
        when(modelMapper.map(trainerDto, TrainerEntity.class)).thenReturn(updatedTrainer);

        when(scanner.nextLine()).thenReturn(username, newFirstName, newLastName, newSpecialization);

        trainerConsoleImpl.updateTrainer();

        verify(trainerService).getTrainer(username);
    }

    @Test
    void testViewTrainer() {
        String username = "trainer1";
        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUserId(username);
        trainerEntity.setSpecialization("Java");

        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setUserId(username);
        trainerDto.setSpecialization("Java");

        when(scanner.nextLine()).thenReturn(username);
        when(trainerService.getTrainer(username)).thenReturn(trainerEntity);
        when(modelMapper.map(trainerEntity, TrainerDto.class)).thenReturn(trainerDto);
        doNothing().when(userConsole).printAllUsername();

        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        trainerConsoleImpl.viewTrainer();

        String output = outputStream.toString().trim();
        assertTrue(output.contains("Username: trainer1"));
        assertTrue(output.contains("Specialization: Java"));

        System.setOut(originalOut);

        verify(trainerService).getTrainer(username);
        verify(userConsole).printAllUsername();
        verify(modelMapper).map(trainerEntity, TrainerDto.class);
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
        log.info("Test for printMenu() passed.");
    }
}
