package org.example.paylod.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTrainerTrainingListResponseDto {

    private String trainingName;
    private String trainingDate;
    private String trainingType;
    private String trainingDuration;
    private String traineeName;
}
