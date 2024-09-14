package org.example.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingTypeEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDto {
    private Long id;
    private TraineeEntity trainee;
    private TrainerEntity trainer;
    private String trainingName;
    private TrainingTypeEntity trainingType;
    private Date trainingDate;
    private Integer trainingDuration;
}
