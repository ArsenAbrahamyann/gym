package org.example.gym.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeTrainingsRequestDto {

    private String traineeName;
    private LocalDateTime periodFrom;
    private LocalDateTime periodTo;
    private String trainerName;
    private String trainingType;
}
