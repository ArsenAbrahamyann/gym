package org.example.entity;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingEntity {
    private String traineeId;
    private String trainerId;
    private String trainingName;
    private TrainingTypeEntity trainingTypeEntity;
    private String trainingDate;
    private Duration trainingDuration;
}
