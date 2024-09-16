package org.example.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDto {
    private TrainingTypeDto specialization;
    private UserDto user;
    private Set<TraineeDto> trainees;
}