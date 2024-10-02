package org.example.paylod.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetNotAssignedOnTraineeActiveTrainersRequestDto {

    @NotBlank
    private String traineeUsername;
}
