package org.example.entity.dto;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.TrainingTypeEntity;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDto {
    private String traineeId;
    private String trainerId;
    private String trainingName;
    private TrainingTypeEntity trainingTypeEntity;
    private String trainingDate;
    private Duration trainingDuration;
}
