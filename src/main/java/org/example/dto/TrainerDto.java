package org.example.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDto {
    private TrainingTypeDto specialization;
    private UserDto user;
    private Set<TraineeDto> trainees;
    private TrainingDto training;


}
