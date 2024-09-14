package org.example.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainingTypeEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDto {
    private Long id;
    private TrainingTypeEntity specialization;
    private UserDto user;
    private Set<TraineeEntity> trainees;
}
