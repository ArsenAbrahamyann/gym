package org.example.gym.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.example.gym.dto.response.TrainingTypesResponseDto;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.mapper.TrainingTypeMapper;
import org.example.gym.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeControllerTest {
    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private TrainingTypeMapper mapper;

    @InjectMocks
    private TrainingTypeController trainingTypeController;

    /**
     * Set up the necessary mocks before each test.
     * Initializes the mocks and prepares the controller for testing.
     */
    @BeforeEach
    public void setUp() {
    }


    @Test
    public void testGetTrainingTypes() {
        // Arrange
        TrainingTypeEntity entity1 = new TrainingTypeEntity(); // Assuming proper constructor and fields
        TrainingTypeEntity entity2 = new TrainingTypeEntity();
        List<TrainingTypeEntity> trainingTypes = Arrays.asList(entity1, entity2);

        TrainingTypesResponseDto dto1 = new TrainingTypesResponseDto(); // Assuming proper constructor and fields
        TrainingTypesResponseDto dto2 = new TrainingTypesResponseDto();
        List<TrainingTypesResponseDto> responseDtos = Arrays.asList(dto1, dto2);

        when(trainingTypeService.findAll()).thenReturn(trainingTypes);
        when(mapper.entityMapToResponse(trainingTypes)).thenReturn(responseDtos);

        // Act
        ResponseEntity<List<TrainingTypesResponseDto>> response = trainingTypeController.getTrainingTypes();

        // Assert
        verify(trainingTypeService).findAll();
        verify(mapper).entityMapToResponse(trainingTypes);
        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody() != null;
        assert response.getBody().size() == 2; // Expecting two items
        assert response.getBody().equals(responseDtos); // Verifying the content
    }
}
