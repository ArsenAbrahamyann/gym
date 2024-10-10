package org.example.gym.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTraineeTrainerListRequestDto {
    private String traineeUsername;
    private List<String> trainerUsername;
}
