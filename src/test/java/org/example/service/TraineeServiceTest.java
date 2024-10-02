package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.example.dto.TraineeDto;
import org.example.dto.UserDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
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
    void testChangeTraineePassword_Success() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        doNothing().when(validationUtils).validatePasswordMatch(any(), anyString());
        doNothing().when(userService).update(any());

        traineeService.changeTraineePassword("username", "newPassword");

        verify(userService).update(userEntity);
    }

    @Test
    void testToggleTraineeStatus_Success() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        doNothing().when(userService).update(any());

        traineeService.toggleTraineeStatus("username");

        verify(userService).update(userEntity);
        assertEquals(false, userEntity.getIsActive());
    }

    @Test
    void testUpdateTraineeTrainers_Success() {
        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.of(traineeEntity));
        when(trainerService.findAllById(any())).thenReturn(List.of(trainerEntity));

        traineeService.updateTraineeTrainers("username", List.of(1L));

        verify(traineeRepository).update(traineeEntity);
    }

    @Test
    void testUpdateTraineeProfile_Success() {
        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.of(traineeEntity));
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        doNothing().when(validationUtils).validateUpdateTrainee(any());

        traineeService.updateTraineeProfile("username", traineeDto);

        verify(traineeRepository).update(traineeEntity);
    }

    @Test
    void testDeleteTraineeByUsername_Success() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.of(traineeEntity));
        doNothing().when(traineeRepository).delete(any());

        traineeService.deleteTraineeByUsername("username");

        verify(traineeRepository).delete(traineeEntity);
    }


    @Test
    void testFindById_Success() {
        when(traineeRepository.findById(anyLong())).thenReturn(Optional.of(traineeEntity));

        Optional<TraineeEntity> result = traineeService.findById(1L);

        assertEquals(traineeEntity, result.get());
    }

    @Test
    void testFindById_NotFound() {
        when(traineeRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<TraineeEntity> result = traineeService.findById(1L);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void testFindByTraineeFromUsername_Success() {
        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.of(traineeEntity));

        Optional<TraineeEntity> result = traineeService.getTrainee("username");

        assertEquals(traineeEntity, result.get());
    }

    @Test
    void testFindByTraineeFromUsername_NotFound() {
        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.empty());

        Optional<TraineeEntity> result = traineeService.getTrainee("username");

        assertEquals(Optional.empty(), result);
    }
}
