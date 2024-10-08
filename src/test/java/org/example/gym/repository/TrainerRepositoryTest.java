//package org.example.repository;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//import org.example.entity.TrainerEntity;
//import org.example.entity.TrainingTypeEntity;
//import org.example.entity.UserEntity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class TrainerRepositoryTest {
//    @Mock
//    private TrainerRepository trainerRepository;
//
//    @Mock
//    private TrainerEntity trainer;
//
//    @Mock
//    private UserEntity user;
//
//    @Mock
//    private TrainingTypeEntity specialization;
//
//
//    /**
//     * Initializes test data and entities before each test method is executed.
//     * This method sets up the environment with a sample user, a specialization, and a trainer entity.
//     */
//    @BeforeEach
//    public void setUp() {
//        user = new UserEntity();
//        user.setId(1L);
//        user.setUsername("testtrainer");
//        user.setPassword("password");
//        user.setFirstName("Jane");
//        user.setLastName("Doe");
//        user.setIsActive(true);
//
//        specialization = new TrainingTypeEntity();
//        specialization.setId(1L);
//
//        trainer = new TrainerEntity();
//        trainer.setId(1L);
//        trainer.setSpecialization(specialization);
//        trainer.setUser(user);
//
//        Set<TrainerEntity> trainers = new HashSet<>();
//        trainers.add(trainer);
//    }
//
//    @Test
//    public void testFindByTrainerFromUsername() {
//        when(trainerRepository.findByTrainerFromUsername(any(String.class))).thenReturn(Optional.of(trainer));
//
//        Optional<TrainerEntity> result = trainerRepository.findByTrainerFromUsername("testtrainer");
//
//        assertTrue(result.isPresent());
//        assertEquals(trainer.getUser().getUsername(), result.get().getUser().getUsername());
//        verify(trainerRepository, times(1)).findByTrainerFromUsername(any(String.class));
//    }
//
//    @Test
//    public void testFindById() {
//        when(trainerRepository.findById(anyLong())).thenReturn(Optional.of(trainer));
//
//        Optional<TrainerEntity> result = trainerRepository.findById(1L);
//
//        assertTrue(result.isPresent());
//        assertEquals(trainer.getId(), result.get().getId());
//        verify(trainerRepository, times(1)).findById(anyLong());
//    }
//
//    @Test
//    public void testSave() {
//        doNothing().when(trainerRepository).save(any(TrainerEntity.class));
//
//        trainerRepository.save(trainer);
//
//        verify(trainerRepository, times(1)).save(any(TrainerEntity.class));
//    }
//
//    @Test
//    public void testUpdate() {
//        doNothing().when(trainerRepository).update(any(TrainerEntity.class));
//
//        trainerRepository.update(trainer);
//
//        verify(trainerRepository, times(1)).update(any(TrainerEntity.class));
//    }
//
//    @Test
//    public void testFindAll() {
//        List<TrainerEntity> trainers = new ArrayList<>();
//        trainers.add(trainer);
//        when(trainerRepository.findAll()).thenReturn(trainers);
//
//        List<TrainerEntity> result = trainerRepository.findAll();
//
//        assertEquals(trainers.size(), result.size());
//        verify(trainerRepository, times(1)).findAll();
//    }
//
//    @Test
//    public void testFindAssignedTrainers() {
//        List<TrainerEntity> trainers = new ArrayList<>();
//        trainers.add(trainer);
//        when(trainerRepository.findAssignedTrainers(anyLong())).thenReturn(trainers);
//
//        List<TrainerEntity> result = trainerRepository.findAssignedTrainers(1L);
//
//        assertEquals(trainers.size(), result.size());
//        verify(trainerRepository, times(1)).findAssignedTrainers(anyLong());
//    }
//
//    @Test
//    public void testFindAllById() {
//        List<TrainerEntity> trainers = new ArrayList<>();
//        trainers.add(trainer);
//        when(trainerRepository.findAllById(anyList())).thenReturn(trainers);
//
//        List<TrainerEntity> result = trainerRepository.findAllById(List.of(1L));
//
//        assertEquals(trainers.size(), result.size());
//        verify(trainerRepository, times(1)).findAllById(anyList());
//    }
//}
