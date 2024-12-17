package org.example.gym.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddTrainingRequestDto {
    private String traineeUsername;
    private String trainerUsername;
    private String trainingName;
    private LocalDateTime trainingDate;
    private Integer trainingDuration;
}
