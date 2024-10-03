package org.example.paylod.response;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.TrainingTypeEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingResponseDto {
    private String name;
    private LocalDateTime date;
    private TrainingTypeEntity type;
    private Integer duration;
    private String trainerName;
}
