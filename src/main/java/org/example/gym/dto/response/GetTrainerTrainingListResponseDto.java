package org.example.gym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gym.entity.TrainingTypeEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTrainerTrainingListResponseDto {

    private String trainingName;
    private String trainingDate;
    private TrainingTypeEntity trainingType;
    private Integer trainingDuration;
    private String traineeName;
}
