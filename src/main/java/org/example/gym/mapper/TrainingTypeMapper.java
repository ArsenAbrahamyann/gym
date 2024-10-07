package org.example.gym.mapper;

import java.util.ArrayList;
import java.util.List;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.paylod.response.TrainingTypesResponseDto;
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
