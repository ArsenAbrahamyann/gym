package org.example.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Objects;
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
    @JsonManagedReference
    private Set<TraineeDto> trainees;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainerDto that = (TrainerDto) o;
        return Objects.equals(specialization, that.specialization) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specialization, user);
    }
}
