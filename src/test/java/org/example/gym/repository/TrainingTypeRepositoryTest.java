//package org.example.repository;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.Optional;
//import org.example.entity.TrainingTypeEntity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class TrainingTypeRepositoryTest {
//
//    @Mock
//    private TrainingTypeRepository trainingTypeRepository;
//
//    @Mock
//    private TrainingTypeEntity trainingTypeEntity;
//
//    /**
//     * Set up the test environment by initializing the training type entity.
//     * This method prepares a sample entity for use in the test methods.
//     */
//    @BeforeEach
//    public void setUp() {
//        trainingTypeEntity = new TrainingTypeEntity();
//        trainingTypeEntity.setId(1L);
//        trainingTypeEntity.setTrainingTypeName("Yoga");
//    }
//
//    @Test
//    public void testSave() {
//        doNothing().when(trainingTypeRepository).save(any(TrainingTypeEntity.class));
//
//        trainingTypeRepository.save(trainingTypeEntity);
//
//        verify(trainingTypeRepository, times(1)).save(any(TrainingTypeEntity.class));
//    }
//
//    @Test
//    public void testFindById() {
//        when(trainingTypeRepository.findById(anyLong())).thenReturn(Optional.of(trainingTypeEntity));
//
//        Optional<TrainingTypeEntity> result = trainingTypeRepository.findById(1L);
//
//        assertTrue(result.isPresent());
//        assertEquals(trainingTypeEntity.getId(), result.get().getId());
//        assertEquals(trainingTypeEntity.getTrainingTypeName(), result.get().getTrainingTypeName());
//        verify(trainingTypeRepository, times(1)).findById(anyLong());
//    }
//}
