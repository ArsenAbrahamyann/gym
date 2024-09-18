//package org.example.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.notNull;
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.anyList;
//import static org.mockito.Mockito.anyLong;
//import static org.mockito.Mockito.anyString;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//import javax.persistence.EntityNotFoundException;
//import org.example.dto.TrainerDto;
//import org.example.dto.TrainingTypeDto;
//import org.example.dto.UserDto;
//import org.example.entity.TrainerEntity;
//import org.example.entity.TrainingEntity;
//import org.example.entity.TrainingTypeEntity;
//import org.example.entity.UserEntity;
//import org.example.exeption.ResourceNotFoundException;
//import org.example.repository.TrainerRepository;
//import org.example.repository.TrainingRepository;
//import org.example.repository.UserRepository;
//import org.example.utils.UserUtils;
//import org.example.utils.ValidationUtils;
//import org.hibernate.type.LocalDateType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class TrainerServiceTest {
//
//    @Mock
//    private TrainerRepository trainerRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private TrainingRepository trainingRepository;
//
//    @Mock
//    private UserUtils userUtils;
//
//    @Mock
//    private ValidationUtils validationUtils;
//
//    @InjectMocks
//    private TrainerService trainerService;
//
//    private TrainerDto trainerDto;
//    private TrainerEntity trainerEntity;
//    private UserEntity userEntity;
//
////    @BeforeEach
////    void setup() {
////        // Mock data initialization
////        userEntity = new UserEntity(1L, "John", "Doe", "johndoe",
////                "password", true);
////        trainerEntity = new TrainerEntity(1L, new TrainingTypeEntity(1L, "Yoga"),
////                userEntity, new HashSet<>(),new TrainingEntity());
////        trainerDto = new TrainerDto(new TrainingTypeDto("Yoga"),
////                new UserDto("John", "Doe", true, "fsg", "sdf"),
////                new HashSet<>());
////    }
//
//    @Test
//    void testCreateTrainerProfile() {
//        // Given
//        List<String> allUsernames = Arrays.asList("existinguser");
//        when(userRepository.findAllUsername()).thenReturn(Optional.of(allUsernames));
//        when(userUtils.generateUsername(anyString(), anyString(), anyList())).thenReturn("newuser");
//        when(userUtils.generatePassword()).thenReturn("newpassword");
//
//        // When
//        TrainerDto result = trainerService.createTrainerProfile(trainerDto);
//
//        // Then
//        verify(userRepository, times(1)).save(any(UserEntity.class));
//        verify(trainerRepository, times(1)).save(any(TrainerEntity.class));
//        assertEquals("John", result.getUser().getFirstName());
//    }
//
//    @Test
//    void testChangeTrainerPassword() {
//        // Given
//        when(trainerRepository.findByTrainerFromUsername(anyString())).thenReturn(Optional.of(trainerEntity));
//
//        // When
//        trainerService.changeTrainerPassword("johndoe", "newpassword");
//
//        // Then
//        verify(userRepository, times(1)).update(any(UserEntity.class));
//        assertEquals("newpassword", trainerEntity.getUser().getPassword());
//    }
//
//    @Test
//    void testToggleTrainerStatus() {
//        // Given
//        when(trainerRepository.findByTrainerFromUsername(anyString())).thenReturn(Optional.of(trainerEntity));
//        Boolean initialStatus = trainerEntity.getUser().getIsActive();
//
//        // When
//        trainerService.toggleTrainerStatus("johndoe");
//
//        // Then
//        verify(userRepository, times(1)).update(any(UserEntity.class));
//        assertNotEquals(initialStatus, trainerEntity.getUser().getIsActive());
//    }
//
//    @Test
//    void testUpdateTrainerProfileWithNonExistingTrainer() {
//        // Given
//        when(trainerRepository.findByTrainerFromUsername(anyString())).thenReturn(Optional.empty());
//
//        // When & Then
//        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
//            trainerService.updateTrainerProfile("nonexistent", trainerDto);
//        });
//        assertEquals("Trainer not found with username: nonexistent", exception.getMessage());
//    }
//
//    @Test
//    void testGetTrainerTrainingsWithNoTrainings() {
//        // Given
//        when(trainerRepository.findByTrainerFromUsername(anyString())).thenReturn(Optional.of(trainerEntity));
//        when(trainingRepository.findTrainingsForTrainer(anyLong(), any(Date.class), any(Date.class), anyString()))
//                .thenReturn(Optional.empty());
//
//        // When & Then
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
//            trainerService.getTrainerTrainings("johndoe", new LocalDateTime(), new LocalDateTime(), "traineeName");
//        });
//        assertEquals("Trainings not found", exception.getMessage());
//    }
//}
