package org.example.gym.paylod.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerTrainingRequestDto {
    private String trainerUsername;
    private LocalDateTime periodFrom;
    private LocalDateTime periodTo;
    private String traineeName;
}
