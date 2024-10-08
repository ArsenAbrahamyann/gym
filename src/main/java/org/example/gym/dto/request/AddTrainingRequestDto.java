package org.example.gym.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddTrainingRequestDto {
    String traineeUsername;
    String trainerUsername;
    String trainingName;
    LocalDateTime trainingDate;
    Integer trainingDuration;
}
