package org.example.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDto {
    private TraineeDto trainee;
    private TrainerDto trainer;
    private String trainingName;
    private TrainingTypeDto trainingType;
    private Date trainingDate;
    private Integer trainingDuration;
}
