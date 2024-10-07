package org.example.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.entity.TrainingTypeEntity;
import org.example.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeServiceTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    private TrainingTypeEntity trainingTypeEntity;

    /**
     * Sets up the test environment before each test method is executed.
     * <p>
     * This method initializes a {@link TrainingTypeEntity} object with sample data, such as an ID and training type name.
     * The {@code @BeforeEach} annotation ensures that this setup is executed before each individual test,
     * providing a consistent starting state for the tests.
     * </p>
     * <p>
     * The {@link TrainingTypeEntity} object is used in the test cases to verify the behavior of the
     * {@link TrainingTypeService} methods.
     * </p>
     */
    @BeforeEach
    void setUp() {
        trainingTypeEntity = new TrainingTypeEntity();
        trainingTypeEntity.setId(1L);
        trainingTypeEntity.setTrainingTypeName("Strength Training");
    }

    @Test
    void testSave_Success() {
        doNothing().when(trainingTypeRepository).save(any(TrainingTypeEntity.class));

        trainingTypeService.save(trainingTypeEntity);

        verify(trainingTypeRepository, times(1)).save(trainingTypeEntity);
    }

    @Test
    void testFindById_Success() {
        when(trainingTypeRepository.findById(1L)).thenReturn(Optional.of(trainingTypeEntity));

        Optional<TrainingTypeEntity> result = trainingTypeService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(trainingTypeEntity, result.get());
        verify(trainingTypeRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(trainingTypeRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<TrainingTypeEntity> result = trainingTypeService.findById(2L);

        assertFalse(result.isPresent());
        verify(trainingTypeRepository, times(1)).findById(2L);
    }

    @Test
    void testSave_Exception() {
        doThrow(new RuntimeException("Database Error")).when(trainingTypeRepository)
                .save(any(TrainingTypeEntity.class));

        assertDoesNotThrow(() -> trainingTypeService.save(trainingTypeEntity));

        verify(trainingTypeRepository, times(1)).save(trainingTypeEntity);
    }

}
