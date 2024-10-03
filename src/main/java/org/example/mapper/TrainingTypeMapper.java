package org.example.mapper;

import java.util.ArrayList;
import java.util.List;
import org.example.entity.TrainingTypeEntity;
import org.example.paylod.response.TrainingTypesResponseDto;
import org.springframework.stereotype.Component;

@Component
public class TrainingTypeMapper {
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
