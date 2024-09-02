package org.example.entity;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Training implements Serializable {
    @Serial
    private static final long serialVersionUID = -5517414036571605326L;
    private String traineeId;
    private String trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private String trainingDate;
    private Duration trainingDuration;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training that = (Training) o;
        return Objects.equals(traineeId, that.traineeId) &&
               Objects.equals(trainerId, that.trainerId) &&
               Objects.equals(trainingName, that.trainingName) &&
               Objects.equals(trainingDate, that.trainingDate) &&
               Objects.equals(trainingDuration, that.trainingDuration) &&
               Objects.equals(trainingType, that.trainingType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traineeId, trainerId, trainingName, trainingDate, trainingDuration, trainingType);
    }


}
