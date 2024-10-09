package org.example.gym.mapper;

import java.util.Arrays;
import java.util.List;
import org.example.gym.dto.response.TrainingTypesResponseDto;
import org.example.gym.entity.TrainingTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

/**
 * Unit test for TrainingTypeMapper.
 * This test verifies the mapping functionality from TrainingTypeEntity to TrainingTypesResponseDto.
 */
@ExtendWith(MockitoExtension.class)
public class TrainingTypeMapperTest {

    @InjectMocks
    private TrainingTypeMapper trainingTypeMapper;

    @Mock
    private TrainingTypeEntity trainingTypeEntity1;

    @Mock
    private TrainingTypeEntity trainingTypeEntity2;

    /**
     * Sets up the necessary data before each test execution.
     */
    @BeforeEach
    void setup() {
        lenient().when(trainingTypeEntity1.getTrainingTypeName()).thenReturn("Yoga");
        lenient().when(trainingTypeEntity1.getId()).thenReturn(1L);

        lenient().when(trainingTypeEntity2.getTrainingTypeName()).thenReturn("Pilates");
        lenient().when(trainingTypeEntity2.getId()).thenReturn(2L);
    }


    @Test
    void testEntityMapToResponse() {
        // Prepare test data
        List<TrainingTypeEntity> entities = Arrays.asList(trainingTypeEntity1, trainingTypeEntity2);

        // Call the method under test
        List<TrainingTypesResponseDto> responseDtos = trainingTypeMapper.entityMapToResponse(entities);

        // Assertions
        assertThat(responseDtos).hasSize(2);
        assertThat(responseDtos.get(0).getTrainingType()).isEqualTo("Yoga");
        assertThat(responseDtos.get(0).getTrainingTypeId()).isEqualTo(1L);
        assertThat(responseDtos.get(1).getTrainingType()).isEqualTo("Pilates");
        assertThat(responseDtos.get(1).getTrainingTypeId()).isEqualTo(2L);
    }

}
