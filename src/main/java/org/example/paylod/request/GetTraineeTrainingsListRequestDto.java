package org.example.paylod.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTraineeTrainingsListRequestDto {

    @NotBlank
    private String traineeName;
    private String periodFrom;
    private String periodTo;
    private String trainingName;
    private String trainingType;
}
