package org.example.gym.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.exeption.TrainingTypeNotFoundException;
import org.example.gym.repository.TrainingTypeRepository;
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


    @Test
    void testFindByTrainingTypeNameWhenNotExists() {
        String trainingTypeName = "Unknown";

        when(trainingTypeRepository.findByTrainingTypeName(trainingTypeName))
                .thenReturn(Optional.empty());

        Optional<TrainingTypeEntity> result = trainingTypeService.findByTrainingTypeName(trainingTypeName);

        assertThat(result).isEmpty();
        verify(trainingTypeRepository, times(1)).findByTrainingTypeName(trainingTypeName);
    }


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


    @Test
    void testFindByIdWhenNotExists() {
        Long trainingTypeId = 99L;

        when(trainingTypeRepository.findById(trainingTypeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainingTypeService.findById(trainingTypeId))
                .isInstanceOf(TrainingTypeNotFoundException.class)
                .hasMessageContaining("TrainingType not found for ID: " + trainingTypeId);

        verify(trainingTypeRepository, times(1)).findById(trainingTypeId);
    }

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

    @Test
    void testFindAllWhenNotExists() {
        when(trainingTypeRepository.findAll()).thenReturn(Arrays.asList());

        List<TrainingTypeEntity> result = trainingTypeService.findAll();

        assertThat(result).isEmpty();
        verify(trainingTypeRepository, times(1)).findAll();
    }

    @Test
    void testLoggingWhenNoTrainingTypesFound() {
        when(trainingTypeRepository.findAll()).thenReturn(Arrays.asList());

        List<TrainingTypeEntity> result = trainingTypeService.findAll();

        assertThat(result).isEmpty();
        verify(trainingTypeRepository, times(1)).findAll();
    }
}