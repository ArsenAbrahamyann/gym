package org.example.paylod.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerResponseDto {

    private String trainerName;
    private String firstName;
    private String lastName;
    private Long trainingTypeId;
}
