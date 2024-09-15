package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.dto.UserDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
import org.example.repository.TraineeRepository;
import org.example.repository.TrainerRepository;
import org.example.repository.UserRepository;
import org.example.utils.UserUtils;
import org.example.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {

    @Mock
    private UserUtils userUtils;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ValidationUtils validationUtils;

    @InjectMocks
    private TraineeService traineeService;

    private UserEntity userEntity;
    private TraineeEntity traineeEntity;
    private TraineeDto traineeDto;
    private TrainerEntity trainerEntity;

    /**
     * Initializes test data and entities before each test method is executed.
     * This method is executed before each test to ensure a consistent test environment.
     */
    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("testPass");

        trainerEntity = new TrainerEntity();
        trainerEntity.setId(1L);
        trainerEntity.setUser(userEntity);

        traineeEntity = new TraineeEntity();
        traineeEntity.setUser(userEntity);

        traineeDto = new TraineeDto();
        traineeDto.setUser(new UserDto("TestFirstName", "TestLastName", true));
        traineeDto.setDateOfBirth(new Date());
        traineeDto.setAddress("123 Test Street");
        Set<TrainerDto> trainerDtos = new HashSet<>();
        trainerDtos.add(new TrainerDto());
        traineeDto.setTrainers(trainerDtos);
    }

    @Test
    public void testCreateTraineeProfile() {
        when(userRepository.findAllUsername()).thenReturn(Optional.of(List.of("testUser")));
        when(userUtils.generateUsername(anyString(), anyString(), any())).thenReturn("newUser");
        when(userUtils.generatePassword()).thenReturn("newPass");

        TraineeDto result = traineeService.createTraineeProfile(traineeDto);
        verify(userRepository).findAllUsername();
        verify(userUtils).generateUsername(anyString(), anyString(), any());
        verify(userUtils).generatePassword();
        verify(userRepository).save(any(UserEntity.class));
        assertEquals(result.getUser().getFirstName(), "TestFirstName");
        verify(traineeRepository).save(any(TraineeEntity.class));
        verify(validationUtils).validateTrainee(any(TraineeEntity.class));
        assertEquals(result.getUser().getLastName(), "TestLastName");
    }

    @Test
    public void testChangeTraineePassword() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        doNothing().when(validationUtils).validatePasswordMatch(any(UserEntity.class), anyString());

        traineeService.changeTraineePassword("testUser", "newPass");

        verify(userRepository).findByUsername(anyString());
        verify(validationUtils).validatePasswordMatch(any(UserEntity.class), anyString());
        verify(userRepository).update(any(UserEntity.class));
    }



    @Test
    public void testUpdateTraineeTrainers() {
        TrainerEntity newTrainerEntity = new TrainerEntity();
        newTrainerEntity.setId(2L);
        newTrainerEntity.setUser(userEntity);

        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.of(traineeEntity));
        when(trainerRepository.findAllById(any())).thenReturn(Optional.of(List.of(newTrainerEntity)));
        doNothing().when(validationUtils).validateUpdateTraineeTrainerList(any(TraineeEntity.class), anyList());

        traineeService.updateTraineeTrainers("testUser", List.of(2L));

        verify(traineeRepository).findByTraineeFromUsername(anyString());
        verify(trainerRepository).findAllById(any());
        verify(validationUtils).validateUpdateTraineeTrainerList(any(TraineeEntity.class), anyList());
        verify(traineeRepository).update(any(TraineeEntity.class));
    }

    @Test
    public void testUpdateTraineeProfile() {
        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.of(traineeEntity));
        doNothing().when(validationUtils).validateUpdateTrainee(any(TraineeEntity.class));

        traineeService.updateTraineeProfile("testUser", traineeDto);

        verify(traineeRepository).findByTraineeFromUsername(anyString());
        verify(traineeRepository).update(any(TraineeEntity.class));
        verify(validationUtils).validateUpdateTrainee(any(TraineeEntity.class));
    }
}
