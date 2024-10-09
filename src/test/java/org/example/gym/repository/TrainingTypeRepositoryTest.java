package org.example.gym.repository;

import java.util.Optional;
import org.example.gym.entity.TrainingTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeRepositoryTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    private TrainingTypeEntity trainingTypeEntity;

    /**
     * Sets up the test environment by initializing a {@link TrainingTypeEntity} instance
     * before each test is executed.
     */
    @BeforeEach
    public void setUp() {
        trainingTypeEntity = new TrainingTypeEntity();
        trainingTypeEntity.setId(1L);
        trainingTypeEntity.setTrainingTypeName("Yoga");
    }


    @Test
    public void testFindById_ValidId_ReturnsTrainingTypeEntity() {
        when(trainingTypeRepository.findById(1L)).thenReturn(Optional.of(trainingTypeEntity));

        Optional<TrainingTypeEntity> result = trainingTypeRepository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(trainingTypeEntity);
    }


    @Test
    public void testFindById_InvalidId_ReturnsEmpty() {
        when(trainingTypeRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<TrainingTypeEntity> result = trainingTypeRepository.findById(2L);

        assertThat(result).isNotPresent();
    }

    @Test
    public void testFindByTrainingTypeName_ValidName_ReturnsTrainingTypeEntity() {
        when(trainingTypeRepository.findByTrainingTypeName("Yoga")).thenReturn(Optional.of(trainingTypeEntity));

        Optional<TrainingTypeEntity> result = trainingTypeRepository.findByTrainingTypeName("Yoga");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(trainingTypeEntity);
    }


    @Test
    public void testFindByTrainingTypeName_InvalidName_ReturnsEmpty() {
        when(trainingTypeRepository.findByTrainingTypeName("Pilates")).thenReturn(Optional.empty());

        Optional<TrainingTypeEntity> result = trainingTypeRepository.findByTrainingTypeName("Pilates");

        assertThat(result).isNotPresent();
    }
}
