package org.example.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"training", "trainees"})
@EqualsAndHashCode(exclude = {"training", "trainees"})
public class TrainerDto {
    private TrainingTypeDto specialization;
    private UserDto user;
    @JsonManagedReference
    private Set<TraineeDto> trainees;
    @JsonBackReference
    private TrainingDto training;


}
