package org.example.gym.mapper;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.response.TrainingTypesResponseDto;
import org.example.gym.entity.TrainingTypeEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting TrainingTypeEntity objects to TrainingTypesResponseDto objects.
 */
@Component
@Slf4j
public class TrainingTypeMapper {

    /**
     * Maps a list of TrainingTypeEntity objects to a list of TrainingTypesResponseDto objects.
     *
     * @param all List of TrainingTypeEntity objects to be mapped.
     * @return List of mapped TrainingTypesResponseDto objects.
     * @throws IllegalArgumentException if the input list is null.
     */
    public List<TrainingTypesResponseDto> entityMapToResponse(List<TrainingTypeEntity> all) {
        List<TrainingTypesResponseDto> responseDtos = new ArrayList<>();
        for (TrainingTypeEntity entity : all) {
            TrainingTypesResponseDto responseDto = new TrainingTypesResponseDto(entity.getTrainingTypeName(),
                    entity.getId());
            responseDtos.add(responseDto);
        }
        return responseDtos;
    }
}
