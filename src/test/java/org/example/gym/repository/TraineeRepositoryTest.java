//package org.example.repository;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.anyLong;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.HashSet;
//import java.util.Optional;
//import java.util.Set;
//import org.example.entity.TraineeEntity;
//import org.example.entity.TrainerEntity;
//import org.example.entity.UserEntity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class TraineeRepositoryTest {
//    @Mock
//    private TraineeRepository traineeRepository;
//
//    @Mock
//    private TraineeEntity trainee;
//    @Mock
//    private UserEntity user;
//
//    /**
//     * Sets up the test environment by initializing entities with sample data.
//     * This method is executed before each test in the test class.
//     */
//    @BeforeEach
//    public void setUp() {
//        user = new UserEntity();
//        user.setId(1L);
//        user.setUsername("testuser");
//        user.setPassword("password");
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setIsActive(true);
//
//        trainee = new TraineeEntity();
//        trainee.setId(1L);
//        trainee.setAddress("123 Main St");
//        trainee.setUser(user);
//
//        TrainerEntity trainer = new TrainerEntity();
//        trainer.setId(1L);
//        trainer.setUser(user);
//
//        Set<TrainerEntity> trainers = new HashSet<>();
//        trainers.add(trainer);
//        trainee.setTrainers(trainers);
//    }
//
//    @Test
//    public void testFindByTraineeFromUsername_Success() {
//        when(traineeRepository.findByTraineeFromUsername(any(String.class))).thenReturn(Optional.of(trainee));
//
//        Optional<TraineeEntity> result = traineeRepository.findByTraineeFromUsername("testuser");
//
//        assertTrue(result.isPresent());
//        assertEquals(trainee.getUser().getUsername(), result.get().getUser().getUsername());
//        verify(traineeRepository, times(1)).findByTraineeFromUsername(any(String.class));
//    }
//
//    @Test
//    public void testFindByTraineeFromUsername_NotFound() {
//        when(traineeRepository.findByTraineeFromUsername(any(String.class))).thenReturn(Optional.empty());
//
//        Optional<TraineeEntity> result = traineeRepository.findByTraineeFromUsername("nonexistentuser");
//
//        assertFalse(result.isPresent());
//        verify(traineeRepository, times(1)).findByTraineeFromUsername(any(String.class));
//    }
//
//    @Test
//    public void testFindById_Success() {
//        when(traineeRepository.findById(anyLong())).thenReturn(Optional.of(trainee));
//
//        Optional<TraineeEntity> result = traineeRepository.findById(1L);
//
//        assertTrue(result.isPresent());
//        assertEquals(trainee.getId(), result.get().getId());
//        verify(traineeRepository, times(1)).findById(anyLong());
//    }
//
//    @Test
//    public void testFindById_NotFound() {
//        when(traineeRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        Optional<TraineeEntity> result = traineeRepository.findById(99L);
//
//        assertFalse(result.isPresent());
//        verify(traineeRepository, times(1)).findById(anyLong());
//    }
//
//    @Test
//    public void testSave() {
//        doNothing().when(traineeRepository).save(any(TraineeEntity.class));
//
//        traineeRepository.save(trainee);
//
//        verify(traineeRepository, times(1)).save(any(TraineeEntity.class));
//    }
//
//    @Test
//    public void testUpdate() {
//        doNothing().when(traineeRepository).update(any(TraineeEntity.class));
//
//        traineeRepository.update(trainee);
//
//        verify(traineeRepository, times(1)).update(any(TraineeEntity.class));
//    }
//
//    @Test
//    public void testDelete() {
//        doNothing().when(traineeRepository).delete(any(TraineeEntity.class));
//
//        traineeRepository.delete(trainee);
//
//        verify(traineeRepository, times(1)).delete(any(TraineeEntity.class));
//    }
//}
