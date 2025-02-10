package org.example.gym.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerWorkloadRequestDto {

    private String trainerUsername;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private LocalDateTime trainingDate;
    private Integer trainingDuration;
    private String actionType;
}
