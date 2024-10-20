package org.example.gym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingResponseDto {
    private String name;
    private String date;
    private String type;
    private Integer duration;
    private String trainerName;
}
