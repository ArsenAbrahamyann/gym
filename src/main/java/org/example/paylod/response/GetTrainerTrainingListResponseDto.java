package org.example.paylod.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.TrainingTypeEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTrainerTrainingListResponseDto {

    private String trainingName;
    private LocalDateTime trainingDate;
    private TrainingTypeEntity trainingType;
    private Integer trainingDuration;
    private String traineeName;
}
