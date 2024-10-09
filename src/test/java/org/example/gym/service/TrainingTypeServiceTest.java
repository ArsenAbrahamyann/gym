package org.example.gym.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeServiceTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    /**
     * Initializes the mock environment before each test.
     * <p>
     * This method sets up the necessary mocking behavior for the repository and
     * injects them into the service class being tested.
     * </p>
     */
    @BeforeEach
    public void setUp() {
    }

    /**
     * Tests the {@link TrainingTypeService#findByTrainingTypeName(String)} method
     * when the training type exists.
     */
    @Test
    void testFindByTrainingTypeNameWhenExists() {
        String trainingTypeName = "Yoga";
        TrainingTypeEntity mockTrainingType = new TrainingTypeEntity(1L, trainingTypeName);

        when(trainingTypeRepository.findByTrainingTypeName(trainingTypeName))
                .thenReturn(Optional.of(mockTrainingType));

        Optional<TrainingTypeEntity> result = trainingTypeService.findByTrainingTypeName(trainingTypeName);

        assertThat(result).isPresent();
        assertThat(result.get().getTrainingTypeName()).isEqualTo(trainingTypeName);
        verify(trainingTypeRepository, times(1)).findByTrainingTypeName(trainingTypeName);
    }

    /**
     * Tests the {@link TrainingTypeService#findByTrainingTypeName(String)} method
     * when the training type does not exist.
     */
    @Test
    void testFindByTrainingTypeNameWhenNotExists() {
        String trainingTypeName = "Unknown";

        when(trainingTypeRepository.findByTrainingTypeName(trainingTypeName))
                .thenReturn(Optional.empty());

        Optional<TrainingTypeEntity> result = trainingTypeService.findByTrainingTypeName(trainingTypeName);

        assertThat(result).isEmpty();
        verify(trainingTypeRepository, times(1)).findByTrainingTypeName(trainingTypeName);
    }

    /**
     * Tests the {@link TrainingTypeService#findById(Long)} method when the training
     * type exists.
     */
    @Test
    void testFindByIdWhenExists() {
        Long trainingTypeId = 1L;
        TrainingTypeEntity mockTrainingType = new TrainingTypeEntity(trainingTypeId, "Cardio");

        when(trainingTypeRepository.findById(trainingTypeId)).thenReturn(Optional.of(mockTrainingType));

        TrainingTypeEntity result = trainingTypeService.findById(trainingTypeId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(trainingTypeId);
        verify(trainingTypeRepository, times(1)).findById(trainingTypeId);
    }

    /**
     * Tests the {@link TrainingTypeService#findById(Long)} method when the training
     * type does not exist, and an exception is thrown.
     */
    @Test
    void testFindByIdWhenNotExists() {
        Long trainingTypeId = 99L;

        when(trainingTypeRepository.findById(trainingTypeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainingTypeService.findById(trainingTypeId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("TrainingType not found for ID");

        verify(trainingTypeRepository, times(1)).findById(trainingTypeId);
    }

    /**
     * Tests the {@link TrainingTypeService#findAll()} method when training types exist.
     */
    @Test
    void testFindAllWhenExists() {
        TrainingTypeEntity type1 = new TrainingTypeEntity(1L, "Cardio");
        TrainingTypeEntity type2 = new TrainingTypeEntity(2L, "Strength");

        when(trainingTypeRepository.findAll()).thenReturn(Arrays.asList(type1, type2));

        List<TrainingTypeEntity> result = trainingTypeService.findAll();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(2);
        verify(trainingTypeRepository, times(1)).findAll();
    }

    /**
     * Tests the {@link TrainingTypeService#findAll()} method when no training types exist.
     */
    @Test
    void testFindAllWhenNotExists() {
        when(trainingTypeRepository.findAll()).thenReturn(Arrays.asList());

        List<TrainingTypeEntity> result = trainingTypeService.findAll();

        assertThat(result).isEmpty();
        verify(trainingTypeRepository, times(1)).findAll();
    }
}
