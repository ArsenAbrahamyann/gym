package org.example.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDto {
    private String traineeId;
    private String trainerId;
    private String trainingName;
    private TrainingTypeDto trainingTypeDto;
    private String trainingDate;
    private String trainingDuration;
}
