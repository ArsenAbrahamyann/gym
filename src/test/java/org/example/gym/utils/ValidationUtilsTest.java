//package org.example.utils;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//import java.util.List;
//import org.example.entity.TraineeEntity;
//import org.example.entity.TrainerEntity;
//import org.example.entity.TrainingEntity;
//import org.example.entity.TrainingTypeEntity;
//import org.example.entity.UserEntity;
//import org.example.exeption.ValidationException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class ValidationUtilsTest {
//
//    @InjectMocks
//    private ValidationUtils validationUtils;
//
//    @Mock
//    private TraineeEntity traineeEntity;
//
//    @Mock
//    private TrainerEntity trainerEntity;
//
//    @Mock
//    private TrainingEntity trainingEntity;
//
//    @Mock
//    private UserEntity userEntity;
//
//    @BeforeEach
//    void setUp() {
//        // Set up default mock behaviors if needed
//    }
//
//    @Test
//    void testValidateTrainee_Valid() {
//        when(traineeEntity.getUser()).thenReturn(userEntity);
//        when(userEntity.getUsername()).thenReturn("traineeUser");
//        when(userEntity.getPassword()).thenReturn("password");
//        when(traineeEntity.getAddress()).thenReturn("address");
//        when(userEntity.getIsActive()).thenReturn(true);
//
//        assertDoesNotThrow(() -> validationUtils.validateTrainee(traineeEntity));
//    }
//
//    @Test
//    void testValidateTrainee_UsernameMissing() {
//        when(traineeEntity.getUser()).thenReturn(userEntity);
//        when(userEntity.getUsername()).thenReturn(null);
//
//        ValidationException thrown = assertThrows(ValidationException.class,
//                () -> validationUtils.validateTrainee(traineeEntity));
//        assertEquals("Trainee username is required.", thrown.getMessage());
//    }
//
//    @Test
//    void testValidateTrainer_Valid() {
//        when(trainerEntity.getUser()).thenReturn(userEntity);
//        when(userEntity.getUsername()).thenReturn("trainerUser");
//        when(userEntity.getPassword()).thenReturn("password");
//        when(trainerEntity.getSpecialization()).thenReturn(new TrainingTypeEntity());
//        when(userEntity.getIsActive()).thenReturn(true);
//
//        assertDoesNotThrow(() -> validationUtils.validateTrainer(trainerEntity));
//    }
//
//    @Test
//    void testValidateTrainer_SpecializationMissing() {
//        when(trainerEntity.getUser()).thenReturn(userEntity);
//        when(userEntity.getUsername()).thenReturn("trainerUser");
//        when(userEntity.getPassword()).thenReturn("password");
//        when(trainerEntity.getSpecialization()).thenReturn(null);
//
//        ValidationException thrown = assertThrows(ValidationException.class,
//                () -> validationUtils.validateTrainer(trainerEntity));
//        assertEquals("Trainer specialization is required.", thrown.getMessage());
//    }
//
//    @Test
//    void testValidatePasswordMatch_Valid() {
//        when(userEntity.getPassword()).thenReturn("password");
//
//        assertDoesNotThrow(() -> validationUtils.validatePasswordMatch(userEntity, "password"));
//    }
//
//    @Test
//    void testValidateUpdateTrainee_Valid() {
//        when(traineeEntity.getId()).thenReturn(1L);
//        when(traineeEntity.getUser()).thenReturn(userEntity);
//        when(userEntity.getUsername()).thenReturn("traineeUser");
//        when(userEntity.getPassword()).thenReturn("password");
//        when(traineeEntity.getAddress()).thenReturn("address");
//        when(userEntity.getIsActive()).thenReturn(true);
//
//        assertDoesNotThrow(() -> validationUtils.validateUpdateTrainee(traineeEntity));
//    }
//
//    @Test
//    void testValidateUpdateTrainee_NoId() {
//        when(traineeEntity.getId()).thenReturn(null);
//
//        ValidationException thrown = assertThrows(ValidationException.class,
//                () -> validationUtils.validateUpdateTrainee(traineeEntity));
//        assertEquals("Trainee ID is required for updates.", thrown.getMessage());
//    }
//
//    @Test
//    void testValidateTraineeTrainingsCriteria_MissingUsername() {
//        ValidationException thrown = assertThrows(ValidationException.class,
//                () -> validationUtils.validateTraineeTrainingsCriteria(null, null, null, null, null));
//        assertEquals("Trainee username is required for fetching training list.", thrown.getMessage());
//    }
//
//    @Test
//    void testValidateUpdateTraineeTrainerList_Valid() {
//        when(traineeEntity.getId()).thenReturn(1L);
//        List<TrainerEntity> trainers = List.of(new TrainerEntity());
//
//        assertDoesNotThrow(() -> validationUtils.validateUpdateTraineeTrainerList(traineeEntity, trainers));
//    }
//
//    @Test
//    void testValidateUpdateTraineeTrainerList_NoTrainee() {
//        when(traineeEntity.getId()).thenReturn(null);
//
//        ValidationException thrown = assertThrows(ValidationException.class,
//                () -> validationUtils.validateUpdateTraineeTrainerList(traineeEntity, List.of()));
//        assertEquals("Trainee ID is required.", thrown.getMessage());
//    }
//}
