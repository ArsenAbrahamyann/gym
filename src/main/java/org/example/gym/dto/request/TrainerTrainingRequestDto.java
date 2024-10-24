package org.example.gym.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerTrainingRequestDto {
    private String trainerUsername;
    private LocalDateTime periodFrom;
    private LocalDateTime periodTo;
    private String traineeName;
}
