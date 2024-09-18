package org.example.service;

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
import org.example.repository.TrainerRepository;
import org.example.repository.UserRepository;
import org.example.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserRepository userRepository;

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
    public void testCreateTraineeProfile_Success() {

        when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userService.authenticateUser(anyString(), anyString()));

        TraineeDto result = traineeService.createTraineeProfile(traineeDto);

        verify(userRepository).findByUsername(userEntity.getUsername());
        verify(userRepository).save(userEntity);
        verify(userService).authenticateUser(userEntity.getUsername(), userEntity.getPassword());
        verify(traineeRepository).save(traineeEntity);

        assertEquals(result.getUser().getFirstName(), "TestFirstName");
        assertEquals(result.getUser().getLastName(), "TestLastName");
    }

    @Test
    public void testUpdateTraineeTrainers_Success() {
        TrainerEntity newTrainerEntity = new TrainerEntity();
        newTrainerEntity.setId(2L);
        newTrainerEntity.setUser(userEntity);

        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.of(traineeEntity));
        when(trainerRepository.findAllById(any())).thenReturn(Optional.of(List.of(newTrainerEntity)));
        doNothing().when(validationUtils).validateUpdateTraineeTrainerList(any(TraineeEntity.class), any());

        traineeService.updateTraineeTrainers("testUser", List.of(2L));

        verify(traineeRepository).findByTraineeFromUsername("testUser");
        verify(trainerRepository).findAllById(List.of(2L));
        verify(validationUtils).validateUpdateTraineeTrainerList(traineeEntity, List.of(newTrainerEntity));
        verify(traineeRepository).update(traineeEntity);
    }

    @Test
    public void testUpdateTraineeProfile_Success() {
        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.of(traineeEntity));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        doNothing().when(validationUtils).validateUpdateTrainee(any(TraineeEntity.class));

        traineeService.updateTraineeProfile("testUser", traineeDto);

        verify(traineeRepository).findByTraineeFromUsername("testUser");
        verify(traineeRepository).update(traineeEntity);
        verify(validationUtils).validateUpdateTrainee(traineeEntity);
    }

    @Test
    public void testCreateTraineeProfile_UserAlreadyExists() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));

        TraineeDto result = traineeService.createTraineeProfile(traineeDto);

        verify(userRepository).findByUsername(userEntity.getUsername());
        verify(userRepository, never()).save(any(UserEntity.class));
        verify(traineeRepository).save(traineeEntity);

        assertEquals(traineeDto, result);
    }

    @Test
    public void testChangeTraineePassword_UserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                traineeService.changeTraineePassword("nonExistentUser", "newPass"));

        verify(userRepository).findByUsername("nonExistentUser");
        verify(userRepository, never()).update(any(UserEntity.class));
    }

    @Test
    public void testDeleteTraineeByUsername_Success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.of(traineeEntity));
        doNothing().when(traineeRepository).delete(traineeEntity);

        traineeService.deleteTraineeByUsername("testUser");

        verify(userRepository).findByUsername("testUser");
        verify(traineeRepository).findByTraineeFromUsername("testUser");
        verify(traineeRepository).delete(traineeEntity);
    }

    @Test
    public void testDeleteTraineeByUsername_UserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                traineeService.deleteTraineeByUsername("nonExistentUser"));

        verify(userRepository).findByUsername("nonExistentUser");
        verify(traineeRepository, never()).findByTraineeFromUsername(anyString());
        verify(traineeRepository, never()).delete(any(TraineeEntity.class));
    }

    @Test
    public void testDeleteTraineeByUsername_TraineeNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        when(traineeRepository.findByTraineeFromUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                traineeService.deleteTraineeByUsername("testUser"));

        verify(userRepository).findByUsername("testUser");
        verify(traineeRepository).findByTraineeFromUsername("testUser");
        verify(traineeRepository, never()).delete(any(TraineeEntity.class));
    }
}
