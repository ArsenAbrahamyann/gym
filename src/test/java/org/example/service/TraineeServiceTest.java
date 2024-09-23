package org.example.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.example.dto.TraineeDto;
import org.example.dto.UserDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.TraineeRepository;
import org.example.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerService trainerService;

    @Mock
    private UserService userService;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TraineeService traineeService;

    private UserEntity userEntity;
    private TraineeEntity traineeEntity;
    private TraineeDto traineeDto;
    private TrainerEntity trainerEntity;

    /**
     * Initializes the test data before each test case.
     * This method is annotated with {@code @BeforeEach}, meaning it runs before each test method in the class.
     * It creates instances of the entities {@code UserEntity}, {@code TrainerEntity}, {@code TraineeEntity}, and
     * the corresponding DTO {@code TraineeDto}, setting their relevant fields with test values.
     *
     * <p>Specifically, it performs the following steps:
     * <ul>
     *     <li>Initializes a {@code UserEntity} object with the username "testUser", password "testPass", and sets its active status to {@code true}.</li>
     *     <li>Creates a {@code TrainerEntity} object, assigns it an ID of 1L, and links it to the {@code userEntity}.</li>
     *     <li>Creates a {@code TraineeEntity} object and links it to the {@code userEntity}.</li>
     *     <li>Initializes a {@code TraineeDto} object, setting the user's first name, last name, and active status in the {@code UserDto}, as well as setting the date of birth and address fields for the trainee.</li>
     * </ul>
     */
    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("testPass");
        userEntity.setIsActive(true);

        trainerEntity = new TrainerEntity();
        trainerEntity.setId(1L);
        trainerEntity.setUser(userEntity);

        traineeEntity = new TraineeEntity();
        traineeEntity.setUser(userEntity);

        traineeDto = new TraineeDto();
        traineeDto.setUser(new UserDto("TestFirstName", "TestLastName", true, "fg", "sfd"));
        traineeDto.setDateOfBirth(LocalDateTime.now());
        traineeDto.setAddress("123 Test Street");

    }

    @Test
    public void testUpdateTraineeTrainers_Success() {
        TrainerEntity newTrainerEntity = new TrainerEntity();
        newTrainerEntity.setId(2L);
        newTrainerEntity.setUser(userEntity);

        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.of(traineeEntity));
        when(trainerService.findAllById(any())).thenReturn(Optional.of(List.of(newTrainerEntity)));
        doNothing().when(validationUtils).validateUpdateTraineeTrainerList(any(TraineeEntity.class), any());

        traineeService.updateTraineeTrainers("testUser", List.of(2L));

        verify(traineeRepository).findByTraineeFromUsername("testUser");
        verify(trainerService).findAllById(List.of(2L));
        verify(validationUtils).validateUpdateTraineeTrainerList(traineeEntity, List.of(newTrainerEntity));
        verify(traineeRepository).update(traineeEntity);
    }

    @Test
    public void testUpdateTraineeProfile_Success() {
        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.of(traineeEntity));
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        doNothing().when(validationUtils).validateUpdateTrainee(any(TraineeEntity.class));

        traineeService.updateTraineeProfile("testUser", traineeDto);

        verify(traineeRepository).findByTraineeFromUsername("testUser");
        verify(traineeRepository).update(traineeEntity);
        verify(validationUtils).validateUpdateTrainee(traineeEntity);
    }

    @Test
    public void testCreateTraineeProfile_UserAlreadyExists() {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setId(1L);

        traineeRepository.save(trainee);

        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    public void testChangeTraineePassword_UserNotFound() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                traineeService.changeTraineePassword("nonExistentUser", "newPass"));

        verify(userService).findByUsername("nonExistentUser");
        verify(userService, never()).update(any(UserEntity.class));
    }

    @Test
    public void testDeleteTraineeByUsername_Success() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.of(traineeEntity));
        doNothing().when(traineeRepository).delete(traineeEntity);

        traineeService.deleteTraineeByUsername("testUser");

        verify(userService).findByUsername("testUser");
        verify(traineeRepository).findByTraineeFromUsername("testUser");
        verify(traineeRepository).delete(traineeEntity);
    }

    @Test
    public void testDeleteTraineeByUsername_UserNotFound() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                traineeService.deleteTraineeByUsername("nonExistentUser"));

        verify(userService).findByUsername("nonExistentUser");
        verify(traineeRepository, never()).findByTraineeFromUsername(anyString());
        verify(traineeRepository, never()).delete(any(TraineeEntity.class));
    }

    @Test
    public void testDeleteTraineeByUsername_TraineeNotFound() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                traineeService.deleteTraineeByUsername("testUser"));

        verify(userService).findByUsername("testUser");
        verify(traineeRepository).findByTraineeFromUsername("testUser");
        verify(traineeRepository, never()).delete(any(TraineeEntity.class));
    }
}
