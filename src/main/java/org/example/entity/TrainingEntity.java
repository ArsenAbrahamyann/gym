package org.example.entity;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingEntity  {
    private String traineeId;
    private String trainerId;
    private String trainingName;
    private TrainingTypeEntity trainingTypeEntity;
    private String trainingDate;
    private Duration trainingDuration;
}
